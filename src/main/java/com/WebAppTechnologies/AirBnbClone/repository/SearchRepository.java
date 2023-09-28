package com.WebAppTechnologies.AirBnbClone.repository;

import com.WebAppTechnologies.AirBnbClone.domain.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends JpaRepository<Search,Long> {
}
