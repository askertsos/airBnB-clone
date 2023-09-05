package com.rbbnbb.TediTry1.domain;

import com.rbbnbb.TediTry1.dto.BookingDTO;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

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

    @ElementCollection(targetClass = LocalDate.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "booking_dates", joinColumns = @JoinColumn(name = "booking_id"))
    private List<LocalDate> dates;

    private Integer guests;
    @Formula("(" +
                "(" +
                    "SELECT r.base_price " +
                    "FROM booking b INNER JOIN rental r ON b.rental_id=r.id " +
                    "WHERE b.id=id" +
                ")" +
                "*" +
                "(" +
                    "SELECT COUNT(*) " +
                    "FROM booking b INNER JOIN booking_dates bd ON b.id=bd.booking_id " +
                    "WHERE b.id=id" +
                ")" +
                "* guests" +
            ")")
    private Double price;
    private LocalDateTime bookedAt;

    public Booking() {}

    public Booking(Long id, User booker, Rental rental, List<LocalDate> dates, Integer guests, LocalDateTime bookedAt) {
        this.id = id;
        this.booker = booker;
        this.rental = rental;
        this.dates = dates;
        this.guests = guests;
        this.bookedAt = bookedAt;
    }

    public Booking(Long id, User booker, Rental rental, BookingDTO bookingDTO){
        this.id = id;
        this.booker = booker;
        this.rental = rental;
        this.dates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        try{
            for (String stringDate: bookingDTO.getDates()) {
                this.dates.add(LocalDate.parse(stringDate,formatter));
            }
        }
        catch(DateTimeParseException e){
            System.out.println("Conversion to LocalDate unsuccessful");
        }
        this.guests = bookingDTO.getGuests();
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

    public List<LocalDate> getDates() {
        return dates;
    }

    public void setDates(List<LocalDate> dates) {
        this.dates = dates;
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
