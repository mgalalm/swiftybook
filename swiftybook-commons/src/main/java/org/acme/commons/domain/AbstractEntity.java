package org.acme.commons.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonIgnore
    @Column(name = "created_date", nullable = false)
    private Instant createdDate = Instant.now();

    @JsonIgnore
    @Version
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;
}
