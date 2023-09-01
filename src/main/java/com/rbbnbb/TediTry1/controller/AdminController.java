package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.dto.PageRequestDTO;
import com.rbbnbb.TediTry1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/auth")
    public ResponseEntity<?> authenticateJWT(@RequestHeader("Authorization") String jwt){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/list")
    public ResponseEntity<?> listUser(@RequestBody PageRequestDTO dto){
        Page<User> userPaginatedList = userRepository.findAll(dto.getPageable(dto));
        Map<String, Object> ResponseBody = new HashMap<String, Object>();
        ResponseBody.put("Users", userPaginatedList);
        return ResponseEntity.ok().body(ResponseBody);
    }

}
