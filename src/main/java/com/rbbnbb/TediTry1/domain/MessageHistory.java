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
    @JoinColumn(name = "host_id", referencedColumnName = "id")
    private User host;

    @ElementCollection
    @CollectionTable(name = "history_messages", joinColumns = @JoinColumn(name = "message_id"))
    private Set<Message> messageSet = new HashSet<>();

    public void addMessage(Message m){ this.messageSet.add(m); }

    public MessageHistory() {}

    public MessageHistory(Long id, User tenant, User host, Set<Message> messageSet) {
        this.id = id;
        this.tenant = tenant;
        this.host = host;
        this.messageSet = messageSet;
    }

    public MessageHistory(Long id, User tenant, User host, Message message){
        this.id = id;
        this.tenant = tenant;
        this.host = host;
        this.messageSet.add(message);
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
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public Set<Message> getMessageSet() {
        return messageSet;
    }

    public void setMessageSet(Set<Message> messageSet) {
        this.messageSet = messageSet;
    }
}
