package com.WebAppTechnologies.AirBnbClone.repository;

import com.WebAppTechnologies.AirBnbClone.domain.Rental;
import com.WebAppTechnologies.AirBnbClone.domain.Review;
import com.WebAppTechnologies.AirBnbClone.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    @Query("SELECT avg(r1.stars) FROM Review r1 JOIN Rental r2 ON r1.rental=r2 WHERE r2.host = ?1")
    Double getHostRatings(User host);

    Collection<Review> findByReviewer(User reviewer);
    List<Review> findByReviewerAndRental(User reviewer, Rental rental);

    Long countByRental(Rental rental);
}
