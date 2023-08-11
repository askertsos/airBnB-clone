package com.rbbnbb.TediTry1.services;


import com.rbbnbb.TediTry1.domain.Role;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.dto.LoginResponseDTO;
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

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    public User registerUser(String username, String password){
        String encodedPassword = passwordEncoder.encode(password);
        Role tenantRole = roleRepository.findByAuthority("TENANT").get();

        Set<Role> authorities = new HashSet<>();
        authorities.add(tenantRole);

        return userRepository.save(new User(0L,username,encodedPassword,authorities));
    }

    public ResponseEntity<?> loginUser(String username, String password){
        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            String token = tokenService.generateJWT(auth);
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .body(auth.getPrincipal());
//            return new LoginResponseDTO(userRepository.findByUsername(username).get(), token);
        }catch(AuthenticationException e) {
            System.out.println("AUTHENTICATION EXCEPTION");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//            return new LoginResponseDTO(null, "");
        }
    }

}
