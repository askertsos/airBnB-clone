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

import java.util.*;

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

    @Autowired
    private RecommendedRentalsRepository recommendedRentalsRepository;

    @PersistenceContext
    private EntityManager entityManager;


    //--------------------------------------------------------------------------------------------
    //-----------------                    ALL USERS                    --------------------------
    //--------------------------------------------------------------------------------------------

    @GetMapping("/auth")

    public ResponseEntity<?> authenticateJWT(@RequestHeader("Authorization") String jwt){
        User user = userService.getUserByJwt(jwt).get();
        return ResponseEntity.ok().body(user.getAuthorities());
    }


    @GetMapping("/profile")
    @Transactional

    public ResponseEntity<?> viewProfileUser(@RequestHeader("Authorization") String jwt){
        Optional<User> optionalUser = userService.getUserByJwt(jwt);
        User user = optionalUser.get();

        Map<String, Object> responseBody = new HashMap<String, Object>();
        responseBody.put("User", user);
        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping("/profile/update")
    @Transactional
    public ResponseEntity<?> updateProfileUser(@RequestBody UserDTO userDTO, @RequestHeader("Authorization") String jwt){
        Optional<User> optionalUser = userService.getUserByJwt(jwt);
        User user = optionalUser.get();
        userService.updateUser(user,userDTO);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/hosts/{id}")
    public ResponseEntity<?> getHostInfo(@PathVariable("id") Long id) {
        User host = userService.assertUserHasAuthority(id, "HOST");
        if (Objects.isNull(host)) return ResponseEntity.badRequest().build();

        //Empty set is still a valid output
        Set<Rental> hostRentals = rentalRepository.findByHost(host);

        HostInfoDTO dto = new HostInfoDTO(host);
        dto.setHostRentals(hostRentals);

        return ResponseEntity.ok().body(dto);
    }

    //--------------------------------------------------------------------------------------------
    //-----------------                     TENANTS                     --------------------------
    //--------------------------------------------------------------------------------------------

    @GetMapping("/recommended_rentals")
    public ResponseEntity<?> getRecommendedRentals(@RequestHeader("Authorization") String jwt){
        User tenant = userService.getUserByJwt(jwt).get();
        Optional<RecommendedRentals> optional = recommendedRentalsRepository.findByTenant(tenant);
        if (optional.isEmpty()) return ResponseEntity.badRequest().build();
        RecommendedRentals recommendedRentals = optional.get();

        List<Rental> rentalList = new ArrayList<>(recommendedRentals.getRentals());

        return ResponseEntity.ok().body(rentalList);
    }
}