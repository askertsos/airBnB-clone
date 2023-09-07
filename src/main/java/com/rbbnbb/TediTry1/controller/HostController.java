package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.domain.Message;
import com.rbbnbb.TediTry1.domain.MessageHistory;
import com.rbbnbb.TediTry1.domain.Rental;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.dto.NewRentalDTO;
import com.rbbnbb.TediTry1.repository.MessageHistoryRepository;
import com.rbbnbb.TediTry1.repository.RentalRepository;
import com.rbbnbb.TediTry1.repository.ReviewRepository;
import com.rbbnbb.TediTry1.repository.UserRepository;
import com.rbbnbb.TediTry1.services.AuthenticationService;
import com.rbbnbb.TediTry1.services.RentalService;
import com.rbbnbb.TediTry1.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/host")
public class HostController {

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
    private MessageHistoryRepository messageHistoryRepository;

    @Autowired
    private JwtDecoder jwtDecoder;

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/new")
    @Transactional
    public ResponseEntity<?> submitNewRental(@RequestBody NewRentalDTO body, @RequestHeader("Authorization") String jwt){

        User host = userService.assertUserHasAuthority(jwt,"HOST");
        if (Objects.isNull(host)) return ResponseEntity.badRequest().build();

        Rental newRental = new Rental(0L,body,host);
        rentalRepository.save(newRental);

        return ResponseEntity.ok().body(newRental);
    }

    @PostMapping("/{rental_id}/info")
    @Transactional
    public ResponseEntity<?> updateRental(@PathVariable("rental_id") String id, @RequestHeader("Authorization") String jwt, @RequestBody NewRentalDTO dto){
        Optional<Rental> optionalRental = rentalRepository.findById(Long.parseLong(id));
        if(optionalRental.isEmpty()) return ResponseEntity.badRequest().build();
        Rental rental = optionalRental.get();

        User host = userService.assertUserHasAuthority(jwt,"HOST");
        if (Objects.isNull(host)) return ResponseEntity.badRequest().build();

        if (!rental.getHost().equals(host)) return ResponseEntity.badRequest().build();

        try {
            rentalService.updateRental(rental.getId(), dto);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(rental);
    }

    @GetMapping("/message_history/{tenantId}")
    public ResponseEntity<?> getMessageHistory(@PathVariable("tenantId") Long tenantId, @RequestHeader("Authorization") String jwt){

        User host = userService.assertUserHasAuthority(jwt,"HOST");
        if (Objects.isNull(host)) return ResponseEntity.badRequest().build();

        User tenant = userService.assertUserHasAuthority(tenantId,"TENANT");
        if (Objects.isNull(tenant)) return ResponseEntity.badRequest().build();

        //Assert that host and tenant are not the same user
        if (host.equals(tenant)) return ResponseEntity.badRequest().build();

        Optional<MessageHistory> optionalMessageHistory = messageHistoryRepository.findByTenantAndHost(tenant,host);
        if (optionalMessageHistory.isEmpty()) return ResponseEntity.ok().build();
        MessageHistory messageHistory = optionalMessageHistory.get();

        return ResponseEntity.ok().body(messageHistory.getMessageSet());
    }

    @PostMapping("/message/{tenantId}")
    @Transactional
    public ResponseEntity<?> sendMessage(@PathVariable("tenantId") Long tenantId, @RequestHeader("Authorization") String jwt, @RequestBody String text){

        User host = userService.assertUserHasAuthority(jwt,"HOST");
        if (Objects.isNull(host)) return ResponseEntity.badRequest().build();

        User tenant = userService.assertUserHasAuthority(tenantId,"TENANT");
        if (Objects.isNull(tenant)) return ResponseEntity.badRequest().build();

        //Assert that host and tenant are not the same user
        if (host.equals(tenant)) return ResponseEntity.badRequest().build();

        Optional<MessageHistory> optionalMessageHistory = messageHistoryRepository.findByTenantAndHost(tenant,host);
        if (optionalMessageHistory.isEmpty()) return ResponseEntity.badRequest().build();

        MessageHistory messageHistory = optionalMessageHistory.get();

        SimpleJpaRepository<Message, Long> messageRepo;
        messageRepo = new SimpleJpaRepository<Message, Long>(Message.class,entityManager);

        Message newMessage = new Message(host,tenant,text);
        messageRepo.save(newMessage);

        messageHistory.addMessage(newMessage);
        messageHistoryRepository.save(messageHistory);

        return ResponseEntity.ok().body(text);
    }
}
