package com.rbbnbb.TediTry1.services;

import com.rbbnbb.TediTry1.domain.Review;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.repository.ReviewRepository;
import com.rbbnbb.TediTry1.repository.RoleRepository;
import com.rbbnbb.TediTry1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.List;

@Service
public class RecommendationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private static final Double alpha = 0.0002;
    private static final Double beta = 0.02;
    private static final Double epsilon = 0.001;
    private static final Integer steps = 5000;

    public void recommend(User tenant){
        Array[][] R;
        //Get all tenants
        List<User> USERS = userRepository.findByRole(roleRepository.findByAuthority("TENANT").get());
        List<Review> REVIEWS = reviewRepository.findAll();
    }

}
