package com.gtu.users_management_service.presentation.rest.internal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gtu.users_management_service.domain.model.Passenger;
import com.gtu.users_management_service.domain.service.PassengerService;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
@RequestMapping("/internal/passengers")
@Hidden
public class PassengerInternalController {

    private final PassengerService passengerService;

    public PassengerInternalController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }



    public Passenger getPassengerByEmail(@RequestParam String email){
        return passengerService.getPassengerByEmail(email);
    }




    
}
