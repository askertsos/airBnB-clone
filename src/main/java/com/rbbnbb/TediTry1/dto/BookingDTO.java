package com.rbbnbb.TediTry1.dto;


import java.util.List;

public class BookingDTO {
    private List<String> dates;
    private Integer guests;

    public BookingDTO() {}

    public BookingDTO(List<String> dates, Integer guests) {
        this.dates = dates;
        this.guests = guests;
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
}
