package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.domain.Rental;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.dto.NewRentalDTO;
import com.rbbnbb.TediTry1.repository.RentalRepository;
import com.rbbnbb.TediTry1.repository.ReviewRepository;
import com.rbbnbb.TediTry1.repository.UserRepository;
import com.rbbnbb.TediTry1.services.AuthenticationService;
import com.rbbnbb.TediTry1.services.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/host")
public class HostController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private RentalService rentalService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    JwtDecoder jwtDecoder;

    @PostMapping("/new_rental")
    @Transactional
    public ResponseEntity<?> submitNewRental(@RequestBody NewRentalDTO body, @RequestHeader("Authorization") String jwt){

        Optional<User> optionalUser = authenticationService.getUserByJwt(jwt);

        if (optionalUser.isEmpty()) return ResponseEntity.status(404).build();

        User user = optionalUser.get();
        Rental newRental = new Rental(0L,body,user);
        rentalRepository.save(newRental);

        return ResponseEntity.ok().body(newRental);
    }

    @PostMapping("/{rental_id}/info")
    @Transactional
    public ResponseEntity<?> updateRental(@PathVariable("rental_id") String id, @RequestHeader("Authorization") String jwt, @RequestBody NewRentalDTO dto){
        Optional<Rental> optionalRental = rentalRepository.findById(Long.parseLong(id));
        if(optionalRental.isEmpty()) return ResponseEntity.badRequest().build();
        Rental rental = optionalRental.get();


        Optional<User> optionalHost = authenticationService.getUserByJwt(jwt);
        if (optionalHost.isEmpty()) return ResponseEntity.badRequest().build();

        User host = optionalHost.get();
        if (!rental.getHost().equals(host)) return ResponseEntity.badRequest().build();

        try {
            rentalService.updateRental(rental.getId(), dto);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(rental);
    }
}
