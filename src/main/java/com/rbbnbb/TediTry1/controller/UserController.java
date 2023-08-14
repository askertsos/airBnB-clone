package com.rbbnbb.TediTry1.controller;


import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

//    @GetMapping("/")
//    public String helloUserController() {
//        return "User access level";
//    }

    @GetMapping("/")
    public ResponseEntity<?> authenticateJWT(@RequestHeader("Authorization") String jwt){
        System.out.println("Token is " + jwt);
        if (jwt.isBlank() || jwt.isEmpty() || jwt.equals("null") ) return ResponseEntity.status(401).build();
        return ResponseEntity.ok().build();
    }

}