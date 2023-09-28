package com.WebAppTechnologies.AirBnbClone.repository;

import com.WebAppTechnologies.AirBnbClone.domain.Booking;
import com.WebAppTechnologies.AirBnbClone.domain.User;
import com.WebAppTechnologies.AirBnbClone.domain.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    public List<Booking> findByBooker(User user);

    public List<Booking> findByBookerAndRental(User user, Rental rental);
}
