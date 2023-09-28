package com.WebAppTechnologies.AirBnbClone.domain;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class RecommendedRentals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", referencedColumnName="id")
    private User tenant;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="tenant_recommended_rentals",
            joinColumns={@JoinColumn(name="recommendation_id", referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="rental_id", referencedColumnName="id")})
    private Set<Rental> rentals;

    public RecommendedRentals() {}

    public RecommendedRentals(Long id, User tenant, Set<Rental> rentals) {
        this.id = id;
        this.tenant = tenant;
        this.rentals = rentals;
    }

    public RecommendedRentals(User tenant, Set<Rental> rentals) {
        this.tenant = tenant;
        this.rentals = rentals;
    }

    public Long getTenantId() {
        return id;
    }

    public void setTenantId(Long id) {
        this.id = id;
    }

    public User getTenant() {
        return tenant;
    }

    public void setTenant(User tenant) {
        this.tenant = tenant;
    }

    public Set<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(Set<Rental> rentals) {
        this.rentals = rentals;
    }
}
