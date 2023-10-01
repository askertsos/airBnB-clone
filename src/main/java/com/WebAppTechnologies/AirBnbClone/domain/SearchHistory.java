package com.WebAppTechnologies.AirBnbClone.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @ElementCollection(targetClass = Search.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "history_searches")
    private List<Search> searchList = new ArrayList<>();

    //Number of times a specific rental was searched by the user
    @ElementCollection(targetClass = Rental.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "history_rentals")
    private Map<Rental,Integer> rentalMap = new HashMap<>();

    public SearchHistory() {}

    public SearchHistory(Long id, User user, List<Search> searchList, Map<Rental,Integer> rentalMap) {
        this.id = id;
        this.user = user;
        this.searchList = searchList;
        this.rentalMap = rentalMap;
    }

    public SearchHistory(User user, Search search){
        this.user = user;
        this.searchList = new ArrayList<>();
        this.searchList.add(search);
    }

    public SearchHistory(User user, Rental rental){
        this.user = user;
        this.rentalMap.put(rental,1);
    }

    public void addSearch(Search search){
        this.searchList.add(search);
    }

    public void addRental(Rental rental){
        Integer appearances = this.rentalMap.get(rental);
        if (appearances == null) appearances = 0;
        this.rentalMap.put(rental,appearances+1);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Search> getSearchList() {
        return searchList;
    }

    public void setSearchList(List<Search> searchList) {
        this.searchList = searchList;
    }

    public Map<Rental,Integer> getRentalMap() {
        return rentalMap;
    }

    public void setRentalMap(Map<Rental,Integer> rentalMap) {
        this.rentalMap = rentalMap;
    }
}
