package com.WebAppTechnologies.AirBnbClone.dto;

public class UserDetailsDTO {

    Long id;

    public UserDetailsDTO() {}

    public UserDetailsDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
