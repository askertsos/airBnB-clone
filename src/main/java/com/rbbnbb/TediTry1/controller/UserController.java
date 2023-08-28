package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.domain.Rental;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.dto.NewRentalDTO;
import com.rbbnbb.TediTry1.repository.UserRepository;
import com.rbbnbb.TediTry1.repository.UserRepository.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpHeaders;
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
        Optional<User> user = userRepository.findByUsername(username);

        if (!user.isPresent()) {
            System.out.println("User not found");
            return ResponseEntity.status(404).build();
        }

        //Create new simple jpa repository to save the new entity
        SimpleJpaRepository<Rental, Long> simpleJpaRepository;
        simpleJpaRepository = new SimpleJpaRepository<Rental, Long>(Rental.class,entityManager);

        //Save the new Rental object
        simpleJpaRepository.save(new Rental(0L,body));

        return ResponseEntity.ok().body(body);
    }

}