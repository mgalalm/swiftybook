package org.acme.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDto {
    private Long id;
    private String title;
    private String description;
    private String status;
    private BigDecimal price;
    private Date available_from;
    private Date available_to;
    private AddressDto address;
    private Set<ReviewDto> reviews;
    private Long categoryId;
    private String image_url;
}
