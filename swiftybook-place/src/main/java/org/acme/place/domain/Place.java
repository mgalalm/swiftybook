package org.acme.place.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.acme.commons.domain.AbstractEntity;
import org.acme.place.domain.enums.PlaceStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "places", schema = "place")
public class Place  extends AbstractEntity {
/**
 *     public id: string,
 *     public title: string,
 *     public description: string,
 *     public imageUrl: string,
 *     public price: number,
 *     public availableFrom: Date,
 *     public availableTo: Date,
 *     public userId: string
 */
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PlaceStatus status;

    @NotNull
    @Column(name = "available_from", nullable = false)
    private Date availableFrom;

    @NotNull
    @Column(name = "available_to", nullable = false)
    private Date availableTo;

    @Column(name = "image_url")
    private String imageUrl;

    @Embedded
    private Address address;

//    @EqualsAndHashCode.Exclude
//    @OneToMany(mappedBy = "place")
//    private Set<Booking> bookings;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinTable(schema = "place", name = "places_reviews",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "reviews_id"))
    private Set<Review> reviews = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
