package com.rbbnbb.TediTry1.repository;

import com.rbbnbb.TediTry1.domain.MessageHistory;
import com.rbbnbb.TediTry1.domain.Rental;
import com.rbbnbb.TediTry1.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageHistoryRepository extends JpaRepository<MessageHistory,Long> {

    public Optional<MessageHistory> findByTenantAndRental(User tenant, Rental rental);

    public List<MessageHistory> findByRental(Rental rental);
}
