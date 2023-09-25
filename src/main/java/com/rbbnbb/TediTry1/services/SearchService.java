package com.rbbnbb.TediTry1.services;

import com.rbbnbb.TediTry1.domain.Rental;
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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
        LocalDate startDate = null;
        LocalDate endDate = null;
        Integer guests = null;

        List<SpecificationDTO> specificationDTOS = dto.getSpecificationList();

        for (SpecificationDTO specDto: specificationDTOS) {
            String column = specDto.getColumn();
            String value = specDto.getValue();
            if(specDto.getOperation().equals(SpecificationDTO.Operation.JOIN)){ //This is a join operation so there may be information about the location (address)
                switch (column) {
                    case "country" -> newSearch.setCountry(value);
                    case "city" -> newSearch.setCity(value);
                    case "neighbourhood" -> newSearch.setNeighbourhood(value);
                }
                continue;
            }
            if (specDto.getOperation().equals(SpecificationDTO.Operation.DATES)){
                String[] stringDates = value.split(",");
                List<LocalDate> dateList = new ArrayList<>();
                try {
                    for (String date: stringDates) {
                        LocalDate localDate = LocalDate.parse(date,dateTimeFormatter);
                        dateList.add(localDate);
                    }
                }
                catch (DateTimeParseException d){
                    throw new IllegalArgumentException();
                }
                newSearch.setStartDate(dateList.get(0));
                newSearch.setEndDate(dateList.get(dateList.size()-1));
                continue;
            }
            if (specDto.getOperation().equals(SpecificationDTO.Operation.GREATER_OR_EQUAL)){
                if (column.equals("maxGuests")) {
                    try { guests = Integer.parseInt(value);}
                    catch (NumberFormatException n){ throw new IllegalArgumentException();}
                    newSearch.setGuests(guests);
                }
                continue;
            }
            if (specDto.getOperation().equals(SpecificationDTO.Operation.AMENITIES)){
                boolean boolValue = Boolean.parseBoolean(specDto.getValue());
                switch (specDto.getColumn()) {
                    case "hasWiFi" -> newSearch.setHasWiFi(boolValue);
                    case "hasAC" -> newSearch.setHasAC(boolValue);
                    case "hasHeating" -> newSearch.setHasHeating(boolValue);
                    case "hasKitchen" -> newSearch.setHasKitchen(boolValue);
                    case "hasTV" -> newSearch.setHasTV(boolValue);
                    case "hasParking" -> newSearch.setHasParking(boolValue);
                    case "hasElevator" -> newSearch.setHasElevator(boolValue);
                    default -> {} //do nothing
                }


            }
        }

        searchRepository.save(newSearch);

        Optional<SearchHistory> optionalSearchHistory = searchHistoryRepository.findByUser(user);
        if (optionalSearchHistory.isEmpty()){
            searchHistoryRepository.save(new SearchHistory(user,newSearch));
            return;
        }

        SearchHistory searchHistory = optionalSearchHistory.get();
        searchHistory.addSearch(newSearch);
        searchHistoryRepository.save(searchHistory);
    }

    public void addRental(User user, Rental rental){
        Optional<SearchHistory> optionalSearchHistory = searchHistoryRepository.findByUser(user);

        if(optionalSearchHistory.isEmpty()){
            searchHistoryRepository.save(new SearchHistory(user,rental));
            return;
        }
        SearchHistory searchHistory = optionalSearchHistory.get();
        searchHistory.addRental(rental);
        searchHistoryRepository.save(searchHistory);
    }

}
