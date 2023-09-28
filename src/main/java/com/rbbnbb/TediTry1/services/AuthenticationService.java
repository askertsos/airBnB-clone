package com.rbbnbb.TediTry1.services;


import com.rbbnbb.TediTry1.domain.Photo;
import com.rbbnbb.TediTry1.domain.Role;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.dto.RegisterDTO;
import com.rbbnbb.TediTry1.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.*;

@Service
@Transactional
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RecommendationService recommendationService;

    public ResponseEntity<?> registerUser(RegisterDTO body) {
        String username = body.getUsername();
        String encodedPassword = passwordEncoder.encode(body.getPassword());
        String first_name = body.getFirst_name();
        String last_name = body.getLast_name();
        String email = body.getEmail();
        String phoneNumber = body.getPhoneNumber();

        //If roles field is BOTH, add TENANT and HOST as new user's roles. If it is TENANT, add only TENANT. If it is HOST, add only HOST.
        Role tenantRole = roleRepository.findByAuthority("TENANT").get();
        Role hostRole = roleRepository.findByAuthority("HOST").get();
        Set<Role> authorities = new HashSet<>();

        if (body.getRoles().equals("both")) {
            authorities.add(tenantRole);
            authorities.add(hostRole);
        } else if (body.getRoles().equals("tenant")) {
            authorities.add(tenantRole);
        } else {
            authorities.add(hostRole);
        }

        //Create new user entity and save it
        User newUser = new User(username, encodedPassword, first_name, last_name, email, phoneNumber, authorities);
        userRepository.save(newUser);

        //Try to create directory for the profile photo. If it fails, ignore it until the user tries to upload a photo.
        //Create it then
        File userDirectory = new File(newUser.getPhotoDirectory());
        try{
            userDirectory.mkdir();
        }
        catch(Exception e){
            //do nothing
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> loginUser(String username, String password){
        try {
            // Generate jwt if password and username combination is correct
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            String token = tokenService.generateJWT(auth);

            // Create response body
            Map<String, Object> body = new HashMap<String, Object>();
            body.put("jwt", token);
            body.put("isAuthenticatedHost", userRepository.findByUsername(username).get().getIsAuthenticatedHost() ? "true" : "false");
            body.put("isTenant", userRepository.findByUsername(username).get().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("TENANT")) ? "true" : "false");
            body.put("isHost", userRepository.findByUsername(username).get().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("HOST")) ? "true" : "false");
            body.put("isAdmin", userRepository.findByUsername(username).get().getAuthorities().stream().allMatch(a -> a.getAuthority().equals("ADMIN")) ? "true" : "false");

            return ResponseEntity.ok()
                                 .body(body);
        } catch(AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}
