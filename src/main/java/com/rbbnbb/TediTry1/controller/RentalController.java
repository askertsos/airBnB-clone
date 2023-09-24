package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.domain.*;
import com.rbbnbb.TediTry1.dto.BookingDTO;
import com.rbbnbb.TediTry1.dto.ReviewDTO;
import com.rbbnbb.TediTry1.repository.*;
import com.rbbnbb.TediTry1.services.AuthenticationService;
import com.rbbnbb.TediTry1.services.RentalService;
import com.rbbnbb.TediTry1.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;
    @Autowired
    private RentalService rentalService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MessageHistoryRepository messageHistoryRepository;

    @Autowired
    private DateTimeFormatter dateTimeFormatter;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/{rentalId}/details")
    public ResponseEntity<?> rentalInfo(@PathVariable("rentalId") Long rentalId){
        Rental rental = rentalRepository.findById(rentalId).get();
        return ResponseEntity.ok().body(rental);
    }

    @PostMapping("/{rentalId}/get_price")
    public ResponseEntity<?> bookRental(@PathVariable("rentalId") Long rentalId, @RequestHeader("Authorization") String jwt, @RequestBody BookingDTO dto){
        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) return ResponseEntity.badRequest().build();
        Rental rental = optionalRental.get();

        LocalDate startDate = LocalDate.parse(dto.getStartDate(),dateTimeFormatter);
        LocalDate endDate = LocalDate.parse(dto.getEndDate(),dateTimeFormatter);

        Integer nDays = startDate.
                datesUntil(endDate.plusDays(1L)).
                toList().
                size();

        dto.setPrice(rental.getPrice(nDays,dto.getGuests()));

        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/{rentalId}/book/confirm")
    @Transactional
    public ResponseEntity<?> confirmBooking(@PathVariable("rentalId") Long rentalId, @RequestHeader("Authorization") String jwt, @RequestBody BookingDTO dto){
        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) return ResponseEntity.badRequest().build();
        Rental rental = optionalRental.get();

        LocalDate startDate, endDate;
        try {
            startDate = LocalDate.parse(dto.getStartDate(), dateTimeFormatter);
            endDate = LocalDate.parse(dto.getEndDate(), dateTimeFormatter);
        }
        catch (DateTimeParseException e){
            return ResponseEntity.badRequest().build();
        }
        System.out.println("before constructBooking");
        Booking newBooking = rentalService.constructBooking(jwt,rental,dto,startDate,endDate);
        if (Objects.isNull(newBooking)) return ResponseEntity.badRequest().build();
        System.out.println("after constructBooking");

        //Update the rental entity
        List<LocalDate> remainingDates = rental.getAvailableDates();
        remainingDates.removeAll(startDate.datesUntil(endDate.plusDays(1L)).toList());
        rental.setAvailableDates(remainingDates);
        rentalRepository.save(rental);

        bookingRepository.save(newBooking);


        return ResponseEntity.ok().body(newBooking);
    }

    @PostMapping("/{rentalId}/review")
    @Transactional
    public ResponseEntity<?> submitReview(@PathVariable("rentalId") Long rentalId, @RequestHeader("Authorization") String jwt, @RequestBody ReviewDTO body){
        User reviewer = userService.getUserByJwt(jwt).get();

        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) return ResponseEntity.badRequest().build();
        Rental rental = optionalRental.get();

        //Assert that this user has booked this rental before
        //Comment this out for the time being while inserting the CSVs into the database
//        List<Booking> bookingList = bookingRepository.findByBookerAndRental(reviewer,rental);
//        if (bookingList.isEmpty()) return ResponseEntity.badRequest().build();

        Review review = new Review(body,reviewer,rental);
        reviewRepository.save(review);
        rental.addReview(review);

        return ResponseEntity.ok().body(review);
    }

    @GetMapping("/{rentalId}/message_history")
    public ResponseEntity<?> viewMessageHistory(@PathVariable("rentalId") Long rentalId, @RequestHeader("Authorization") String jwt){
        User tenant = userService.getUserByJwt(jwt).get();

        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) return ResponseEntity.badRequest().build();
        Rental rental = optionalRental.get();

        Optional<MessageHistory> optionalMessageHistory = messageHistoryRepository.findByTenantAndRental(tenant,rental);
        if (optionalMessageHistory.isEmpty()){
            return ResponseEntity.ok().body(new MessageHistory(tenant,rental));
        }
        MessageHistory messageHistory = optionalMessageHistory.get();
        return ResponseEntity.ok().body(messageHistory);
    }

    @PostMapping("/{rentalId}/message_host")
    @Transactional
    public ResponseEntity<?> messageHost(@PathVariable("rentalId") Long rentalId, @RequestHeader("Authorization") String jwt, @RequestBody String text){
        User tenant = userService.getUserByJwt(jwt).get();

        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) return ResponseEntity.badRequest().build();
        Rental rental = optionalRental.get();

        User host = rental.getHost();

        //Assert that tenant and host are not the same user
        if (tenant.equals(host)) return ResponseEntity.badRequest().build();

        Message newMessage = new Message(tenant,host,text);

        SimpleJpaRepository<Message, Long> messageRepo;
        messageRepo = new SimpleJpaRepository<Message, Long>(Message.class,entityManager);
        messageRepo.save(newMessage);

        Optional<MessageHistory> optionalMessageHistory = messageHistoryRepository.findByTenantAndRental(tenant,rental);
        MessageHistory messageHistory;
        if (optionalMessageHistory.isEmpty()){
            messageHistory = new MessageHistory(tenant,rental,newMessage);
        }
        else{
            messageHistory = optionalMessageHistory.get();
            messageHistory.addMessage(newMessage);
        }
        messageHistoryRepository.save(messageHistory);

        return ResponseEntity.ok().body(newMessage);
    }

}
