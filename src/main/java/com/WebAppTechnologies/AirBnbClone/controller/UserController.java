package com.WebAppTechnologies.AirBnbClone.controller;

import com.WebAppTechnologies.AirBnbClone.domain.Photo;
import com.WebAppTechnologies.AirBnbClone.domain.RecommendedRentals;
import com.WebAppTechnologies.AirBnbClone.domain.Rental;
import com.WebAppTechnologies.AirBnbClone.domain.User;
import com.WebAppTechnologies.AirBnbClone.dto.UserDTO;
import com.WebAppTechnologies.AirBnbClone.repository.*;
import com.WebAppTechnologies.AirBnbClone.dto.HostInfoDTO;

import com.WebAppTechnologies.AirBnbClone.services.AuthenticationService;
import com.WebAppTechnologies.AirBnbClone.services.PhotoService;
import com.WebAppTechnologies.AirBnbClone.services.RecommendationService;
import com.WebAppTechnologies.AirBnbClone.services.UserService;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserService userService;
    @Autowired
    private PhotoService photoService;

    @Autowired
    private RecommendationService recommendationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private PhotoRepository photoRepository;

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


    @PostMapping("/update_profile_picture")
    @Transactional
    public ResponseEntity<?> uploadUserProfilePicture(@RequestHeader("Authorization") String jwt, @RequestParam("image") MultipartFile file){
        User user = userService.getUserByJwt(jwt).get();
        Photo newPhoto;

        //Check whether user directory was successfully created during registration.
        //if not, create it now
        String userPhotoDirectory = user.getPhotoDirectory();
        File userDirectory = new File(userPhotoDirectory);
        if (!userDirectory.exists() || !userDirectory.isDirectory()){
            if (!userDirectory.mkdir()) return ResponseEntity.internalServerError().build();
        }
        try {
            newPhoto = photoService.saveImage(file, userPhotoDirectory);
        }
        catch(IOException e){
            return ResponseEntity.internalServerError().build();
        }
        if (Objects.nonNull(newPhoto)) {
            Photo oldPhoto = user.getProfilePicture();
            if (Objects.nonNull(oldPhoto)) {
                photoRepository.deleteById(oldPhoto.getId());
                user.setProfilePicture(null);
            }
            user.setProfilePicture(newPhoto);
            userRepository.save(user);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile_picture")
    public ResponseEntity<?> getUserProfilePicture(@RequestHeader("Authorization") String jwt){
        User user = userService.getUserByJwt(jwt).get();

        byte[] imageData;
        Photo profilePicture = user.getProfilePicture();

        if (Objects.isNull(profilePicture)){
            Map<String, Object> responseBody = new HashMap<String, Object>();
            responseBody.put("Photo", null);
            return ResponseEntity.ok().body(responseBody);
        }

        Map<String, Object> responseBody = new HashMap<String, Object>();
        responseBody.put("Photo", profilePicture.getName());

        return ResponseEntity.ok().body(responseBody);
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