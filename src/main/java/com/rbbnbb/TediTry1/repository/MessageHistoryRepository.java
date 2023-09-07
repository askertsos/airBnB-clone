package com.rbbnbb.TediTry1.repository;

import com.rbbnbb.TediTry1.domain.MessageHistory;
import com.rbbnbb.TediTry1.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageHistoryRepository extends JpaRepository<MessageHistory,Long> {

    public Optional<MessageHistory> findByTenantAndHost(User tenant, User host);
}
