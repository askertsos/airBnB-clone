package com.WebAppTechnologies.AirBnbClone.repository;

import com.WebAppTechnologies.AirBnbClone.domain.User;
import com.WebAppTechnologies.AirBnbClone.domain.RecommendedRentals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecommendedRentalsRepository extends JpaRepository<RecommendedRentals,Long> {
    Optional<RecommendedRentals> findByTenant(User tenant);
}
