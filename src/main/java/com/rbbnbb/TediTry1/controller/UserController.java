package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.dto.NewRentalDTO;
import com.rbbnbb.TediTry1.dto.ReviewDTO;
import com.rbbnbb.TediTry1.domain.*;
import com.rbbnbb.TediTry1.dto.HostInfoDTO;
import com.rbbnbb.TediTry1.dto.UserDTO;
import com.rbbnbb.TediTry1.repository.*;

import com.rbbnbb.TediTry1.services.AuthenticationService;
import com.rbbnbb.TediTry1.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MessageHistoryRepository messageHistoryRepository;

    @PersistenceContext
    private EntityManager entityManager;



    //--------------------------------------------------------------------------------------------
    //-----------------                    ALL USERS                    --------------------------
    //--------------------------------------------------------------------------------------------

    @GetMapping("/auth")
    public ResponseEntity<?> authenticateJWT(@RequestHeader("Authorization") String jwt){
        return ResponseEntity.ok().build();
    }


    @PostMapping("/profile")
    @Transactional
    public ResponseEntity<?> updateUserInfo(@RequestHeader("Authorization") String jwt, @RequestBody UserDTO userDTO){

        Optional<User> optionalUser = userService.getUserByJwt(jwt);
        if (optionalUser.isEmpty()) return ResponseEntity.badRequest().build();
        User user = optionalUser.get();

        userService.updateUser(user,userDTO);

        return ResponseEntity.ok().body(user);
    }

    //        SimpleJpaRepository<Review, Long> reviewRepo;
    //        reviewRepo = new SimpleJpaRepository<Review, Long>(Review.class,entityManager);


    @GetMapping("/hosts/{id}")
    public ResponseEntity<?> getHostInfo(@PathVariable("id") Long id){
        User host = userService.assertUserHasAuthority(id,"HOST");
        if (Objects.isNull(host)) return ResponseEntity.badRequest().build();

        //Empty set is still a valid output
        Set<Rental> hostRentals = rentalRepository.findByHost(host);

        HostInfoDTO dto = new HostInfoDTO(host);
        dto.setHostRentals(hostRentals);

        return ResponseEntity.ok().body(dto);

    }

    //--------------------------------------------------------------------------------------------
    //-----------------                     TENANTS                     --------------------------
    //--------------------------------------------------------------------------------------------

    @PostMapping("/hosts/{hostId}/message")
    @Transactional
    public ResponseEntity<?> messageHost(@PathVariable("hostId") Long hostId, @RequestHeader("Authorization") String jwt, @RequestBody String text){
        User tenant = userService.assertUserHasAuthority(jwt,"TENANT");
        if (Objects.isNull(tenant)) return ResponseEntity.badRequest().build();

        User host = userService.assertUserHasAuthority(hostId,"HOST");
        if (Objects.isNull(host)) return ResponseEntity.badRequest().build();

        //Assert that tenant and host are not the same user
        if (tenant.equals(host)) return ResponseEntity.badRequest().build();

        Message newMessage = new Message(tenant,host,text);

        SimpleJpaRepository<Message, Long> messageRepo;
        messageRepo = new SimpleJpaRepository<Message, Long>(Message.class,entityManager);
        messageRepo.save(newMessage);

        Optional<MessageHistory> optionalMessageHistory = messageHistoryRepository.findByTenantAndHost(tenant,host);
        MessageHistory messageHistory;
        if (optionalMessageHistory.isEmpty()){
            messageHistory = new MessageHistory(0L,tenant,host,newMessage);
        }
        else{
            messageHistory = optionalMessageHistory.get();
            messageHistory.addMessage(newMessage);
        }
        messageHistoryRepository.save(messageHistory);

        return ResponseEntity.ok().body(newMessage);
    }

}