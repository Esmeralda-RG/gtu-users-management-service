package com.gtu.users_management_service.presentation.rest.internal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gtu.users_management_service.application.dto.PassengerDTO;
import com.gtu.users_management_service.domain.model.Passenger;
import com.gtu.users_management_service.domain.service.PassengerService;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/internal/passengers")
@Hidden
public class PassengerInternalController {

    private final PassengerService passengerService;

    public PassengerInternalController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }


    @GetMapping 
    public Passenger getPassengerByEmail(@RequestParam String email){
        return passengerService.getPassengerByEmail(email);
    }  

    @PutMapping("/{id}/reset-password")
    public Passenger resetPassword(@PathVariable Long id, @RequestParam String newPassword) {
        Passenger passenger = new Passenger();
        passenger.setId(id);
        return passengerService.resetPassword(passenger, newPassword);
    }


    @PostMapping
    public Passenger postMethodName(@RequestBody PassengerDTO passengerDTO) {
        return passengerService.createPassenger(new Passenger(
            passengerDTO.getId(),
            passengerDTO.getName(),
            passengerDTO.getEmail(),
            passengerDTO.getPassword()
        ));
    }
    
}
