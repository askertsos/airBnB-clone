package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.domain.*;
import com.rbbnbb.TediTry1.dto.NewRentalDTO;
import com.rbbnbb.TediTry1.dto.PageRequestDTO;
import com.rbbnbb.TediTry1.repository.*;
import com.rbbnbb.TediTry1.services.AuthenticationService;
import com.rbbnbb.TediTry1.services.RentalService;
import com.rbbnbb.TediTry1.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    private PhotoRepository photoRepository;

    @Autowired
    private JwtDecoder jwtDecoder;

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/new")
    @Transactional
    public ResponseEntity<?> submitNewRental(@RequestBody NewRentalDTO body, @RequestHeader("Authorization") String jwt){
        User host = userService.getUserByJwt(jwt).get();

        //Create new rental entity
        Rental newRental = new Rental(host,body);

        //Save first time to generate the id, which is used for the path of the photos
        rentalRepository.save(newRental);

        //Generate the full path of each photo based on the rental's id and then store them inside the rental entity
        Set<Photo> rentalPhotos = new HashSet<>();

        //Add the appropriate prefix to all saved photos
        for (String filePath:body.getPhotoPaths()){
            String fullPath = "src/main/resources/RentalPhotos/" + newRental.getId().toString() + "/" + filePath;
            Photo newPhoto = new Photo(fullPath);
            photoRepository.save(newPhoto);
            rentalPhotos.add(newPhoto);
        }

        //Update the new rental entity and save it again
        newRental.setPhotos(rentalPhotos);
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

    @GetMapping("/rentals/{rentalId}/messages/{pageNo}")
    public ResponseEntity<?> getMessageHistory(@PathVariable("rentalId") Long rentalId, @PathVariable("pageNo") Integer pageNo, @RequestHeader("Authorization") String jwt){
        Optional<User> optionalUser = userService.getUserByJwt(jwt);
        if (optionalUser.isEmpty()) return ResponseEntity.badRequest().build();
        User host = optionalUser.get();

        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) return ResponseEntity.badRequest().build();
        Rental rental = optionalRental.get();
        if (!host.equals(rental.getHost())) return ResponseEntity.badRequest().build();
        //List containing all message histories with all tenants on this rental
        List<MessageHistory> messageHistoryList = messageHistoryRepository.findByRental(rental);

        if (messageHistoryList.isEmpty()) return ResponseEntity.ok().body(new ArrayList());

        //Assert that the pageNo is valid
        final int index = pageNo - 1;
        final int pageSize = 10;
        final double messagesPerPage = (double) messageHistoryList.size() / pageSize;
        int maxPageNo = (int)Math.ceil(messagesPerPage);
        if (pageNo < 1 || pageNo > maxPageNo) return ResponseEntity.badRequest().build();

        Collections.sort(messageHistoryList, new Comparator<MessageHistory>() {
            @Override
            public int compare(MessageHistory m1, MessageHistory m2) {
                List<Message> messageList1 = new ArrayList<>(m1.getMessageSet());
                List<Message> messageList2 = new ArrayList<>(m2.getMessageSet());

                messageList1.sort(Comparator.comparing(Message::getSentAt).reversed());
                messageList2.sort(Comparator.comparing(Message::getSentAt).reversed());

                return messageList2.get(0).getSentAt().compareTo(messageList1.get(0).getSentAt());
            }
        });

        final int startIndex = index*pageSize;
        int endIndex = (index + 1) * pageSize;
        if (endIndex >= messageHistoryList.size()) endIndex = messageHistoryList.size() - 1;

        List<MessageHistory> pagedList = messageHistoryList.subList(startIndex,endIndex);

        return ResponseEntity.ok().body(pagedList);
    }

    @PostMapping("/rentals/{rentalId}/message/{tenantId}")
    @Transactional
    public ResponseEntity<?> sendMessage(@PathVariable("rentalId") Long rentalId, @PathVariable("tenantId") Long tenantId, @RequestHeader("Authorization") String jwt, @RequestBody String text){

        User host = userService.getUserByJwt(jwt).get();

        User tenant = userService.assertUserHasAuthority(tenantId,"TENANT");
        if (Objects.isNull(tenant)) return ResponseEntity.badRequest().build();

        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) return ResponseEntity.badRequest().build();
        Rental rental = optionalRental.get();

        if (!host.equals(rental.getHost())) return ResponseEntity.badRequest().build();

        Optional<MessageHistory> optionalMessageHistory = messageHistoryRepository.findByTenantAndRental(tenant,rental);
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
