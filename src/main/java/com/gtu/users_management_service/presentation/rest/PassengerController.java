package com.gtu.users_management_service.presentation.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtu.users_management_service.application.dto.PassengerDTO;
import com.gtu.users_management_service.application.dto.ResponseDTO;
import com.gtu.users_management_service.application.usecase.PassengerUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/passengers")
@Tag(name = "Passenger", description = "Endpoints for managing passengers")
@CrossOrigin(origins = "*")
public class PassengerController {
    
    private final PassengerUseCase passengerUseCase;

    public PassengerController(PassengerUseCase passengerUseCase) {
        this.passengerUseCase = passengerUseCase;
    }

    @PostMapping 
    @Operation(summary = "Create a new passenger", description = "Add a new passenger to the system.")
    public ResponseEntity<ResponseDTO<PassengerDTO>> createPassenger(@Valid @RequestBody PassengerDTO passengerDTO) {
        PassengerDTO createdPassenger = passengerUseCase.createPassenger(passengerDTO);
        return ResponseEntity.status(201).body(new ResponseDTO<>("Passenger created successfully", createdPassenger, 201));
    }  
}
