package com.WebAppTechnologies.AirBnbClone.repository;

import com.WebAppTechnologies.AirBnbClone.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo,Long> {
    Optional<Photo> findByFilePath(String filePath);

    Optional<Photo> findByName(String name);
}
