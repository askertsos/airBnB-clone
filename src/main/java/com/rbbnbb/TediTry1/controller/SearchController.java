package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.domain.Rental;
import com.rbbnbb.TediTry1.domain.Search;
import com.rbbnbb.TediTry1.domain.SearchHistory;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.dto.PageRequestDTO;
import com.rbbnbb.TediTry1.dto.SearchRequestDTO;
import com.rbbnbb.TediTry1.dto.SpecificationDTO;
import com.rbbnbb.TediTry1.repository.RentalRepository;
import com.rbbnbb.TediTry1.repository.SearchHistoryRepository;
import com.rbbnbb.TediTry1.repository.UserRepository;
import com.rbbnbb.TediTry1.services.SearchService;
import com.rbbnbb.TediTry1.services.SpecificationService;
import com.rbbnbb.TediTry1.services.UserService;
import jakarta.annotation.Nullable;
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
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/search")
@CrossOrigin("*")
public class SearchController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    @Autowired
    private SpecificationService<Rental> rentalSpecificationService;
    @Autowired
    private RentalRepository rentalRepository;


    @Autowired
    private UserService userService;
    @Autowired
    private SearchService searchService;

    @Autowired
    private DateTimeFormatter dateTimeFormatter;

    @PostMapping("/")
    public ResponseEntity<?> searchRentals(@Nullable @RequestHeader("Authorization") String jwt, @RequestBody SearchRequestDTO dto){
        Optional<User> optionalUser = userService.getUserByJwt(jwt);
        if (optionalUser.isPresent()){
            try{
                User user = optionalUser.get();
                searchService.addSearch(user,dto);
            }
            catch (IllegalArgumentException i){
                return ResponseEntity.badRequest().build();
            }
        }


        Specification<Rental> searchSpecification = rentalSpecificationService.getSearchSpecification(dto);

        Pageable pageable = new PageRequestDTO().getPageable(dto.getPageRequestDTO());

        Page<Rental> rentalPage = rentalRepository.findAll(searchSpecification,pageable);

        int days = -1;
        int guests = -1;
        List<SpecificationDTO> specList = dto.getSpecificationList();
        for (SpecificationDTO specDto: specList) {
            if (specDto.getOperation().equals(SpecificationDTO.Operation.DATES)){
                String[] stringDates = specDto.getValue().split(",");

                LocalDate startDate = LocalDate.parse(stringDates[0],dateTimeFormatter);
                LocalDate endDate = startDate;
                if (stringDates.length == 2) {
                    endDate = LocalDate.parse(stringDates[1], dateTimeFormatter);
                }
                days = startDate.datesUntil(endDate.plusDays(1L)).toList().size();
                continue;
            }
            if (specDto.getColumn().equals("maxGuests") && specDto.getOperation().equals(SpecificationDTO.Operation.GREATER_THAN)){
                guests = Integer.parseInt(specDto.getValue());
            }
            if (days > 0 && guests > 0) break;
        }

        final int finalDays = days;
        final int finalGuests = guests;
        List<Rental> sortedList = rentalPage.stream().sorted(new Comparator<Rental>() {
            @Override
            public int compare(Rental r1, Rental r2) {
                return r1.getPrice(finalDays,finalGuests).compareTo(r2.getPrice(finalDays,finalGuests));
            }
        }).toList();

        Map<String, Object> responseBody = new HashMap<String, Object>();
        responseBody.put("content", sortedList);
        responseBody.put("totalPages",rentalPage.getTotalPages());
        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping("/{rentalId}/details")
    public ResponseEntity<?> rentalInfo(@PathVariable("rentalId") Long rentalId, @Nullable @RequestHeader("Authorization") String jwt){
        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) return ResponseEntity.badRequest().build();
        Rental rental = optionalRental.get();

        Optional<User> optionalUser = userService.getUserByJwt(jwt);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            Optional<SearchHistory> optionalSearchHistory = searchHistoryRepository.findByUser(user);
            if (optionalSearchHistory.isEmpty()){
                searchHistoryRepository.save(new SearchHistory(user,rental));
            }
            else{
                SearchHistory searchHistory = optionalSearchHistory.get();
                searchHistory.addRental(rental);
                searchHistoryRepository.save(searchHistory);
            }
        }

        return ResponseEntity.ok().body(rental);
    }
}
