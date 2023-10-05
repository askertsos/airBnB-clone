package com.WebAppTechnologies.AirBnbClone;

import com.WebAppTechnologies.AirBnbClone.domain.Role;
import com.WebAppTechnologies.AirBnbClone.domain.User;
import com.WebAppTechnologies.AirBnbClone.repository.RoleRepository;
import com.WebAppTechnologies.AirBnbClone.repository.UserRepository;
import com.WebAppTechnologies.AirBnbClone.services.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;


@SpringBootApplication
@EnableScheduling
public class AirBnbCloneApplication {
	@Autowired
	private RecommendationService recommendationService;

	public static void main(String[] args) {
		SpringApplication.run(AirBnbCloneApplication.class, args);
	}
	@Bean
	CommandLineRunner run(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder){
		return args ->{
			if (roleRepository.findByAuthority("ADMIN").isPresent()) return;
			Role adminRole = roleRepository.save(new Role("ADMIN"));
			roleRepository.save(new Role("TENANT"));
			roleRepository.save(new Role("HOST"));

			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);

			User admin = new User(1L,"admin",passwordEncoder.encode("password"),roles);
			userRepository.save(admin);
		};
	}

	//Schedule this for once per day, as its complexity is ~= O(n*m*r) or O(n*m*s) where n=users, m=rentals, r=reviews, s=searches
	@Scheduled(fixedDelay = 1000L * 60 * 60 * 24, initialDelay = 1000L * 10)
	public void updateRecommendedRentals(){
		System.out.println("Commencing the recommendation service");
		LocalDateTime before = LocalDateTime.now();
		recommendationService.updateRecommendationTable();
		LocalDateTime after = LocalDateTime.now();
		System.out.println("Recommendation service completed after " + before.until(after, ChronoUnit.SECONDS) + " seconds");
	}
}
