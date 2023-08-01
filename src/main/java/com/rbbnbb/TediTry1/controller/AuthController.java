package com.rbbnbb.TediTry1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    // handler method to handle home page request
    @GetMapping("/index")
    public String home(){
        return "index";
    }
}
