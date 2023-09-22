package com.rbbnbb.TediTry1.services;


import com.rbbnbb.TediTry1.domain.Photo;
import com.rbbnbb.TediTry1.domain.Role;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.dto.RegisterDTO;
import com.rbbnbb.TediTry1.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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

    public ResponseEntity<?> registerUser(RegisterDTO body){
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

        if (body.getRoles().equals("both")){
            authorities.add(tenantRole);
            authorities.add(hostRole);
        }
        else if(body.getRoles().equals("tenant")){
            authorities.add(tenantRole);
        }
        else{
            authorities.add(hostRole);
        }

        //Create new user entity and save it
        User newUser = new User(username, encodedPassword, first_name, last_name, email, phoneNumber, authorities);
        userRepository.save(newUser);


        //If the user sent a profile picture name, create new Photo instance and add it to the user
        if (Objects.nonNull(body.getPictureName()) && !(body.getPictureName().isEmpty())) {

            try {
                //Create profile picture path based on the user's generated id
                String fullPath = "src/main/resources/ProfilePictures/" + newUser.getId().toString() + "/" + body.getPictureName();
                Photo profilePicture = new Photo(fullPath);
                photoRepository.save(profilePicture);
                newUser.setProfilePicture(profilePicture);
                userRepository.save(newUser);
                }
            catch (Exception e){
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.ok().build();

    }

    public ResponseEntity<?> loginUser(String username, String password){
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            String token = tokenService.generateJWT(auth);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add(HttpHeaders.AUTHORIZATION,token);
            responseHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,HttpHeaders.AUTHORIZATION);
            recommendationService.recommend((User)auth.getPrincipal());
            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body((User)auth.getPrincipal());
        }catch(AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}
