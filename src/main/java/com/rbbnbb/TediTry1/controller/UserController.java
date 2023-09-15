package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.dto.NewRentalDTO;
import com.rbbnbb.TediTry1.dto.ReviewDTO;
import com.rbbnbb.TediTry1.domain.*;
import com.rbbnbb.TediTry1.dto.HostInfoDTO;
import com.rbbnbb.TediTry1.dto.UserDTO;
import com.rbbnbb.TediTry1.repository.*;

import com.rbbnbb.TediTry1.services.AuthenticationService;
import com.rbbnbb.TediTry1.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.ResponseEntity;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
    private RoleRepository roleRepository;
    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MessageHistoryRepository messageHistoryRepository;

    @PersistenceContext
    private EntityManager entityManager;



    //--------------------------------------------------------------------------------------------
    //-----------------                    ALL USERS                    --------------------------
    //--------------------------------------------------------------------------------------------

    @GetMapping("/auth")
    public ResponseEntity<?> authenticateJWT(@RequestHeader("Authorization") String jwt){
        return ResponseEntity.ok().build();
    }


    @PostMapping("/profile")
    @Transactional
    public ResponseEntity<?> updateUserInfo(@RequestHeader("Authorization") String jwt, @RequestBody UserDTO userDTO){

        Optional<User> optionalUser = userService.getUserByJwt(jwt);
        if (optionalUser.isEmpty()) return ResponseEntity.badRequest().build();
        User user = optionalUser.get();

        userService.updateUser(user,userDTO);
        User responseBody = userService.getUserByJwt(jwt).get();

        return ResponseEntity.ok().body(responseBody);
    }

    //        SimpleJpaRepository<Review, Long> reviewRepo;
    //        reviewRepo = new SimpleJpaRepository<Review, Long>(Review.class,entityManager);


    @GetMapping("/hosts/{id}")
    public ResponseEntity<?> getHostInfo(@PathVariable("id") Long id){
        User host = userService.assertUserHasAuthority(id,"HOST");
        if (Objects.isNull(host)) return ResponseEntity.badRequest().build();

        //Empty set is still a valid output
        Set<Rental> hostRentals = rentalRepository.findByHost(host);

        HostInfoDTO dto = new HostInfoDTO(host);
        dto.setHostRentals(hostRentals);

        return ResponseEntity.ok().body(dto);

    }

}