package org.acme.place.domain;

import io.smallrye.common.constraint.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.acme.commons.domain.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "categories",schema = "place")
public class Category extends AbstractEntity {
    @Column(name="name", nullable = false)
    private String name;

    @NotNull
    @Column(name="description", nullable = false)
    private String description;
}
