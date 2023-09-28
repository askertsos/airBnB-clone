package com.WebAppTechnologies.AirBnbClone.repository;

import com.WebAppTechnologies.AirBnbClone.domain.User;
import com.WebAppTechnologies.AirBnbClone.domain.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory,Long> {
    Optional<SearchHistory> findByUser(User user);
}
