package com.rbbnbb.TediTry1.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; //ROLE_ADMIN || ROLE_HOST || ROLE_TENANT

    public Role(){}
    public Role(String role){
        this.name=role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
