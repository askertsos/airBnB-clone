package com.rbbnbb.TediTry1.domain;

import com.rbbnbb.TediTry1.dto.NewRentalDTO;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;
@Entity
public class Rental {

    public enum RentalType {privateRoom, publicRoom, house};
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Double price;

    //Space
    private Integer beds;
    private Integer bedrooms;
    private Integer bathrooms;
    private RentalType type;
    private Boolean hasLivingRoom;
    private Double surfaceArea;

    //Descriprtion
    private String description;

    //Rules
    private Boolean allowSmoking;
    private Boolean allowPets;
    private Boolean allowEvents;
    private Integer minDays;


    //Location
    private String address;
    //map
    private String neighbourhood;
    private String publicTransport;

    //Photos
    //private Set<Photo> photos;

    @ManyToOne(fetch = FetchType.EAGER)
    private User host;

    public Rental(Long id, String title, Double price, Integer beds, Integer bedrooms, Integer bathrooms, RentalType type, Boolean hasLivingRoom, Double surfaceArea, String description, Boolean allowSmoking, Boolean allowPets, Boolean allowEvents, Integer minDays, String address, String neighbourhood, String publicTransport) {
        this.id = id;
        this.title = title;
        this.price = price;
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
        this.neighbourhood = neighbourhood;
        this.publicTransport = publicTransport;
    }

    public Rental(Long id, NewRentalDTO dto){
        this.title = dto.getTitle();
        this.price = dto.getPrice();
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
        this.neighbourhood = dto.getNeighbourhood();
        this.publicTransport = dto.getPublicTransport();

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public String getPublicTransport() {
        return publicTransport;
    }

    public void setPublicTransport(String publicTransport) {
        this.publicTransport = publicTransport;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }
}
