package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.repository.RentalRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@CrossOrigin("*")
public class SearchController {


    @Autowired
    RentalRepository rentalRepository;

//    @GetMapping("/")
//    public ResponseEntity<?> searchRentals(){
//
//
//    }
}
