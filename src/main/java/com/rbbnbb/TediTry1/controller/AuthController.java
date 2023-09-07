package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.dto.LoginDTO;
import com.rbbnbb.TediTry1.dto.RegisterDTO;
import com.rbbnbb.TediTry1.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO body){
        return authService.registerUser(body);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO body){
        return authService.loginUser(body.getUsername(), body.getPassword());
    }
    @GetMapping("/index")
    public String home(){
        return "Registration and Login System";
    }
}
