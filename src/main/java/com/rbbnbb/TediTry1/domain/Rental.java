package com.rbbnbb.TediTry1.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.rbbnbb.TediTry1.dto.NewRentalDTO;
import jakarta.persistence.*;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.Formula;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Entity
@JacksonXmlRootElement(localName = "rental")
public class Rental {

    public enum RentalType {privateRoom, publicRoom, house}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Double basePrice;
    private Double chargePerPerson;
    private Integer maxGuests;

    @ElementCollection(targetClass = LocalDate.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "rental_available_dates", joinColumns = @JoinColumn(name = "rental_id"))
    private List<LocalDate> availableDates = new ArrayList<LocalDate>();

    //Space
    private Integer beds;
    private Integer bedrooms;
    private Integer bathrooms;
    private RentalType type;
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
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private Address address;
    //map
    private List<String> publicTransport;

    //Photos
    @ElementCollection(targetClass = Photo.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "rental_photos", joinColumns = @JoinColumn(name = "rental_id"))
    private Set<Photo> photos;

    //Amenities
    private Boolean hasWiFi;
    private Boolean hasAC;
    private Boolean hasHeating;
    private Boolean hasKitchen;
    private Boolean hasTV;
    private Boolean hasParking;
    private Boolean hasElevator;

    @OneToMany(mappedBy = "rental")
    private Set<Review> reviews;

    @ManyToOne(fetch = FetchType.EAGER)
    private User host;

    //Formulas
//    @Formula("reviews.stream().mapToInt(a->a.getStars()).average().orElse(0)")
//    @Formula("(SELECT SUM(r1.stars) FROM review r1 INNER JOIN rental r2 ON r1.rental=r2 WHERE r2.id=id) / (SELECT COUNT(*) FROM review r1 INNER JOIN rental r2 ON r1.rental=r2 WHERE r2.id=id)")
    @Formula("(SELECT AVG(rev.stars) FROM review rev INNER JOIN rental ren ON rev.rental_id=ren.id WHERE ren.id=id)")
    private Double rating;

    public void addReview(Review review){
        this.reviews.add(review);
    }

    public void addPhoto(Photo photo) {this.photos.add(photo); }

    public void removeAvailableDates(List<LocalDate> bookingDates){
        for (LocalDate date: bookingDates) {
            availableDates.remove(date);
        }
    }

    public Double getPrice(Integer days, Integer tenants){
        //Assume days >= minDays
        return (basePrice + tenants*chargePerPerson)*days;
    }

    public Rental(){}

    public Rental(Long id, String title, Double basePrice, Double chargePerPerson, Integer maxGuests, List<LocalDate> availableDates, Integer beds, Integer bedrooms, Integer bathrooms, RentalType type, Boolean hasLivingRoom, Double surfaceArea, String description, Boolean allowSmoking, Boolean allowPets, Boolean allowEvents, Integer minDays, Address address, List<String> publicTransport, Set<Photo> photos, Boolean hasWiFi, Boolean hasAC, Boolean hasHeating, Boolean hasKitchen, Boolean hasTV, Boolean hasParking, Boolean hasElevator, Set<Review> reviews, User host, Double rating) {
        this.id = id;
        this.title = title;
        this.basePrice = basePrice;
        this.chargePerPerson = chargePerPerson;
        this.maxGuests = maxGuests;
        this.availableDates = availableDates;
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
        this.photos = photos;
        this.hasWiFi = hasWiFi;
        this.hasAC = hasAC;
        this.hasHeating = hasHeating;
        this.hasKitchen = hasKitchen;
        this.hasTV = hasTV;
        this.hasParking = hasParking;
        this.hasElevator = hasElevator;
        this.reviews = reviews;
        this.host = host;
        this.rating = rating;
    }

    public Rental(NewRentalDTO dto, User user){
        this.title = dto.getTitle();
        this.basePrice = dto.getBasePrice();
        this.chargePerPerson = dto.getChargePerPerson();

        this.availableDates = new ArrayList<>();
        if (!dto.getAvailableDates().isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (String date : dto.getAvailableDates()) {
                LocalDate localDate = LocalDate.parse(date, formatter);
                this.availableDates.add(localDate);
            }
        }
        this.maxGuests = dto.getMaxGuests();
        this.beds = dto.getBeds();
        this.bedrooms = dto.getBedrooms();
        this.bathrooms = dto.getBathrooms();
        this.type = dto.getType();
        this.hasLivingRoom = dto.getHasLivingRoom();
        this.surfaceArea = dto.getSurfaceArea();
        this.description = dto.getDescription();
        this.allowSmoking = dto.getAllowSmoking();
        this.allowPets = dto.getAllowPets();
        this.allowEvents = dto.getAllowEvents();
        this.minDays = dto.getMinDays();
        this.address = dto.getAddress();
        this.publicTransport = dto.getPublicTransport();
        this.hasWiFi = dto.getHasWiFi();
        this.hasAC = dto.getHasAC();
        this.hasHeating = dto.getHasHeating();
        this.hasKitchen = dto.getHasKitchen();
        this.hasTV = dto.getHasTV();
        this.hasParking = dto.getHasParking();
        this.hasElevator = dto.getHasElevator();
        this.host = user;
        this.reviews = new HashSet<>();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public RentalType getType() {
        return type;
    }

    public void setType(RentalType type) {
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

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
