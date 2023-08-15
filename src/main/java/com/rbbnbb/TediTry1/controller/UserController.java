package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.repository.UserRepository;
import com.rbbnbb.TediTry1.repository.UserRepository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

//    @GetMapping("/")
//    public String helloUserController() {
//        return "User access level";
//    }
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtDecoder jwtDecoder;

    @GetMapping("/auth")
    public ResponseEntity<?> authenticateJWT(@RequestHeader("Authorization") String jwt){

//        if (jwt.isBlank() || jwt.isEmpty() || jwt.equals("null") ) return ResponseEntity.status(401).build();
//        Jwt decodedJWT = jwtDecoder.decode(jwt);
//        String username = decodedJWT.getSubject();
//        if (userRepository.findByUsername(username).isPresent())
            return ResponseEntity.ok().build();
//        return ResponseEntity.status(401).build();

    }

}