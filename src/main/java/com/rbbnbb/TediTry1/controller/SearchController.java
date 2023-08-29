package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.domain.Rental;
import com.rbbnbb.TediTry1.dto.SearchRequestDTO;
import com.rbbnbb.TediTry1.repository.RentalRepository;
import com.rbbnbb.TediTry1.services.SpecificationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@CrossOrigin("*")
public class SearchController {

    @Autowired
    SpecificationService<Rental> rentalSpecificationService;
    @Autowired
    RentalRepository rentalRepository;

    @GetMapping("/")
    public ResponseEntity<?> searchRentals(@RequestBody SearchRequestDTO dto){

        Specification<Rental> searchSpecification = rentalSpecificationService.getSearchSpecification(dto);

        List<Rental> rentals = rentalRepository.findAll(searchSpecification);
        return ResponseEntity.ok().body(rentals);

    }
}
