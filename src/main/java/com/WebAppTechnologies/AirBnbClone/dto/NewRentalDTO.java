package com.WebAppTechnologies.AirBnbClone.dto;

import com.WebAppTechnologies.AirBnbClone.domain.Address;
import com.WebAppTechnologies.AirBnbClone.domain.Rental;

import java.util.List;

public class NewRentalDTO {

    private String title;
    private Double basePrice;
    private Double chargePerPerson;
    private List<String> availableDates;
    private Integer maxGuests;

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

    //Location
    private Address address;
    //map

    private List<String> publicTransport;

    //Amenities
    private Boolean hasWiFi;
    private Boolean hasAC;
    private Boolean hasHeating;
    private Boolean hasKitchen;
    private Boolean hasTV;
    private Boolean hasParking;
    private Boolean hasElevator;

    public NewRentalDTO() {}

    public NewRentalDTO(String title, Double basePrice, Double chargePerPerson, List<String> availableDates, Integer maxGuests, Integer beds, Integer bedrooms, Integer bathrooms, Rental.RentalType type, Boolean hasLivingRoom, Double surfaceArea, String description, Boolean allowSmoking, Boolean allowPets, Boolean allowEvents, Integer minDays, Address address, List<String> publicTransport, Boolean hasWiFi, Boolean hasAC, Boolean hasHeating, Boolean hasKitchen, Boolean hasTV, Boolean hasParking, Boolean hasElevator) {
        this.title = title;
        this.basePrice = basePrice;
        this.chargePerPerson = chargePerPerson;
        this.availableDates = availableDates;
        this.maxGuests = maxGuests;
        this.beds = beds;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.type = type;
        this.hasLivingRoom = hasLivingRoom;
        this.surfaceArea = surfaceArea;
        this.description = description;
        this.allowSmoking = allowSmoking;
        this.allowPets = allowPets;
        this.allowEvents = allowEvents;
        this.minDays = minDays;
        this.address = address;
        this.publicTransport = publicTransport;
        this.hasWiFi = hasWiFi;
        this.hasAC = hasAC;
        this.hasHeating = hasHeating;
        this.hasKitchen = hasKitchen;
        this.hasTV = hasTV;
        this.hasParking = hasParking;
        this.hasElevator = hasElevator;
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

    public List<String> getAvailableDates() {
        return availableDates;
    }

    public void setAvailableDates(List<String> availableDates) {
        this.availableDates = availableDates;
    }

    public Integer getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(Integer maxGuests) {
        this.maxGuests = maxGuests;
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

    public String toString(){
        return "New Rental: title: " + this.title;
    }
}
