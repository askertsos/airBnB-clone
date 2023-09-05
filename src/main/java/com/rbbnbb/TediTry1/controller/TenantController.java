package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.domain.Rental;
import com.rbbnbb.TediTry1.domain.Review;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.dto.ReviewDTO;
import com.rbbnbb.TediTry1.repository.RentalRepository;
import com.rbbnbb.TediTry1.repository.ReviewRepository;
import com.rbbnbb.TediTry1.repository.UserRepository;
import com.rbbnbb.TediTry1.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/tenant")
public class TenantController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    JwtDecoder jwtDecoder;
    @PostMapping("/review")
    @Transactional
    public ResponseEntity<?> submitReview(@RequestHeader("Authorization") String jwt, @RequestBody ReviewDTO body){
        Optional<User> optionalUser = authenticationService.getUserByJwt(jwt);

        if (optionalUser.isEmpty()) return ResponseEntity.status(404).build();
        User user = optionalUser.get();

        Optional<Rental> optionalRental = rentalRepository.findById(body.getRentalId());
        if (optionalRental.isEmpty()) return ResponseEntity.status(404).build();
        Rental rental = optionalRental.get();

        Review review = new Review(0L,body,user,rental);
        reviewRepository.save(review);
        rental.addReview(review);

        return ResponseEntity.ok().body(review);
    }
}
