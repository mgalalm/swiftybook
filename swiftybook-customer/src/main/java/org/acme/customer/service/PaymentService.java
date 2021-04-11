package org.acme.customer.service;

import lombok.extern.slf4j.Slf4j;
import org.acme.commons.dto.BookingDto;
import org.acme.commons.dto.PaymentDto;
import org.acme.customer.client.BookingRestClient;
import org.acme.customer.domain.Payment;
import org.acme.customer.domain.enums.PaymentStatus;
import org.acme.customer.repository.PaymentRepository;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
@Transactional
public class PaymentService {

    @Inject
    PaymentRepository paymentRepository;

    @Inject
    @RestClient
    BookingRestClient bookingRestClient;

    public static PaymentDto mapToDto(Payment payment, Long bookingId) {
        if (payment != null) {
            return new PaymentDto(
                    payment.getId(),
                    payment.getPaypalPaymentId(),
                    payment.getStatus().name(),
                    bookingId
            );
        }
        return null;
    }

    public List<PaymentDto> findByPriceRange(Double max) {
        return this.paymentRepository
                .findAllByAmountBetween(BigDecimal.ZERO, BigDecimal.valueOf(max))
                .stream()
                .map(payment -> mapToDto(payment, findBookingByPaymentId(payment.getId()).getId()))
                .collect(Collectors.toList());
    }

    public List<PaymentDto> findAll() {
        log.debug("Request to get all Payments");
        return this.paymentRepository.findAll()
                .stream()
                .map(payment -> findById(payment.getId()))
                .collect(Collectors.toList());
    }

    public PaymentDto findById(Long id) {
        log.debug("Request to get Payment : {}", id);
        var booking = findBookingByPaymentId(id);

        return this.paymentRepository
                .findById(id)
                .map(payment -> mapToDto(payment, booking.getId()))
                .orElse(null);
    }

    public PaymentDto create(PaymentDto paymentDto) {
        log.debug("Request to create Payment : {}", paymentDto);

        var booking =
                this.bookingRestClient
                        .findById(paymentDto.getBookingId())
                        .orElseThrow(() -> new IllegalStateException("The Booking does not exist!"));



        var payment = this.paymentRepository.saveAndFlush(new Payment(
                paymentDto.getPaypalPaymentId(),
                PaymentStatus.valueOf(paymentDto.getStatus()),
                booking.getTotalPrice()
        ));

        booking.setStatus("PAID");
//        booking.setPayment(payment);

        this.bookingRestClient.save(booking);

        return mapToDto(payment, booking.getId());
    }

    private BookingDto findBookingByPaymentId(Long id) {
        return this.bookingRestClient.findByPaymentId(id)
                .orElseThrow(() -> new IllegalStateException("No Booking exists for the Payment ID " + id));
    }

    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        this.paymentRepository.deleteById(id);
    }
}