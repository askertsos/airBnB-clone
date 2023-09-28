package com.WebAppTechnologies.AirBnbClone.database;

import com.WebAppTechnologies.AirBnbClone.domain.Booking;
import com.WebAppTechnologies.AirBnbClone.domain.User;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.WebAppTechnologies.AirBnbClone.domain.Rental;
import com.WebAppTechnologies.AirBnbClone.domain.Review;

import java.util.ArrayList;
import java.util.List;

//This whole package is created specifically for portraying the state of the database in a manner appropriate for a .json/.xml file
public class DatabaseSnapshot {

    @JacksonXmlElementWrapper(localName = "All_Hosts")
    @JacksonXmlProperty(localName = "Host_Information")
    private List<HostProperties> hostList;

    public DatabaseSnapshot(){}

    public DatabaseSnapshot(List<User> allHosts, List<Rental> allRentals, List<Booking> allBookings, List<Review> allReviews){
        this.hostList = new ArrayList<>();

        for (User host: allHosts) {
            List<Rental> hostRentals = new ArrayList<>(allRentals);
            hostRentals.removeIf(r -> (!r.getHost().getId().equals(host.getId())));

            List<Booking> hostBookings = new ArrayList<>(allBookings);
            hostBookings.removeIf(b->(!b.getRental().getHost().getId().equals(host.getId())));

            List<Review> hostReviews = new ArrayList<>(allReviews);
            hostReviews.removeIf(r->(!r.getRental().getHost().getId().equals(host.getId())));

            hostList.add(new HostProperties(host,hostRentals,hostBookings,hostReviews));
        }

    }

    public List<HostProperties> getHostList() {
        return hostList;
    }

    public void setHostList(List<HostProperties> hostList) {
        this.hostList = hostList;
    }

}