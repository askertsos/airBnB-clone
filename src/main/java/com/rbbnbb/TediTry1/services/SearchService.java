package com.rbbnbb.TediTry1.services;

import com.rbbnbb.TediTry1.domain.Search;
import com.rbbnbb.TediTry1.domain.SearchHistory;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.dto.SearchRequestDTO;
import com.rbbnbb.TediTry1.dto.SpecificationDTO;
import com.rbbnbb.TediTry1.repository.SearchHistoryRepository;
import com.rbbnbb.TediTry1.repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SearchService {

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;
    @Autowired
    private DateTimeFormatter dateTimeFormatter;

    public void addSearch(User user, SearchRequestDTO dto) throws IllegalArgumentException{
        Search newSearch = new Search();
        String country = null;
        String city = null;
        String neighbourhood = null;
        LocalDate startDate = null;
        LocalDate endDate = null;
        Integer nGuests = null;

        List<SpecificationDTO> specificationDTOS = dto.getSpecificationList();

        for (SpecificationDTO specDto: specificationDTOS) {
            String column = specDto.getColumn();
            String value = specDto.getValue();
            if(specDto.getOperation().equals(SpecificationDTO.Operation.JOIN)){ //This is a join operation so there may be information about the location (address)
                if (column.equals("country")){
                    country = value;
                }
                else if (column.equals("city")){
                    city = value;
                }
                else if (column.equals("neighbourhood")){
                    neighbourhood = value;
                }
                continue;
            }
            if (specDto.getOperation().equals(SpecificationDTO.Operation.DATES)){
                String[] dates = value.split(",");
                if (dates.length != 2) throw new IllegalArgumentException();
                startDate = LocalDate.parse(dates[0],dateTimeFormatter);
                endDate = LocalDate.parse(dates[1],dateTimeFormatter);
                continue;
            }
            if (specDto.getOperation().equals(SpecificationDTO.Operation.LESS_THAN)){
                if (column.equals("maxGuests")) {
                    nGuests = Integer.parseInt(value) - 1;
                }
            }
        }

        newSearch.setCountry(country);
        newSearch.setCity(city);
        newSearch.setNeighbourhood(neighbourhood);
        newSearch.setStartDate(startDate);
        newSearch.setEndDate(endDate);
        newSearch.setnGuests(nGuests);

        searchRepository.save(newSearch);

        Optional<SearchHistory> optionalSearchHistory = searchHistoryRepository.findByUser(user);
        if (optionalSearchHistory.isPresent()){
            SearchHistory searchHistory = optionalSearchHistory.get();
            searchHistory.addSearch(newSearch);
            searchHistoryRepository.save(searchHistory);
            return;
        }

        SearchHistory searchHistory = new SearchHistory(user,newSearch);
        searchHistoryRepository.save(searchHistory);


    }
}
