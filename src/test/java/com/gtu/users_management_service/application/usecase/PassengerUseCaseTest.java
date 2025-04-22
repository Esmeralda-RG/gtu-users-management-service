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
        passengerDTO.setPassword("Passw0rd");
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
}
