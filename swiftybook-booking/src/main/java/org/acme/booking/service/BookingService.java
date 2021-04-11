package org.acme.booking.service;


import lombok.extern.slf4j.Slf4j;
import org.acme.booking.client.PlaceRestClient;
import org.acme.booking.domain.Booking;
import org.acme.booking.domain.enums.BookingStatus;
import org.acme.booking.repository.BookingRepository;
import org.acme.commons.dto.BookingDto;
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
public class BookingService {

    @Inject
    BookingRepository bookingRepository;
    @Inject
    @RestClient
    PlaceRestClient placeRestClient;

    public static BookingDto mapToDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getPrice(),
                booking.getStatus().name(),
                booking.getBookedFrom(),
                booking.getBookedTo(),
                booking.getPaymentId()!= null ? booking.getPaymentId() : null,
                booking.getGuestNumber(),
                booking.getPlaceId(),
                booking.getCustomerId()
//                CustomerService.mapToDto(booking.getCustomer()).getId()
        );
    }

    public List<BookingDto> findAll() {
        log.debug("Request to get all Bookings");
        return this.bookingRepository.findAll()
                .stream()
                .map(BookingService::mapToDto)
                .collect(Collectors.toList());
    }

    public BookingDto findById(Long id) {
        log.debug("Request to get Booking : {}", id);
        return this.bookingRepository.findById(id)
                .map(BookingService::mapToDto)
                .orElse(null);
    }

    public List<BookingDto> findAllByUser(Long id) {
        return this.bookingRepository.findByCustomerId(id)
                .stream()
                .map(BookingService::mapToDto)
                .collect(Collectors.toList());
    }

    public BookingDto create(BookingDto bookingDto) {
        log.debug("Request to create Booking : {}", bookingDto);
        var placeId = bookingDto.getPlaceId();
        var place = this.placeRestClient.findById(placeId).orElseThrow(() -> new IllegalStateException("No Place exists by this place id " + placeId));
//        var customerId = bookingDto.getCustomerId();
        var totalPrice = place.getPrice().multiply(BigDecimal.valueOf(bookingDto.getGuestNumber()));

        return mapToDto(
                this.bookingRepository.save(
                        new Booking(
                                totalPrice,
                                BookingStatus.CREATED,
                                bookingDto.getCustomerId(),
                                bookingDto.getPlaceId(),
                                bookingDto.getBooked_from(),
                                bookingDto.getBooked_to(),
                                bookingDto.getGuestNumber(),
                                null
                        )
                )
        );
    }

    public void delete(Long id) {
        log.debug("Request to delete Booking : {}", id);

        var Booking = this.bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Booking with ID[" + id + "] cannot be found!"));

//        Optional.ofNullable(Booking.getPayment()).ifPresent(paymentRepository::delete);

        bookingRepository.delete(Booking);
    }

    public boolean existsById(Long id) {
        return this.bookingRepository.existsById(id);
    }

    public BookingDto findByPaymentId(Long id) {
        return mapToDto(
                this.bookingRepository.findByPaymentId(id)
                        .orElseThrow(() -> new IllegalStateException("Booking with ID[" + id + "] cannot be found!"))
        );
    }
}
