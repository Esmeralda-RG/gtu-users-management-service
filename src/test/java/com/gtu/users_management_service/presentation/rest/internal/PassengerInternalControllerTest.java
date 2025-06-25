package com.gtu.users_management_service.presentation.rest.internal;

import com.gtu.users_management_service.domain.model.Passenger;
import com.gtu.users_management_service.domain.service.PassengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PassengerInternalControllerTest {

    @Mock
    private PassengerService passengerService;

    @InjectMocks
    private PassengerInternalController passengerInternalController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPassengerByEmail_shouldReturnPassengerWhenFound() {
        Passenger passenger = new Passenger(1L, "Juan Pérez", "juan.perez@gtu.com", "Passw0rd");
        when(passengerService.getPassengerByEmail("juan.perez@gtu.com")).thenReturn(passenger);

        ResponseEntity<Passenger> response = passengerInternalController.getPassengerByEmail("juan.perez@gtu.com");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(passenger, response.getBody());
        verify(passengerService).getPassengerByEmail("juan.perez@gtu.com");
    }

    @Test
    void getPassengerByEmail_shouldReturnEmptyPassengerWhenNotFound() {
        when(passengerService.getPassengerByEmail("notfound@gtu.com"))
                .thenThrow(new IllegalArgumentException("Passenger not found"));

        ResponseEntity<Passenger> response = passengerInternalController.getPassengerByEmail("notfound@gtu.com");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getId()); 
        verify(passengerService).getPassengerByEmail("notfound@gtu.com");
    }

    @Test
    void getPassengerByEmail_shouldReturnInternalServerErrorWhenOtherIllegalArgumentException() {
        when(passengerService.getPassengerByEmail("invalid@gtu.com"))
                .thenThrow(new IllegalArgumentException("Invalid input"));

        ResponseEntity<Passenger> response = passengerInternalController.getPassengerByEmail("invalid@gtu.com");

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(passengerService).getPassengerByEmail("invalid@gtu.com");
    }

    @Test
    void getPassengerByEmail_shouldReturnInternalServerErrorWhenGenericException() {
        when(passengerService.getPassengerByEmail("error@gtu.com"))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<Passenger> response = passengerInternalController.getPassengerByEmail("error@gtu.com");

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(passengerService).getPassengerByEmail("error@gtu.com");
    }

    @Test
    void resetPassword_shouldReturnUpdatedPassengerWhenSuccessful() {
        Passenger passenger = new Passenger(1L, "Juan Pérez", "juan.perez@gtu.com", "OldPass");
        when(passengerService.resetPassword(any(Passenger.class), eq("NewPass1"))).thenReturn(passenger);

        Passenger result = passengerInternalController.resetPassword(1L, "NewPass1");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Juan Pérez", result.getName());
        verify(passengerService).resetPassword(any(Passenger.class), eq("NewPass1"));
    }

    @Test
    void resetPassword_shouldThrowExceptionWhenServiceFails() {
        when(passengerService.resetPassword(any(Passenger.class), eq("NewPass1")))
                .thenThrow(new IllegalArgumentException("Invalid password"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            passengerInternalController.resetPassword(1L, "NewPass1"));

        assertEquals("Invalid password", exception.getMessage());
        verify(passengerService).resetPassword(any(Passenger.class), eq("NewPass1"));
    }
}