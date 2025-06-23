package com.gtu.users_management_service.presentation.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtu.users_management_service.application.dto.PassengerDTO;
import com.gtu.users_management_service.application.dto.PasswordUpdateDTO;
import com.gtu.users_management_service.application.dto.ResponseDTO;
import com.gtu.users_management_service.application.usecase.PassengerUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing passenger", description = "Update the details of an existing passenger.")
    public ResponseEntity<ResponseDTO<PassengerDTO>> updatePassenger(@PathVariable Long id, @Valid @RequestBody PassengerDTO passengerDTO) {
        passengerDTO.setId(id);
        PassengerDTO updatedPassenger = passengerUseCase.updatePassenger(passengerDTO);
        return ResponseEntity.ok(new ResponseDTO<>("Passenger updated successfully", updatedPassenger, 200));
    }

    @PutMapping("/{id}/password")
    @Operation(summary = "Update passenger password", description = "Update the password of an existing passenger.")
    public ResponseEntity<ResponseDTO<PassengerDTO>> updatePassword(@PathVariable Long id, @Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setId(id);
        passengerDTO.setPassword(passwordUpdateDTO.getCurrentPassword());
        PassengerDTO updatedPassenger = passengerUseCase.updatePassword(passengerDTO, passwordUpdateDTO);
        return ResponseEntity.ok(new ResponseDTO<>("Passenger password updated successfully", updatedPassenger, 200));
    }

    @GetMapping("/count")
    @Operation(summary = "Count passengers", description = "Get the total number of passengers.")
    public ResponseEntity<ResponseDTO<Long>> countPassengers() {
        Long count = passengerUseCase.countPassengers();
        return ResponseEntity.ok(new ResponseDTO<>("Total number of passengers", count, 200));
    }
}
