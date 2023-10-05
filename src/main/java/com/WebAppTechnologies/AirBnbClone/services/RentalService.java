package com.WebAppTechnologies.AirBnbClone.services;

import com.WebAppTechnologies.AirBnbClone.domain.Address;
import com.WebAppTechnologies.AirBnbClone.domain.Booking;
import com.WebAppTechnologies.AirBnbClone.domain.User;
import com.WebAppTechnologies.AirBnbClone.repository.BookingRepository;
import com.WebAppTechnologies.AirBnbClone.repository.RentalRepository;
import com.WebAppTechnologies.AirBnbClone.domain.Rental;
import com.WebAppTechnologies.AirBnbClone.dto.BookingDTO;
import com.WebAppTechnologies.AirBnbClone.dto.NewRentalDTO;
import com.WebAppTechnologies.AirBnbClone.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@Transactional
public class RentalService {

    @Bean

    public DateTimeFormatter dateTimeFormatter(){
        return DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PhotoRepository photoRepository;
    
    //Constructs the new booking based on the user, rental and booking info.
    //Returns new Booking instance if all info is valid, null otherwise
    public Booking constructBooking(String jwt, Rental rental, BookingDTO bookingDTO, LocalDate startDate, LocalDate endDate){

        User booker = userService.assertUserHasAuthority(jwt,"TENANT");
        if (Objects.isNull(booker)) return null;

        //Assert that the guest number, as well as the booking dates are valid
        if (bookingDTO.getGuests() > rental.getMaxGuests()) return null;
        if (startDate.isBefore(LocalDate.now())) return null;
        if (startDate.isAfter(endDate)) return null;

        List<LocalDate> bookingDates = startDate.datesUntil(endDate.plusDays(1L)).toList();
        if (bookingDates.size() < rental.getMinDays()) return null;


        Set<LocalDate> availableDateSet = new HashSet<LocalDate>(rental.getAvailableDates());
        if (!availableDateSet.containsAll(bookingDates)) return null;

        return new Booking(booker, rental, startDate, endDate, bookingDTO.getGuests());
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
            List<LocalDate> dates = new ArrayList<>();
            try {
                for (String date : dto.getAvailableDates()) {
                    LocalDate localDate = LocalDate.parse(date, dateTimeFormatter());
                    dates.add(localDate);
                }
            } catch (DateTimeParseException dateTimeParseException) {
                throw new IllegalArgumentException();
            }
            rental.setAvailableDates(dates);
        }

        if (Objects.nonNull(dto.getAddress())){
            Address address = dto.getAddress();
            if (Objects.nonNull(address.getCountry())) rental.getAddress().setCountry(address.getCountry());
            if (Objects.nonNull(address.getCity())) rental.getAddress().setCity(address.getCity());
            if (Objects.nonNull(address.getNeighbourhood())) rental.getAddress().setNeighbourhood(address.getNeighbourhood());
            if (Objects.nonNull(address.getStreet())) rental.getAddress().setStreet(address.getStreet());
            if (Objects.nonNull(address.getNumber())) rental.getAddress().setNumber(address.getNumber());
            if (Objects.nonNull(address.getFloorNo())) rental.getAddress().setFloorNo(address.getFloorNo());
        }

        if (Objects.nonNull(dto.getMaxGuests())) rental.setMaxGuests(dto.getMaxGuests());
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
