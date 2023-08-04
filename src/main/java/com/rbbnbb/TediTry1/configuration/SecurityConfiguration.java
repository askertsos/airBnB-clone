package com.rbbnbb.TediTry1.configuration;

import jdk.jshell.spi.ExecutionControlProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService detailsService) {
        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
        daoAuthProvider.setUserDetailsService(detailsService);
        return new ProviderManager(daoAuthProvider);
    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> {
////                    auth.requestMatchers("/auth/**").permitAll();
////                    auth.requestMatchers("/view/**").permitAll();
////                    auth.requestMatchers("/admin/**").hasRole("ADMIN");
////                    auth.requestMatchers("/user/**").hasAnyRole("ADMIN", "USER");
//                    auth.anyRequest().permitAll();
//                });
//
////        http.oauth2ResourceServer()
////                .jwt()
////                .jwtAuthenticationConverter(jwtAuthenticationConverter());
////        http.sessionManagement(
////                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////        );
//
//        return http.build();
//    }
}

