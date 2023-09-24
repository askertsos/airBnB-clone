package com.rbbnbb.TediTry1.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @ElementCollection(targetClass = Search.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "history_searches", joinColumns = @JoinColumn(name = "search_id"))
    private List<Search> searchList;

    @ElementCollection(targetClass = Rental.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "history_rentals", joinColumns = @JoinColumn(name = "rental_id"))
    private List<Rental> rentalList; //Duplicate rental entries make correlation stronger

    public SearchHistory() {}

    public SearchHistory(Long id, User user, List<Search> searchList, List<Rental> rentalList) {
        this.id = id;
        this.user = user;
        this.searchList = searchList;
        this.rentalList = rentalList;
    }

    public SearchHistory(User user, Search search){
        this.user = user;
        this.searchList = new ArrayList<>();
        this.searchList.add(search);
    }

    public SearchHistory(User user, Rental rental){
        this.user = user;
        this.rentalList = new ArrayList<>();
        this.rentalList.add(rental);
    }

    public void addSearch(Search search){
        this.searchList.add(search);
    }

    public void addRental(Rental rental){
        this.rentalList.add(rental);
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

    public List<Rental> getRentalList() {
        return rentalList;
    }

    public void setRentalList(List<Rental> rentalList) {
        this.rentalList = rentalList;
    }
}
