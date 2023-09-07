package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.domain.Rental;
import com.rbbnbb.TediTry1.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private RentalRepository rentalRepository;
    @GetMapping("/")
    public String adminControllerFunc(){
        return "is Admin";
    }

    @GetMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getJson(){
        List<Rental> allRentals = rentalRepository.findAll();

        return ResponseEntity.ok(allRentals);
    }

    @GetMapping(value = "/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> getXml(){
        List<Rental> allRentals = rentalRepository.findAll();

        return ResponseEntity.ok(allRentals);
    }
}
