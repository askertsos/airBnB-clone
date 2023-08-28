package com.rbbnbb.TediTry1.services;


import com.rbbnbb.TediTry1.domain.Review;
import com.rbbnbb.TediTry1.domain.Role;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.dto.LoginResponseDTO;
import com.rbbnbb.TediTry1.dto.RegisterDTO;
import com.rbbnbb.TediTry1.repository.RentalRepository;
import com.rbbnbb.TediTry1.repository.ReviewRepository;
import com.rbbnbb.TediTry1.repository.RoleRepository;
import com.rbbnbb.TediTry1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

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
        System.out.println(body.getRoles());
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

        try {
            userRepository.save(new User(0L, username, encodedPassword, first_name, last_name, email, phoneNumber, authorities));
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<?> loginUser(String username, String password){
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            String token = tokenService.generateJWT(auth);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add(HttpHeaders.AUTHORIZATION,token);
            responseHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,HttpHeaders.AUTHORIZATION);
            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body((User)auth.getPrincipal());
//                    .body(reviewRepository.getHostRatings((User)auth.getPrincipal()));
//                    .body(rentalRepository.findByHost((User)auth.getPrincipal()));
        }catch(AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
