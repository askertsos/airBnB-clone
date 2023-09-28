package com.WebAppTechnologies.AirBnbClone.database;

import com.WebAppTechnologies.AirBnbClone.domain.Booking;
import com.WebAppTechnologies.AirBnbClone.domain.User;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.WebAppTechnologies.AirBnbClone.domain.Rental;
import com.WebAppTechnologies.AirBnbClone.domain.Review;

import java.util.ArrayList;
import java.util.List;

public class HostProperties {

    @JacksonXmlProperty(localName = "Host_Attributes")
    private User hostInfo;

    @JacksonXmlElementWrapper(localName = "Host_Rentals")
    @JacksonXmlProperty(localName = "Rental_Information")
    private List<RentalProperties> rentals;

    public HostProperties(){}

    public HostProperties(User host, List<Rental> hostRentals, List<Booking> hostBookings, List<Review> hostReviews){
        this.rentals = new ArrayList<>();
        this.hostInfo = host;

        for (Rental rental: hostRentals) {
            List<Booking> rentalBookings = new ArrayList<>(hostBookings);
            rentalBookings.removeIf(b -> (!b.getRental().getId().equals(rental.getId())));

            List<Review> rentalReviews = new ArrayList<>(hostReviews);
            rentalReviews.removeIf(r -> (!r.getRental().getId().equals(rental.getId())));

            rentals.add(new RentalProperties(rental,rentalBookings,rentalReviews));
        }
    }

    public User getHostInfo() {
        return hostInfo;
    }

    public void setHostInfo(User hostInfo) {
        this.hostInfo = hostInfo;
    }

    public List<RentalProperties> getRentals() {
        return rentals;
    }

    public void setRentals(List<RentalProperties> rentals) {
        this.rentals = rentals;
    }
}
