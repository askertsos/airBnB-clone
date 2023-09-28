package com.WebAppTechnologies.AirBnbClone.controller;

import com.WebAppTechnologies.AirBnbClone.domain.Rental;
import com.WebAppTechnologies.AirBnbClone.domain.Review;
import com.WebAppTechnologies.AirBnbClone.domain.User;
import com.WebAppTechnologies.AirBnbClone.dto.LoginDTO;
import com.WebAppTechnologies.AirBnbClone.dto.MassReviewDTO;
import com.WebAppTechnologies.AirBnbClone.dto.RegisterDTO;
import com.WebAppTechnologies.AirBnbClone.repository.RentalRepository;
import com.WebAppTechnologies.AirBnbClone.repository.ReviewRepository;
import com.WebAppTechnologies.AirBnbClone.repository.UserRepository;
import com.WebAppTechnologies.AirBnbClone.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Optional;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO body){
        return authService.registerUser(body);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO body){
        return authService.loginUser(body.getUsername(), body.getPassword());
    }
    @GetMapping("/index")
    public String home(){
        return "Registration and Login System";
    }

    @PostMapping("/mass_review")
    public ResponseEntity<?> massReview(@RequestBody MassReviewDTO dto){
        Optional<Rental> optionalRental = rentalRepository.findById(dto.getRentalId());
        if (optionalRental.isEmpty()) return ResponseEntity.badRequest().build();
        Rental rental = optionalRental.get();

        Optional<User> optionalUser = userRepository.findById(dto.getReviewerId());
        if (optionalUser.isEmpty()) return ResponseEntity.badRequest().build();
        User reviewer = optionalUser.get();

        //Assert that this user has booked this rental before
        //Comment this out for the time being while inserting the CSVs into the database
//        List<Booking> bookingList = bookingRepository.findByBookerAndRental(reviewer,rental);
//        if (bookingList.isEmpty()) return ResponseEntity.badRequest().build();

        Review review = new Review(reviewer,rental,dto.getStars());
        reviewRepository.save(review);
        rental.addReview(review);

        return ResponseEntity.ok().body(review);

    }
}
