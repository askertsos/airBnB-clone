package com.WebAppTechnologies.AirBnbClone.database;

import com.WebAppTechnologies.AirBnbClone.domain.*;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RentalProperties {
    public static enum RentalType {publicRoom, privateRoom, house}
    private String title;
    private Double basePrice; //Price just for renting the rental for the day, not including guest number
    private Double chargePerPerson;
    private Integer maxGuests;

    @JacksonXmlElementWrapper(localName = "Rental_AvailableDates")
    @JacksonXmlProperty(localName = "Date")
    private List<LocalDate> availableDates = new ArrayList<LocalDate>();

    //Space
    private Integer beds;
    private Integer bedrooms;
    private Integer bathrooms;
    private Rental.RentalType type;
    private Boolean hasLivingRoom;
    private Double surfaceArea;

    //Description
    private String description;

    //Rules
    private Boolean allowSmoking;
    private Boolean allowPets;
    private Boolean allowEvents;
    private Integer minDays;

    private Address address;
    //map

    @JacksonXmlElementWrapper(localName = "Public_Transport")
    private List<String> publicTransport;

    //Photos
    @JacksonXmlElementWrapper(localName = "Rental_Photos")
    @JacksonXmlProperty(localName = "Photo")
    private Set<Photo> photos;

    //Amenities
    private Boolean hasWiFi;
    private Boolean hasAC;
    private Boolean hasHeating;
    private Boolean hasKitchen;
    private Boolean hasTV;
    private Boolean hasParking;
    private Boolean hasElevator;


    @JacksonXmlElementWrapper(localName = "Rental_Reviews")
    @JacksonXmlProperty(localName = "Review")
    private Set<Review> reviews;

//    @JacksonXmlProperty(localName = "Rental_Attributes")
//    private Rental rental;
    @JacksonXmlElementWrapper(localName = "Rental_Bookings")
    @JacksonXmlProperty(localName = "Booking")
    private List<Booking> bookingList;

//    @JacksonXmlElementWrapper(localName = "Rental_Reviews")
//    @JacksonXmlProperty(localName = "Review_Information")
//    private List<Review> reviewList;

    public RentalProperties(){}

    public RentalProperties(Rental rental, List<Booking> bookingList, List<Review> reviewList) {
        this.title = rental.getTitle();
        this.basePrice = rental.getBasePrice();
        this.chargePerPerson = rental.getChargePerPerson();
        this.maxGuests = rental.getMaxGuests();
        this.availableDates = rental.getAvailableDates();
        this.beds = rental.getBeds();
        this.bedrooms = rental.getBedrooms();
        this.bathrooms = rental.getBathrooms();
        this.type = rental.getType();
        this.hasLivingRoom = rental.getHasLivingRoom();
        this.surfaceArea = rental.getSurfaceArea();
        this.description = rental.getDescription();
        this.allowSmoking = rental.getAllowSmoking();
        this.allowPets = rental.getAllowPets();
        this.allowEvents = rental.getAllowEvents();
        this.minDays = rental.getMinDays();
        this.address = rental.getAddress();
        this.publicTransport = rental.getPublicTransport();
        this.photos = rental.getPhotos();
        this.hasWiFi = rental.getHasWiFi();
        this.hasAC = rental.getHasAC();
        this.hasHeating = rental.getHasHeating();
        this.hasKitchen = rental.getHasKitchen();
        this.hasTV = rental.getHasTV();
        this.hasParking = rental.getHasParking();
        this.hasElevator = rental.getHasElevator();
        this.reviews = rental.getReviews();

        this.bookingList = bookingList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Double getChargePerPerson() {
        return chargePerPerson;
    }

    public void setChargePerPerson(Double chargePerPerson) {
        this.chargePerPerson = chargePerPerson;
    }

    public Integer getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(Integer maxGuests) {
        this.maxGuests = maxGuests;
    }

    public List<LocalDate> getAvailableDates() {
        return availableDates;
    }

    public void setAvailableDates(List<LocalDate> availableDates) {
        this.availableDates = availableDates;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public Integer getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
    }

    public Rental.RentalType getType() {
        return type;
    }

    public void setType(Rental.RentalType type) {
        this.type = type;
    }

    public Boolean getHasLivingRoom() {
        return hasLivingRoom;
    }

    public void setHasLivingRoom(Boolean hasLivingRoom) {
        this.hasLivingRoom = hasLivingRoom;
    }

    public Double getSurfaceArea() {
        return surfaceArea;
    }

    public void setSurfaceArea(Double surfaceArea) {
        this.surfaceArea = surfaceArea;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAllowSmoking() {
        return allowSmoking;
    }

    public void setAllowSmoking(Boolean allowSmoking) {
        this.allowSmoking = allowSmoking;
    }

    public Boolean getAllowPets() {
        return allowPets;
    }

    public void setAllowPets(Boolean allowPets) {
        this.allowPets = allowPets;
    }

    public Boolean getAllowEvents() {
        return allowEvents;
    }

    public void setAllowEvents(Boolean allowEvents) {
        this.allowEvents = allowEvents;
    }

    public Integer getMinDays() {
        return minDays;
    }

    public void setMinDays(Integer minDays) {
        this.minDays = minDays;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<String> getPublicTransport() {
        return publicTransport;
    }

    public void setPublicTransport(List<String> publicTransport) {
        this.publicTransport = publicTransport;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    public Boolean getHasWiFi() {
        return hasWiFi;
    }

    public void setHasWiFi(Boolean hasWiFi) {
        this.hasWiFi = hasWiFi;
    }

    public Boolean getHasAC() {
        return hasAC;
    }

    public void setHasAC(Boolean hasAC) {
        this.hasAC = hasAC;
    }

    public Boolean getHasHeating() {
        return hasHeating;
    }

    public void setHasHeating(Boolean hasHeating) {
        this.hasHeating = hasHeating;
    }

    public Boolean getHasKitchen() {
        return hasKitchen;
    }

    public void setHasKitchen(Boolean hasKitchen) {
        this.hasKitchen = hasKitchen;
    }

    public Boolean getHasTV() {
        return hasTV;
    }

    public void setHasTV(Boolean hasTV) {
        this.hasTV = hasTV;
    }

    public Boolean getHasParking() {
        return hasParking;
    }

    public void setHasParking(Boolean hasParking) {
        this.hasParking = hasParking;
    }

    public Boolean getHasElevator() {
        return hasElevator;
    }

    public void setHasElevator(Boolean hasElevator) {
        this.hasElevator = hasElevator;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }
}
