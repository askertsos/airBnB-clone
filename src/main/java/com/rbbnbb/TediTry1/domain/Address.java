package com.rbbnbb.TediTry1.domain;

import jakarta.persistence.*;

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String country;
    private String city;
    private String neighbourhood;
    private String street;
    private Integer number;

    private Integer floorNo;

    public Address(){}

    public Address(Address address){
        this.id = address.getId();
        this.country = address.getCountry();
        this.city = address.getCity();
        this.neighbourhood = address.getNeighbourhood();
        this.street = address.getStreet();
        this.number = address.getNumber();
        this.floorNo = address.getFloorNo();
    }
    public Address(Long id, String country, String city, String neighbourhood, String street, Integer number, Integer floorNo) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.neighbourhood = neighbourhood;
        this.street = street;
        this.number = number;
        this.floorNo = floorNo;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(Integer floorNo) {
        this.floorNo = floorNo;
    }
}
