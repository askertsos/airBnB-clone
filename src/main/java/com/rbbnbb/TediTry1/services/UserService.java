package com.rbbnbb.TediTry1.services;

import com.rbbnbb.TediTry1.domain.Role;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.dto.UserDTO;
import com.rbbnbb.TediTry1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    public void updateUser(User user, UserDTO userDTO){
        if (Objects.nonNull(userDTO.getUsername())) user.setUsername(userDTO.getUsername());
        if (Objects.nonNull(userDTO.getPassword())) user.setPassword(encoder.encode(userDTO.getPassword()));
        if (Objects.nonNull(userDTO.getFirst_name())) user.setFirst_name(userDTO.getFirst_name());
        if (Objects.nonNull(userDTO.getLast_name())) user.setLast_name(userDTO.getLast_name());
        if (Objects.nonNull(userDTO.getEmail())) user.setEmail(userDTO.getEmail());
        if (Objects.nonNull(userDTO.getPhoneNumber())) user.setPhoneNumber(userDTO.getPhoneNumber());

        //Roles cannot be changed

        userRepository.save(user);

    }

    public Boolean isTenant(User user){
        Collection<?> grantedAuthorities = user.getAuthorities();
        for (var grantedAuthority: grantedAuthorities) {
            Role role = (Role) grantedAuthority;
            if (role.getAuthority().equals("TENANT")) return true;
        }
        return false;
    }

    public Boolean isHost(User user){
        Collection<?> grantedAuthorities = user.getAuthorities();
        for (var grantedAuthority: grantedAuthorities) {
            Role role = (Role) grantedAuthority;
            if (role.getAuthority().equals("HOST")) return true;
        }
        return false;
    }

}
