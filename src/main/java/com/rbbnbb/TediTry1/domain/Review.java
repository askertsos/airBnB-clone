package com.rbbnbb.TediTry1.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tenant_reviews",
            joinColumns = {@JoinColumn (name = "REVIEW_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn (name = "TENANT_ID", referencedColumnName = "ID")}
    )
    private User reviewer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "rental_reviews",
            joinColumns = {@JoinColumn (name = "REVIEW_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn (name = "RENTAL_ID", referencedColumnName = "ID")}
    )
    private Rental rental;

    private String text;

    private Integer stars;
}
