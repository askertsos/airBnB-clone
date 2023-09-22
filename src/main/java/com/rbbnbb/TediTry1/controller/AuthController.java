package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.domain.Rental;
import com.rbbnbb.TediTry1.domain.Review;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.dto.LoginDTO;
import com.rbbnbb.TediTry1.dto.MassReviewDTO;
import com.rbbnbb.TediTry1.dto.RegisterDTO;
import com.rbbnbb.TediTry1.repository.RentalRepository;
import com.rbbnbb.TediTry1.repository.ReviewRepository;
import com.rbbnbb.TediTry1.repository.UserRepository;
import com.rbbnbb.TediTry1.services.AuthenticationService;
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
