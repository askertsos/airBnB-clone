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

    private List<Search> searchList;

    public SearchHistory() {}

    public SearchHistory(Long id, User user, List<Search> searchList) {
        this.id = id;
        this.user = user;
        this.searchList = searchList;
    }

    public SearchHistory(User user, Search search){
        this.user = user;
        this.searchList = new ArrayList<>();
        this.searchList.add(search);
    }

    public void addSearch(Search search){
        this.searchList.add(search);
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
}
