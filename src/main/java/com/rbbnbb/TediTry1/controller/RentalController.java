package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.domain.Booking;
import com.rbbnbb.TediTry1.domain.Rental;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.dto.BookingDTO;
import com.rbbnbb.TediTry1.repository.BookingRepository;
import com.rbbnbb.TediTry1.repository.RentalRepository;
import com.rbbnbb.TediTry1.repository.UserRepository;
import com.rbbnbb.TediTry1.services.AuthenticationService;
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
@RequestMapping("/rentals")
public class RentalController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private JwtDecoder jwtDecoder;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/{rental}")
    public ResponseEntity<?> rentalInfo(@PathVariable("rental") String rentalTitle){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{rental}/book")
    @Transactional
    public ResponseEntity<?> bookRental(@PathVariable("rental") String title, @RequestHeader("Authorization") String jwt, @RequestBody BookingDTO dto){

        //Find the rental
        Optional<Rental> optionalRental = rentalRepository.findByTitleIgnoreCase(title);
        if (optionalRental.isEmpty()) return ResponseEntity.status(404).build();
        Rental rental = optionalRental.get();



        Optional<User> optionalUser = authenticationService.getUserByJwt(jwt);
        if (optionalUser.isEmpty()) return ResponseEntity.status(404).build();
        User user = optionalUser.get();

        try {
            rental.removeAvailableDates(dto.getDates());
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }

        //Save the new booking
        Booking newBooking = new Booking(0L,user,rental,dto);
        bookingRepository.save(newBooking);

        return ResponseEntity.ok().body(newBooking);

    }

}
