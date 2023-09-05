package com.rbbnbb.TediTry1.repository;

import com.rbbnbb.TediTry1.domain.Rental;
import com.rbbnbb.TediTry1.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.spi.PersistenceProvider;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long>, JpaSpecificationExecutor<Rental> {


//    @Query("SELECT r FROM Rental r WHERE r.host = ?1")
//    Collection<Rental> getHostRentals(User host);

    List<Rental> findByHost(User host);

    Optional<Rental> findByTitleIgnoreCase(String title);

    //Might concern address, availableDates, #of_people()
    //List<Rental> searchRentals(List<SearchDTO> dtos);

}
