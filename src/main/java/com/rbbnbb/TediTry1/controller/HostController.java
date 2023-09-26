package com.rbbnbb.TediTry1.controller;

import com.rbbnbb.TediTry1.domain.*;
import com.rbbnbb.TediTry1.dto.NewRentalDTO;
import com.rbbnbb.TediTry1.dto.PageRequestDTO;

import com.rbbnbb.TediTry1.repository.*;

import com.rbbnbb.TediTry1.repository.MessageHistoryRepository;
import com.rbbnbb.TediTry1.repository.RentalRepository;
import com.rbbnbb.TediTry1.repository.UserRepository;

import com.rbbnbb.TediTry1.services.AuthenticationService;
import com.rbbnbb.TediTry1.services.PhotoService;
import com.rbbnbb.TediTry1.services.RentalService;
import com.rbbnbb.TediTry1.services.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.*;

import java.util.HashMap;
import java.util.Map;
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
    private PhotoService photoService;

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

    @GetMapping("/auth")
    public ResponseEntity<?> authenticateJWT(@RequestHeader("Authorization") String jwt){
        User host = userService.getUserByJwt(jwt).get();
        Map<String, Object> ResponseBody = new HashMap<>();
        ResponseBody.put("Roles", host.getAuthorities());
        ResponseBody.put("isAuthenticatedHost", host.getIsAuthenticatedHost());
        return ResponseEntity.ok().body(ResponseBody);
    }

    @PostMapping("/rental/new")
    @Transactional
    public ResponseEntity<?> submitNewRental(@RequestBody NewRentalDTO body, @RequestHeader("Authorization") String jwt){
        User host = userService.getUserByJwt(jwt).get();

        //Create new rental entity
        Rental newRental = new Rental(host,body);

        //Save first time to generate the id, which is used for the path of the photos
        rentalRepository.save(newRental);

        return ResponseEntity.ok().body(newRental);
    }

    @PostMapping("/rental/{rentalId}/update")
    @Transactional
    public ResponseEntity<?> updateRental(@PathVariable("rentalId") Long rentalId, @RequestBody NewRentalDTO dto, @RequestHeader("Authorization") String jwt){

        Rental rental;
        try {
            rental = rentalRepository.findById(rentalId).get();
        } catch(Exception e){
            return ResponseEntity.badRequest().build();
        }

        User host = userService.getUserByJwt(jwt).get();
        if (!host.equals(rental.getHost())) return ResponseEntity.badRequest().build();
        rentalService.updateRental(rentalId, dto);
        Rental responseBody = rentalRepository.findById(rentalId).get();

        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping("/rental/{rentalId}/add_photos")
    public ResponseEntity<?> addRentalPhotos(@PathVariable("rentalId") Long rentalId,
                                             @RequestHeader("Authorization") String jwt,
                                             @RequestParam("image")List<MultipartFile> photos){


        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if(optionalRental.isEmpty()) return ResponseEntity.badRequest().build();
        Rental rental = optionalRental.get();

        User host = userService.getUserByJwt(jwt).get();
        if (!rental.getHost().getId().equals(host.getId())) return ResponseEntity.badRequest().build();

        String rentalPhotoDirectory = rental.getPhotoDirectory();
        File rentalDirectory = new File(rentalPhotoDirectory);
        if (!rentalDirectory.exists() || !rentalDirectory.isDirectory()){
            if (!rentalDirectory.mkdir()) return ResponseEntity.internalServerError().build();
        }

        List<Photo> photoList = new ArrayList<>(photos.size());
        try {
            for (MultipartFile file : photos) {
                String filePath = rentalPhotoDirectory + File.separator + file.getOriginalFilename();
                Optional<Photo> optionalPhoto = photoRepository.findByFilePath(filePath);
                if (optionalPhoto.isPresent()) continue;
                Photo photo = photoService.saveImage(file, rentalPhotoDirectory);
                photoList.add(photo);
            }
        }
        catch(IOException e){
            return ResponseEntity.internalServerError().build();
        }

        for (Photo p: photoList) {
            photoRepository.save(p);
            rental.addPhoto(p);
        }
        rentalRepository.save(rental);

        return ResponseEntity.ok().build();
    }


    @PostMapping("/rental/{rentalId}/remove_photos")
    public ResponseEntity<?> removeRentalPhotos(@PathVariable("rentalId") Long rentalId,
                                             @RequestHeader("Authorization") String jwt,
                                             @RequestBody List<String> filePathList){


        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if(optionalRental.isEmpty()) return ResponseEntity.badRequest().build();
        Rental rental = optionalRental.get();

        User host = userService.getUserByJwt(jwt).get();
        if (!rental.getHost().getId().equals(host.getId())) return ResponseEntity.badRequest().build();

        for (String filePath : filePathList) {
            Optional<Photo> optionalPhoto = photoRepository.findByFilePath(filePath);
            if (optionalPhoto.isEmpty()) continue;

            Photo photo = optionalPhoto.get();
            rental.removePhoto(photo);
            photoRepository.deleteById(photo.getId());
        }

        rentalRepository.save(rental);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/rental/list")
    @Transactional
    public ResponseEntity<?> listRentals(@RequestBody PageRequestDTO dto, @RequestHeader("Authorization") String jwt){

        User host = userService.getUserByJwt(jwt).get();

        Page<Rental> listRentalsPaginated = rentalRepository.findByHostWithPagination(host, dto.getPageable(dto));
        Map<String, Object> ResponseBody = new HashMap<String, Object>();
        ResponseBody.put("Rentals", listRentalsPaginated);

        return ResponseEntity.ok().body(ResponseBody);
    }

    @GetMapping("/{rentalId}/info")
    @Transactional
    public ResponseEntity<?> viewRentalInfo(@PathVariable("rentalId") Long rentalId, @RequestHeader("Authorization") String jwt){

        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if(optionalRental.isEmpty()) return ResponseEntity.badRequest().build();
        Rental rental = optionalRental.get();

        User host = userService.getUserByJwt(jwt).get();
        if (!rental.getHost().getId().equals(host.getId())) return ResponseEntity.badRequest().build();

        Map<String, Object> ResponseBody = new HashMap<String, Object>();
        ResponseBody.put("Rental", rental);

        return ResponseEntity.ok().body(ResponseBody);
    }

    @GetMapping("/rentals/{rentalId}/messages/{pageNo}")
    public ResponseEntity<?> getMessageHistory(@PathVariable("rentalId") Long rentalId, @PathVariable("pageNo") Integer pageNo, @RequestHeader("Authorization") String jwt){
        Optional<User> optionalUser = userService.getUserByJwt(jwt);
        if (optionalUser.isEmpty()) return ResponseEntity.badRequest().build();
        User host = optionalUser.get();

        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) return ResponseEntity.badRequest().build();
        Rental rental = optionalRental.get();

        if (!host.getId().equals(rental.getHost().getId())) return ResponseEntity.badRequest().build();
        //List containing all message histories with all tenants on this rental
        List<MessageHistory> messageHistoryList = messageHistoryRepository.findByRental(rental);

        if (messageHistoryList.isEmpty()) return ResponseEntity.ok().body(new ArrayList());

        //Assert that the pageNo is valid
        final int index = pageNo - 1;
        final int pageSize = 10;
        final double messagesPerPage = (double) messageHistoryList.size() / pageSize;
        int maxPageNo = (int)Math.ceil(messagesPerPage);
        if (pageNo < 1 || pageNo > maxPageNo) return ResponseEntity.badRequest().build();

        //Sort messageHistoryList with descending order depending on whose message was sent last
        messageHistoryList.sort((m1, m2) -> {
            List<Message> messageList1 = new ArrayList<>(m1.getMessageList());
            List<Message> messageList2 = new ArrayList<>(m2.getMessageList());

            messageList1.sort(Comparator.comparing(Message::getSentAt).reversed());
            messageList2.sort(Comparator.comparing(Message::getSentAt).reversed());

            return messageList2.get(0).getSentAt().compareTo(messageList1.get(0).getSentAt());
        });

        //Simulate pagination by getting the respective sublist
        final int start = index*pageSize;
        int end = Math.min((index + 1) * pageSize,messageHistoryList.size());
        List<MessageHistory> pagedList = messageHistoryList.subList(start,end);

        Map<String, Object> ResponseBody = new HashMap<String, Object>();
        ResponseBody.put("History", pagedList);
        ResponseBody.put("MaxPage", maxPageNo);

        return ResponseEntity.ok().body(ResponseBody);
    }

    @GetMapping("/{rentalId}/tenant/{tenantId}/message_history/{pageNo}")
    public ResponseEntity<?> viewMessageHistory(@PathVariable("rentalId") Long rentalId,@PathVariable("tenantId") Long tenantId ,@PathVariable("pageNo") Integer pageNo, @RequestHeader("Authorization") String jwt){
        User tenant = userRepository.findById(tenantId).get();

        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) return ResponseEntity.badRequest().build();
        Rental rental = optionalRental.get();

        User host = userService.getUserByJwt(jwt).get();
        if (!rental.getHost().getId().equals(host.getId())) return ResponseEntity.badRequest().build();


        Optional<MessageHistory> optionalMessageHistory = messageHistoryRepository.findByTenantAndRental(tenant,rental);
        if (optionalMessageHistory.isEmpty()){
            return ResponseEntity.ok().body(new MessageHistory(tenant,rental));
        }

        MessageHistory messageHistory = optionalMessageHistory.get();
        List<Message> messageList = new ArrayList<>(messageHistory.getMessageList());

        //Assert that the pageNo is valid
        final int index = pageNo - 1;
        final int pageSize = 18;
        final double messagesPerPage = (double) messageList.size() / pageSize;
        int maxPageNo = (int)Math.ceil(messagesPerPage);
        if (index < 0 || index > maxPageNo) return ResponseEntity.badRequest().build();

        //Sort messageList in descending order with respect to when each message was sent
        messageList.sort((m1, m2) -> m2.getSentAt().compareTo(m1.getSentAt()));

        messageHistory.setMessageList(messageList);
        final int start = index*pageSize;
        int end = Math.min((index + 1) * pageSize,messageList.size());
        List<Message> pagedList = messageList.subList(start,end);

        Map<String, Object> ResponseBody = new HashMap<String, Object>();
        ResponseBody.put("Messages", pagedList);
        ResponseBody.put("MaxPage", maxPageNo);

        return ResponseEntity.ok().body(ResponseBody);
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
        messageRepo = new SimpleJpaRepository<>(Message.class, entityManager);

        Message newMessage = new Message(host,tenant,text);
        messageRepo.save(newMessage);

        messageHistory.addMessage(newMessage);
        messageHistoryRepository.save(messageHistory);

        return ResponseEntity.ok().body(messageHistory);
    }

}
