package com.rbbnbb.TediTry1.repository;

import com.rbbnbb.TediTry1.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
}
