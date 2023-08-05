package com.rbbnbb.TediTry1.services;


import com.rbbnbb.TediTry1.domain.Role;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.dto.LoginResponseDTO;
import com.rbbnbb.TediTry1.repository.RoleRepository;
import com.rbbnbb.TediTry1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.HashSet;
import java.util.NoSuchElementException;
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
        Role tenantRole = roleRepository.findByAuthority("ROLE_TENANT").get();

        Set<Role> authorities = new HashSet<>();
        authorities.add(tenantRole);

        return userRepository.save(new User(username,encodedPassword,authorities));
    }

    public LoginResponseDTO loginUser(String username, String password){
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String token = tokenService.generateJWT(auth);
            try {
                return new LoginResponseDTO(userRepository.findByUsername(username).get(), token);
            } catch (NoSuchElementException n){
                System.out.println("Not found");
                throw new AuthenticationException();
            }
        }catch(AuthenticationException e){
            return new LoginResponseDTO(null, "");
        }
    }

}
