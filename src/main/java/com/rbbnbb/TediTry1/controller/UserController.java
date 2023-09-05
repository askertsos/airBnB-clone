package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.domain.Address;
import com.rbbnbb.TediTry1.domain.Rental;
import com.rbbnbb.TediTry1.domain.Review;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.dto.NewRentalDTO;
import com.rbbnbb.TediTry1.dto.ReviewDTO;
import com.rbbnbb.TediTry1.dto.UserDTO;
import com.rbbnbb.TediTry1.repository.RentalRepository;
import com.rbbnbb.TediTry1.repository.ReviewRepository;
import com.rbbnbb.TediTry1.repository.UserRepository;

import com.rbbnbb.TediTry1.services.AuthenticationService;
import com.rbbnbb.TediTry1.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/auth")
    public ResponseEntity<?> authenticateJWT(@RequestHeader("Authorization") String jwt){
        return ResponseEntity.ok().build();
    }


    @PostMapping("/info")
    @Transactional
    public ResponseEntity<?> updateUserInfo(@RequestHeader("Authorization") String jwt, @RequestBody UserDTO userDTO){

        Optional<User> optionalUser = authenticationService.getUserByJwt(jwt);
        if (optionalUser.isEmpty()) return ResponseEntity.badRequest().build();
        User user = optionalUser.get();

        userService.updateUser(user,userDTO);

        return ResponseEntity.ok().body(user);
    }

    //        SimpleJpaRepository<Review, Long> reviewRepo;
    //        reviewRepo = new SimpleJpaRepository<Review, Long>(Review.class,entityManager);



}