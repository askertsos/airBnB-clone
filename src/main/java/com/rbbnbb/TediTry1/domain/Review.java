package com.rbbnbb.TediTry1.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.EAGER)
    private User reviewer;

    @ManyToOne(fetch = FetchType.EAGER)
    private Rental rental;

    private String text;

    private Integer stars;
}
