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
    public ResponseEntity authenticateJWT(@RequestHeader String Authorization){
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,HttpHeaders.AUTHORIZATION);
        if (Authorization.isBlank() || Authorization.isEmpty()) return ResponseEntity.status(401).build();
        return ResponseEntity.ok().headers(responseHeaders).build();
    }

}