package com.dijicert.booking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "reservation")
public class Hotel extends Audit<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hotel")
    private Set<Reservation> reservations = new HashSet<>();
}
