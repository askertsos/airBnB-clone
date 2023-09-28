package com.WebAppTechnologies.AirBnbClone.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class MessageHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    private User tenant;

    @ManyToOne
    @JoinColumn(name = "rental_id", referencedColumnName = "id")
    private Rental rental;

    @ElementCollection
    @CollectionTable(name = "history_messages", joinColumns = @JoinColumn(name = "message_id"))
    private List<Message> messageList = new ArrayList<>();

    public void addMessage(Message m){ this.messageList.add(m); }

    public MessageHistory() {}

    public MessageHistory(Long id, User tenant, Rental rental, List<Message> messageList) {
        this.id = id;
        this.tenant = tenant;
        this.rental = rental;
        this.messageList = messageList;
    }

    public MessageHistory(User tenant, Rental rental, Message message){
        this.tenant = tenant;
        this.rental = rental;
        this.messageList.add(message);
    }

    public MessageHistory(User tenant, Rental rental){
        this.tenant = tenant;
        this.rental = rental;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getTenant() {
        return tenant;
    }

    public void setTenant(User tenant) {
        this.tenant = tenant;
    }

    public User getHost() {
        return rental.getHost();
    }


    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}
