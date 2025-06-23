package com.gtu.users_management_service.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gtu.users_management_service.application.dto.PassengerDTO;
import com.gtu.users_management_service.application.dto.PasswordUpdateDTO;
import com.gtu.users_management_service.domain.model.Passenger;
import com.gtu.users_management_service.domain.service.PassengerService;

@ExtendWith(MockitoExtension.class)
class PassengerUseCaseTest {
    @Mock
    private PassengerService passengerService;

    @InjectMocks
    private PassengerUseCase passengerUseCase;

    private Passenger passenger;
    private PassengerDTO passengerDTO;
    private PasswordUpdateDTO passwordUpdateDTO;

    @BeforeEach
    void setUp() {
        passenger = new Passenger();
        passenger.setId(1L);
        passenger.setName("John Doe");
        passenger.setEmail("johndoe@example.com");
        passenger.setPassword("Passw0rd");

        passengerDTO = new PassengerDTO();
        passengerDTO.setId(1L);
        passengerDTO.setName("John Doe");
        passengerDTO.setEmail("johndoe@example.com");
        
        passwordUpdateDTO = new PasswordUpdateDTO();
        passwordUpdateDTO.setCurrentPassword("Passw0rd");
        passwordUpdateDTO.setNewPassword("NewPassw0rd");
    }

    @Test
    void createPassenger_Success() {
        when(passengerService.createPassenger(any(Passenger.class))).thenReturn(passenger);
        PassengerDTO result = passengerUseCase.createPassenger(passengerDTO);

        assertNotNull(result);
        assertEquals(passenger.getId(), result.getId());
        assertEquals(passenger.getName(), result.getName());
        assertEquals(passenger.getEmail(), result.getEmail());

        verify(passengerService, times(1)).createPassenger(any(Passenger.class));
    }

    @Test
    void createPassenger_EmailAlreadyExists() {
        when(passengerService.createPassenger(any(Passenger.class))).thenThrow(new IllegalArgumentException("Email already exists"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            passengerUseCase.createPassenger(passengerDTO);
        });
        assertEquals("Email already exists", exception.getMessage());

        verify(passengerService, times(1)).createPassenger(any(Passenger.class));
    }

    @Test
    void createPassenger_InvalidPassword() {
        passengerDTO.setPassword("12345");
        when(passengerService.createPassenger(any(Passenger.class))).thenThrow(new IllegalArgumentException("Invalid password"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            passengerUseCase.createPassenger(passengerDTO);
        });
        assertEquals("Invalid password", exception.getMessage());

        verify(passengerService, times(1)).createPassenger(any(Passenger.class));
    }

    @Test
    void updatePassenger_Success() {
        passengerDTO.setName("Jane Doe");
        passengerDTO.setEmail("janedoe@example.com");
        passenger.setName("Jane Doe");
        passenger.setEmail("janedoe@example.com");

        when(passengerService.updatePassenger(any(Passenger.class))).thenReturn(passenger);
        PassengerDTO result = passengerUseCase.updatePassenger(passengerDTO);

        assertNotNull(result);
        assertEquals(passenger.getId(), result.getId());
        assertEquals("Jane Doe", result.getName());
        assertEquals("janedoe@example.com", result.getEmail());

        verify(passengerService, times(1)).updatePassenger(any(Passenger.class));
    }

    @Test
    void updatePassenger_PassengerNotFound() {
        when(passengerService.updatePassenger(any(Passenger.class))).thenThrow(new IllegalArgumentException("Passenger not found"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            passengerUseCase.updatePassenger(passengerDTO);
        });
        assertEquals("Passenger not found", exception.getMessage());

        verify(passengerService, times(1)).updatePassenger(any(Passenger.class));
    }

    @Test
    void updatePassword_Success() {
        passenger.setPassword("NewPassw0rd");
        when(passengerService.updatePassword(any(Passenger.class), any(PasswordUpdateDTO.class))).thenReturn(passenger);
        PassengerDTO result = passengerUseCase.updatePassword(passengerDTO, passwordUpdateDTO);

        assertNotNull(result);
        assertEquals(passenger.getId(), result.getId());
        assertEquals(passenger.getName(), result.getName());
        assertEquals(passenger.getEmail(), result.getEmail());
        assertEquals("NewPassw0rd", result.getPassword());

        verify(passengerService, times(1)).updatePassword(any(Passenger.class), any(PasswordUpdateDTO.class));
    }

    @Test
    void updatePassword_InvalidCurrentPassword() {
        when(passengerService.updatePassword(any(Passenger.class), any(PasswordUpdateDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid current password"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            passengerUseCase.updatePassword(passengerDTO, passwordUpdateDTO);
        });
        assertEquals("Invalid current password", exception.getMessage());

        verify(passengerService, times(1)).updatePassword(any(Passenger.class), any(PasswordUpdateDTO.class));
    }

    @Test
    void countPassengers_Success() {
        when(passengerService.countPassengers()).thenReturn(42L);
        Long result = passengerUseCase.countPassengers();

        assertEquals(42L, result);
        verify(passengerService, times(1)).countPassengers();
    }
}
