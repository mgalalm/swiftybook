package org.acme.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private BigDecimal totalPrice;
    private String status;
    private Date booked_from;
    private Date booked_to;
    private Long paymentId;
    private Integer guestNumber;
    private Long placeId;
    private Long customerId;
}
