package com.rbbnbb.TediTry1.repository;

import com.rbbnbb.TediTry1.domain.Search;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends JpaRepository<Search,Long> {
}
