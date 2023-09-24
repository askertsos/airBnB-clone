package com.rbbnbb.TediTry1.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

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
    private Set<Message> messageSet = new HashSet<>();

    public void addMessage(Message m){ this.messageSet.add(m); }

    public MessageHistory() {}

    public MessageHistory(Long id, User tenant, Rental rental, Set<Message> messageSet) {
        this.id = id;
        this.tenant = tenant;
        this.rental = rental;
        this.messageSet = messageSet;
    }

    public MessageHistory(User tenant, Rental rental, Message message){
        this.tenant = tenant;
        this.rental = rental;
        this.messageSet.add(message);
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


    public Set<Message> getMessageSet() {
        return messageSet;
    }

    public void setMessageSet(Set<Message> messageSet) {
        this.messageSet = messageSet;
    }
}
