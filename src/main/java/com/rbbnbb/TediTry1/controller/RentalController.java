package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.domain.Booking;
import com.rbbnbb.TediTry1.domain.Rental;
import com.rbbnbb.TediTry1.domain.Review;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.dto.BookingDTO;
import com.rbbnbb.TediTry1.dto.ReviewDTO;
import com.rbbnbb.TediTry1.repository.BookingRepository;
import com.rbbnbb.TediTry1.repository.RentalRepository;
import com.rbbnbb.TediTry1.repository.ReviewRepository;
import com.rbbnbb.TediTry1.repository.UserRepository;
import com.rbbnbb.TediTry1.services.AuthenticationService;
import com.rbbnbb.TediTry1.services.RentalService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private JwtDecoder jwtDecoder;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/{rentalId}")
    public ResponseEntity<?> rentalInfo(@PathVariable("rentalId") Long rentalId){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{rentalId}/book")
    @Transactional
    public ResponseEntity<?> bookRental(@PathVariable("rentalId") Long rentalId, @RequestHeader("Authorization") String jwt, @RequestBody BookingDTO dto){

        Booking newBooking = rentalService.constructBooking(jwt,rentalId,dto);
        if (Objects.isNull(newBooking)) return ResponseEntity.badRequest().build();

        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) return ResponseEntity.badRequest().build();

        Rental rental = optionalRental.get();

        dto.setPrice(rental.getPrice(dto.getGuests(),dto.getDates().size()));

        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/{rentalId}/book/confirm")
    @Transactional
    public ResponseEntity<?> confirmBooking(@PathVariable("rentalId") Long rentalId, @RequestHeader("Authorization") String jwt, @RequestBody BookingDTO dto){

        Booking newBooking = rentalService.constructBooking(jwt,rentalId,dto);
        if (Objects.isNull(newBooking)) return ResponseEntity.badRequest().build();

        bookingRepository.save(newBooking);

        return ResponseEntity.ok().body(newBooking);
    }

    @PostMapping("/{rentalId}/review")
    @Transactional
    public ResponseEntity<?> submitReview(@PathVariable("rentalId") Long rentalId, @RequestHeader("Authorization") String jwt, @RequestBody ReviewDTO body){
        Optional<User> optionalUser = authenticationService.getUserByJwt(jwt);

        if (optionalUser.isEmpty()) return ResponseEntity.badRequest().build();
        User user = optionalUser.get();

        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) return ResponseEntity.badRequest().build();
        Rental rental = optionalRental.get();

        //Assert that this user has booked this rental before
        List<Booking> bookingList = bookingRepository.findByBookerAndRental(user,rental);
        if (bookingList.isEmpty()) return ResponseEntity.badRequest().build();

        Review review = new Review(0L,body,user,rental);
        reviewRepository.save(review);
        rental.addReview(review);

        return ResponseEntity.ok().body(review);
    }

}
