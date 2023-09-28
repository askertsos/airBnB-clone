package com.WebAppTechnologies.AirBnbClone.dto;


import java.time.LocalDate;
import java.util.List;

public class BookingDTO {
    private String startDate;
    private String endDate;
    private Integer guests;
    private Double price;

    public BookingDTO() {}

    public BookingDTO(String startDate, String endDate, Integer guests, Double price) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.guests = guests;
        this.price = price;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
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

    public String toString() {
        return "Booking: from " + this.startDate + " to " + this.endDate + " for " + this.guests + " guests.";
    }
}
