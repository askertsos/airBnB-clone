package com.WebAppTechnologies.AirBnbClone.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "booker_id", referencedColumnName = "id")
    private User booker;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "rental_id", referencedColumnName = "id")
    private Rental rental;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer guests;
//    @Formula("(" +
//                "(" +
//                    "SELECT r.base_price " +
//                    "FROM booking b INNER JOIN rental r ON b.rental_id=r.id " +
//                    "WHERE b.id=id" +
//                ")" +
//                "*" +
//                "(" +
//                    "SELECT COUNT(*) " +
//                    "FROM booking b INNER JOIN booking_dates bd ON b.id=bd.booking_id " +
//                    "WHERE b.id=id" +
//                ")" +
//                "* guests" +
//            ")")
    private Double price;
    private LocalDateTime bookedAt;

    public Booking() {}

    public Booking(Long id, User booker, Rental rental, LocalDate startDate, LocalDate endDate, Integer guests, Double price, LocalDateTime bookedAt) {
        this.id = id;
        this.booker = booker;
        this.rental = rental;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guests = guests;
        this.price = price;
        this.bookedAt = bookedAt;
    }

    public Booking(User booker, Rental rental, LocalDate startDate, LocalDate endDate, Integer guests){
        this.booker = booker;
        this.rental = rental;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guests = guests;
        this.price = rental.getPrice(startDate.datesUntil(endDate).toList().size(),this.guests);
        this.bookedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getBooker() {
        return booker;
    }

    public void setBooker(User booker) {
        this.booker = booker;
    }

    public Rental getRental() {
        return rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getGuests() {
        return guests;
    }

    public void setGuests(Integer guests) {
        this.guests = guests;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }
}
