package com.rbbnbb.TediTry1;

import com.rbbnbb.TediTry1.domain.Role;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.repository.RoleRepository;
import com.rbbnbb.TediTry1.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class TediTry1Application {

	public static void main(String[] args) {
		SpringApplication.run(TediTry1Application.class, args);
	}

	@Bean
	CommandLineRunner run(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder){
		return args ->{
			if (roleRepository.findByAuthority("ROLE_ADMIN").isPresent()) return;
			Role adminRole = roleRepository.save(new Role("ROLE_ADMIN"));
			roleRepository.save(new Role("ROLE_TENANT"));
			roleRepository.save(new Role("ROLE_HOST"));

			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);

			User admin = new User(1L,"admin",passwordEncoder.encode("password"),roles);
			userRepository.save(admin);
		};

	}
}
