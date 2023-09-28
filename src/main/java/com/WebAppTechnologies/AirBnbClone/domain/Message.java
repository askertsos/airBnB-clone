package com.WebAppTechnologies.AirBnbClone.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    private User recipient;

    private String contents;

    private LocalDateTime sentAt;

    public Message() {}

    public Message(Long id, User sender, User recipient, String contents, LocalDateTime sentAt) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.contents = contents;
        this.sentAt = sentAt;
    }

    public Message(User sender, User recipient, String contents) {
        this.sender = sender;
        this.recipient = recipient;
        this.contents = contents;
        this.sentAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}

