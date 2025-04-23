package com.gtu.users_management_service.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gtu.users_management_service.application.dto.PasswordUpdateDTO;
import com.gtu.users_management_service.domain.model.Passenger;
import com.gtu.users_management_service.domain.repository.PassengerRepository;

@ExtendWith(MockitoExtension.class)
class PassengerServiceImplTest {
    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private PassengerServiceImpl passengerService;

    private Passenger passenger;

    @BeforeEach
    void setUp() {
        passenger = new Passenger();
        passenger.setId(1L);
        passenger.setName("John Doe");
        passenger.setEmail("johndoe@example.com");
        passenger.setPassword("Passw0rd");
    }

    @Test
    void createPassenger_Success() {
        when(passengerRepository.save(passenger)).thenReturn(passenger);
        Passenger createdPassenger = passengerService.createPassenger(passenger);

        assertNotNull(createdPassenger);
        assertEquals("John Doe", createdPassenger.getName());
        assertEquals("johndoe@example.com", createdPassenger.getEmail());
        verify(passengerRepository, times(1)).existsByEmail("johndoe@example.com");
        verify(passengerRepository, times(1)).save(passenger);
    }

    @Test
    void createPassenger_EmailAlreadyExists() {
        when(passengerRepository.existsByEmail("johndoe@example.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> { 
            passengerService.createPassenger(passenger);
        });
        assertEquals("Email already exists", exception.getMessage());

        verify(passengerRepository, times(1)).existsByEmail("johndoe@example.com");
        verify(passengerRepository, never()).save(any());
    }

    @Test
    void createPassenger_InvalidPassword() {
        passenger.setPassword("12345");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> { 
            passengerService.createPassenger(passenger);
        });
        assertEquals("Password must contain at least 8 characters, including uppercase letters and numbers", exception.getMessage());

        verify(passengerRepository, never()).save(any());
    }

    @Test
    void updatePassenger_Success() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        Passenger updatedPassenger = new Passenger();
        updatedPassenger.setId(1L);
        updatedPassenger.setName("Jane Doe");
        updatedPassenger.setEmail("janedoe@example.com");
        
        when(passengerRepository.save(any(Passenger.class))).thenReturn(updatedPassenger);
        Passenger result = passengerService.updatePassenger(updatedPassenger);

        assertNotNull(result);
        assertEquals("Jane Doe", result.getName());
        assertEquals("janedoe@example.com", result.getEmail());
        verify(passengerRepository, times(1)).findById(1L);
        verify(passengerRepository, times(1)).existsByEmail("janedoe@example.com");
        verify(passengerRepository, times(1)).save(any(Passenger.class));
    }

    @Test
    void updatePassenger_EmailAlreadyExists() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(passengerRepository.existsByEmail("johndoe@example.com")).thenReturn(true);
        Passenger updatedPassenger = new Passenger();
        updatedPassenger.setId(1L);
        updatedPassenger.setEmail("johndoe@example.com");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> { 
            passengerService.updatePassenger(updatedPassenger);
        });

        assertEquals("Email already exists", exception.getMessage());
        verify(passengerRepository, times(1)).findById(1L);
        verify(passengerRepository, times(1)).existsByEmail("johndoe@example.com");
    }

    @Test
    void updatePassenger_PasswordCannotBeUpdated() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        Passenger updatedPassenger = new Passenger();
        updatedPassenger.setId(1L);
        updatedPassenger.setPassword("NewPassword");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> { 
            passengerService.updatePassenger(updatedPassenger);
        });

        assertEquals("Password cannot be updated", exception.getMessage());
        verify(passengerRepository, times(1)).findById(1L);
        verify(passengerRepository, never()).save(any());
    }

    @Test
    void updatePassenger_PassengerNotFound() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());
        Passenger updatedPassenger = new Passenger();
        updatedPassenger.setId(1L);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> { 
            passengerService.updatePassenger(updatedPassenger);
        });

        assertEquals("Passenger not found", exception.getMessage());
        verify(passengerRepository, times(1)).findById(1L);
        verify(passengerRepository, never()).save(any());
    }
}
