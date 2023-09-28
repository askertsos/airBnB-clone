package com.WebAppTechnologies.AirBnbClone.dto;

import com.WebAppTechnologies.AirBnbClone.domain.User;
import com.WebAppTechnologies.AirBnbClone.domain.Rental;
import org.hibernate.annotations.Formula;

import java.util.HashSet;
import java.util.Set;

public class HostInfoDTO {
    private String first_name;
    private String last_name;
    private String email;
    private String phoneNumber;

    @Formula("(" +
            "SELECT " +
                "AVG(rev.stars) " +
            "FROM " +
                "review rev " +
                "INNER JOIN rental ren ON rev.rental_id=ren.id " +
                "INNER JOIN user u ON ren.host_id=u.id" +
            "WHERE " +
                "u.email=email" +
            ")")
    private Double rating;

    private Set<Rental> hostRentals = new HashSet<>();

    public HostInfoDTO() {}

    public HostInfoDTO(String first_name, String last_name, String email, String phoneNumber, Double rating, Set<Rental> hostRentals) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.hostRentals = hostRentals;
    }

    public HostInfoDTO(User host){
        this.first_name = host.getFirst_name();
        this.last_name = host.getLast_name();
        this.email = host.getEmail();
        this.phoneNumber = host.getPhoneNumber();
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Set<Rental> getHostRentals() {
        return hostRentals;
    }

    public void setHostRentals(Set<Rental> hostRentals) {
        this.hostRentals = hostRentals;
    }
}
