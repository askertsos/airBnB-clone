package com.WebAppTechnologies.AirBnbClone.repository;

import com.WebAppTechnologies.AirBnbClone.domain.User;
import com.WebAppTechnologies.AirBnbClone.domain.MessageHistory;
import com.WebAppTechnologies.AirBnbClone.domain.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageHistoryRepository extends JpaRepository<MessageHistory,Long> {

    public Optional<MessageHistory> findByTenantAndRental(User tenant, Rental rental);

    public List<MessageHistory> findByRental(Rental rental);
}
