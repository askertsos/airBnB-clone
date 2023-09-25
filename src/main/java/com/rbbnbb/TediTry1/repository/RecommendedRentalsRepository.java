package com.rbbnbb.TediTry1.repository;

import com.rbbnbb.TediTry1.domain.RecommendedRentals;
import com.rbbnbb.TediTry1.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecommendedRentalsRepository extends JpaRepository<RecommendedRentals,Long> {
    Optional<RecommendedRentals> findByTenant(User tenant);
}
