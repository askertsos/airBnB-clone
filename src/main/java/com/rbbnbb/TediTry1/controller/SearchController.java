package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.domain.Rental;
import com.rbbnbb.TediTry1.dto.PageRequestDTO;
import com.rbbnbb.TediTry1.dto.SearchRequestDTO;
import com.rbbnbb.TediTry1.repository.RentalRepository;
import com.rbbnbb.TediTry1.services.SpecificationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/search")
@CrossOrigin("*")
public class SearchController {

    @Autowired
    private SpecificationService<Rental> rentalSpecificationService;
    @Autowired
    private RentalRepository rentalRepository;

    @PostMapping("/")
    public ResponseEntity<?> searchRentals(@RequestBody SearchRequestDTO dto){

        Specification<Rental> searchSpecification = rentalSpecificationService.getSearchSpecification(dto);

        Pageable pageable = new PageRequestDTO().getPageable(dto.getPageRequestDTO());

        Page<Rental> rentalPage = rentalRepository.findAll(searchSpecification,pageable);


        return ResponseEntity.ok().body(rentalPage);

    }

    @GetMapping("/{rentalId}/details")
    public ResponseEntity<?> rentalInfo(@PathVariable("rentalId") Long rentalId){
        Rental rental = rentalRepository.findById(rentalId).get();
        return ResponseEntity.ok().body(rental);
    }

}
