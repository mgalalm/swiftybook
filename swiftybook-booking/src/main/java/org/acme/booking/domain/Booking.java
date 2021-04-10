package org.acme.booking.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.acme.booking.domain.enums.BookingStatus;
import org.acme.commons.domain.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * An Order.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "bookings")
public class Booking extends AbstractEntity {
    @NotNull
    @Column(name = "total_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "place_id", nullable = false)
    private Long  placeId;

    @NotNull
    @Column(name = "booked_from", nullable = false)
    private Date bookedFrom;

    @NotNull
    @Column(name = "booked_to", nullable = false)
    private Date bookedTo;

    @NotNull
    @Column(name = "guest_number", nullable = false)
    private Integer GuestNumber;

    @Column(name = "payment_id")
    private Long paymentId;

}
