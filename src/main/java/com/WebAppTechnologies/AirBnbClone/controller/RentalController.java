package com.WebAppTechnologies.AirBnbClone.controller;

import com.WebAppTechnologies.AirBnbClone.domain.*;
import com.WebAppTechnologies.AirBnbClone.repository.*;
import com.WebAppTechnologies.AirBnbClone.dto.BookingDTO;
import com.WebAppTechnologies.AirBnbClone.dto.ReviewDTO;
import com.WebAppTechnologies.AirBnbClone.services.AuthenticationService;
import com.WebAppTechnologies.AirBnbClone.services.RentalService;
import com.WebAppTechnologies.AirBnbClone.services.UserService;
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
import java.util.*;

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

    @GetMapping("/auth")
    public ResponseEntity<?> hasAccess(){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{rentalId}/details")
    public ResponseEntity<?> rentalInfo(@PathVariable("rentalId") Long rentalId){
        Rental rental = rentalRepository.findById(rentalId).get();
        return ResponseEntity.ok().body(rental);
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

        Booking newBooking = rentalService.constructBooking(jwt,rental,dto,startDate,endDate);
        if (Objects.isNull(newBooking)) return ResponseEntity.badRequest().build();


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
        List<Booking> bookingList = bookingRepository.findByBookerAndRental(reviewer,rental);
        if (bookingList.isEmpty()) return ResponseEntity.badRequest().build();

        Review review = new Review(body,reviewer,rental);
        reviewRepository.save(review);
        rental.addReview(review);

        return ResponseEntity.ok().body(review);
    }

    @GetMapping("/{rentalId}/message_history/{pageNo}")
    public ResponseEntity<?> viewMessageHistory(@PathVariable("rentalId") Long rentalId,@PathVariable("pageNo") Integer pageNo, @RequestHeader("Authorization") String jwt){
        User tenant = userService.getUserByJwt(jwt).get();

        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) return ResponseEntity.badRequest().build();
        Rental rental = optionalRental.get();

        Optional<MessageHistory> optionalMessageHistory = messageHistoryRepository.findByTenantAndRental(tenant,rental);
        if (optionalMessageHistory.isEmpty()){
            Map<String, Object> ResponseBody = new HashMap<String, Object>();
            ResponseBody.put("Messages", null);
            ResponseBody.put("MaxPage", 1);
            return ResponseEntity.ok().body(ResponseBody);
        }

        MessageHistory messageHistory = optionalMessageHistory.get();
        List<Message> messageList = new ArrayList<>(messageHistory.getMessageList());

        //Assert that the pageNo is valid
        final int index = pageNo - 1;
        final int pageSize = 18;
        final double messagesPerPage = (double) messageList.size() / pageSize;
        int maxPageNo = (int)Math.ceil(messagesPerPage);
        if (index < 0 || index > maxPageNo) return ResponseEntity.badRequest().build();

        messageList.sort((m1, m2) -> m2.getSentAt().compareTo(m1.getSentAt()));

//        messageHistory.setMessageList(messageList);
        final int start = index*pageSize;
        int end = Math.min((index + 1) * pageSize,messageList.size());
        List<Message> pagedList = messageList.subList(start,end);

        Map<String, Object> ResponseBody = new HashMap<String, Object>();
        ResponseBody.put("Messages", pagedList);
        ResponseBody.put("MaxPage", maxPageNo);

        return ResponseEntity.ok().body(ResponseBody);
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
