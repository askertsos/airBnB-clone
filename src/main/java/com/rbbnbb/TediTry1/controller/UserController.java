package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.domain.Address;
import com.rbbnbb.TediTry1.domain.Rental;
import com.rbbnbb.TediTry1.domain.Review;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.dto.NewRentalDTO;
import com.rbbnbb.TediTry1.dto.ReviewDTO;
import com.rbbnbb.TediTry1.repository.RentalRepository;
import com.rbbnbb.TediTry1.repository.ReviewRepository;
import com.rbbnbb.TediTry1.repository.UserRepository;

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
    UserRepository userRepository;

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    JwtDecoder jwtDecoder;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/auth")
    public ResponseEntity<?> authenticateJWT(@RequestHeader("Authorization") String jwt){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/new_rental")
    @Transactional
    public ResponseEntity<?> submitNewRental(@RequestBody NewRentalDTO body,@RequestHeader("Authorization") String jwt){

        //Find user by decoding jwt
        String pureJwt = jwt;
        pureJwt = pureJwt.replaceFirst("Bearer ", "");
        Jwt decodedJWT = jwtDecoder.decode(pureJwt);
        String username = decodedJWT.getSubject();
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (!optionalUser.isPresent()) {
            System.out.println("User not found");
            return ResponseEntity.status(404).build();
        }
//
//        //Save the new Rental object
//        User user = optionalUser.get();
//        simpleJpaRepository.save(new Rental(0L,body,user));

        User user = optionalUser.get();
        rentalRepository.save(new Rental(0L,body,user));

        return ResponseEntity.ok().body(body);
    }

    @PostMapping("/review")
    @Transactional
    public ResponseEntity<?> submitReview(@RequestHeader("Authorization") String jwt, @RequestBody ReviewDTO body){
        String pureJwt = jwt;
        pureJwt = pureJwt.replaceFirst("Bearer ", "");
        Jwt decodedJWT = jwtDecoder.decode(pureJwt);
        String username = decodedJWT.getSubject();
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (!optionalUser.isPresent()) {
            System.out.println("User not found");
            return ResponseEntity.status(404).build();
        }
        User user = optionalUser.get();

//        SimpleJpaRepository<Rental, Long> rentalRepo;
//        rentalRepo = new SimpleJpaRepository<Rental, Long>(Rental.class,entityManager);
//
//        Optional<Rental> optionalRental = rentalRepo.findById(body.getRentalId());
//        if (!optionalRental.isPresent()){
//            System.out.println("Invalid rental_id");
//            return ResponseEntity.status(404).build();
//        }
//        Rental rental = optionalRental.get();
//
//        Review review = new Review(0L,body,user,rental);
//        SimpleJpaRepository<Review, Long> reviewRepo;
//        reviewRepo = new SimpleJpaRepository<Review, Long>(Review.class,entityManager);
//
//        reviewRepo.save(review);

        Optional<Rental> optionalRental = rentalRepository.findById(body.getRentalId());
        if (!optionalRental.isPresent()){
            System.out.println("invalid rental id");
            return ResponseEntity.status(404).build();
        }
        Rental rental = optionalRental.get();

        Review review = new Review(0L,body,user,rental);
        reviewRepository.save(review);
        rental.addReview(review);


//        reviewRepository.save(review);
//
//        Optional<Rental> optionalRental = rentalRepository.findById(body.getRentalId());
//        if (!optionalRental.isPresent()){
//            System.out.println("Rental not found");
//            return ResponseEntity.status(404).build();
//        }


        return ResponseEntity.ok().body(body);
    }

}