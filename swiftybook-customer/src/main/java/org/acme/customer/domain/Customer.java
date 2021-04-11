package org.acme.customer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.acme.commons.domain.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends AbstractEntity {
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "telephone")
    private String telephone;

//    @EqualsAndHashCode.Exclude
//    @OneToMany(mappedBy = "customer")
//    private Set<Booking> bookings;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;
}
