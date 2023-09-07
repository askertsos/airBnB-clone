package com.rbbnbb.TediTry1.dto;


import java.util.List;

public class BookingDTO {
    private List<String> dates;
    private Integer guests;
    private Double price;

    public BookingDTO() {}

    public BookingDTO(List<String> dates, Integer guests, Double price) {
        this.dates = dates;
        this.guests = guests;
        this.price = price;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
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
}
