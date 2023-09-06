package com.rbbnbb.TediTry1.services;

import com.rbbnbb.TediTry1.domain.Booking;
import com.rbbnbb.TediTry1.domain.Rental;
import com.rbbnbb.TediTry1.domain.User;
import com.rbbnbb.TediTry1.dto.BookingDTO;
import com.rbbnbb.TediTry1.dto.NewRentalDTO;
import com.rbbnbb.TediTry1.repository.BookingRepository;
import com.rbbnbb.TediTry1.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class RentalService {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private BookingRepository bookingRepository;

    //Constructs the new booking based on the user, rental and booking info.
    //Returns the instance if all info is valid, null otherwise
    public Booking constructBooking(String jwt, Long rentalId, BookingDTO bookingDTO){

        //Find the rental
        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isEmpty()) return null;
        Rental rental = optionalRental.get();

        //Find the user
        Optional<User> optionalUser = authenticationService.getUserByJwt(jwt);
        if (optionalUser.isEmpty()) return null;
        User user = optionalUser.get();

        //Assert that the guest number, as well as the booking dates are valid
        if (bookingDTO.getGuests() > rental.getMaxPeople()) return null;
        if (bookingDTO.getDates().isEmpty()) return null;
        if (bookingDTO.getDates().size() < rental.getMinDays()) return null;

        //Convert all dates from String to LocalDate and, if one is invalid, return null
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        List<LocalDate> bookingDates = new ArrayList<>();
        try {
            for (String date : bookingDTO.getDates()) {
                LocalDate localDate = LocalDate.parse(date, formatter);
                if (!rental.getAvailableDates().contains(localDate)) return null;
                bookingDates.add(localDate);
            }
        }
        catch (DateTimeParseException d){return null;}

        //All dates are both valid and available in the rental, go through with removing them and creating the new booking
        rental.removeAvailableDates(bookingDates);

        //Save changes
        rentalRepository.save(rental);

        //Save the new booking instance
        return new Booking(0L,user,rental,bookingDTO);
    }

    public void updateRental(Long id, NewRentalDTO dto) throws IllegalArgumentException{

        Optional<Rental> optionalRental = rentalRepository.findById(id);
        if (optionalRental.isEmpty()) throw new IllegalArgumentException();
        Rental rental = optionalRental.get();

        if (Objects.nonNull(dto.getTitle())) rental.setTitle(dto.getTitle());
        if (Objects.nonNull(dto.getBasePrice())) rental.setBasePrice(dto.getBasePrice());
        if (Objects.nonNull(dto.getChargePerPerson())) rental.setChargePerPerson(dto.getChargePerPerson());

        //Empty list is a valid argument, so there is no need to check for it
        if (Objects.nonNull(dto.getAvailableDates())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
            List<LocalDate> dates = new ArrayList<>();
            try {
                for (String date : dto.getAvailableDates()) {
                    LocalDate localDate = LocalDate.parse(date, formatter);
                    dates.add(localDate);
                }
            } catch (DateTimeParseException dateTimeParseException) {
                throw new IllegalArgumentException();
            }
            rental.setAvailableDates(dates);
        }

        if (Objects.nonNull(dto.getMaxPeople())) rental.setMaxPeople(dto.getMaxPeople());
        if (Objects.nonNull(dto.getBeds())) rental.setBeds(dto.getBeds());
        if (Objects.nonNull(dto.getBedrooms())) rental.setBedrooms(dto.getBedrooms());
        if (Objects.nonNull(dto.getBathrooms())) rental.setBathrooms(dto.getBathrooms());
        if (Objects.nonNull(dto.getType())) rental.setType(dto.getType());
        if (Objects.nonNull(dto.getHasLivingRoom())) rental.setHasLivingRoom(dto.getHasLivingRoom());
        if (Objects.nonNull(dto.getSurfaceArea())) rental.setSurfaceArea(dto.getSurfaceArea());
        if (Objects.nonNull(dto.getDescription())) rental.setDescription(dto.getDescription());
        if (Objects.nonNull(dto.getAllowSmoking())) rental.setAllowSmoking(dto.getAllowSmoking());
        if (Objects.nonNull(dto.getAllowPets())) rental.setAllowPets(dto.getAllowPets());
        if (Objects.nonNull(dto.getAllowEvents())) rental.setAllowEvents(dto.getAllowEvents());
        if (Objects.nonNull(dto.getMinDays())) rental.setMinDays(dto.getMinDays());
        if (Objects.nonNull(dto.getAddress())) rental.setAddress(dto.getAddress());
        if (Objects.nonNull(dto.getPublicTransport())) rental.setPublicTransport(dto.getPublicTransport());
        if (Objects.nonNull(dto.getHasWiFi())) rental.setHasWiFi(dto.getHasWiFi());
        if (Objects.nonNull(dto.getHasAC())) rental.setHasAC(dto.getHasAC());
        if (Objects.nonNull(dto.getHasHeating())) rental.setHasHeating(dto.getHasHeating());
        if (Objects.nonNull(dto.getHasKitchen())) rental.setHasKitchen(dto.getHasKitchen());
        if (Objects.nonNull(dto.getHasTV())) rental.setHasTV(dto.getHasTV());
        if (Objects.nonNull(dto.getHasParking())) rental.setHasParking(dto.getHasParking());
        if (Objects.nonNull(dto.getHasElevator())) rental.setHasElevator(dto.getHasElevator());

        //Host and reviews cannot be altered
        rentalRepository.save(rental);
    }


}
