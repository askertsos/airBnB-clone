package com.rbbnbb.TediTry1.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@CrossOrigin("localhost:8080")
public class AdminController {

    @GetMapping("/")
    public String adminControllerFunc(){
        return "is Admin";
    }
}
