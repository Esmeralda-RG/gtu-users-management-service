package com.gtu.users_management_service.application.service;

import com.gtu.users_management_service.application.dto.PasswordUpdateDTO;
import com.gtu.users_management_service.domain.model.Passenger;
import com.gtu.users_management_service.domain.repository.PassengerRepository;
import com.gtu.users_management_service.infrastructure.security.PasswordEncoderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PassengerServiceImplTest {

    private PassengerRepository passengerRepository;
    private PassengerServiceImpl passengerService;

    @BeforeEach
    void setUp() {
        passengerRepository = mock(PassengerRepository.class);
        passengerService = new PassengerServiceImpl(passengerRepository);
    }

    @Test
    void shouldCreatePassenger_whenDataIsValid() {
        Passenger passenger = new Passenger(null, "Test", "test@example.com", "Password1");
        when(passengerRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passengerRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Passenger result = passengerService.createPassenger(passenger);

        assertNotNull(result);
        verify(passengerRepository).save(any());
    }

    @Test
    void shouldThrow_whenNameIsNullOnCreate() {
        Passenger passenger = new Passenger(null, null, "test@example.com", "Password1");
        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.createPassenger(passenger));
        assertEquals("Name cannot be null or empty", ex.getMessage());
    }

    @Test
    void shouldThrow_whenEmailAlreadyExistsOnCreate() {
        Passenger passenger = new Passenger(null, "Test", "test@example.com", "Password1");
        when(passengerRepository.existsByEmail("test@example.com")).thenReturn(true);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.createPassenger(passenger));
        assertEquals("Email already exists", ex.getMessage());
    }

    @Test
    void shouldThrow_whenPasswordIsInvalidOnCreate() {
        Passenger passenger = new Passenger(null, "Test", "test@example.com", "short");
        when(passengerRepository.existsByEmail("test@example.com")).thenReturn(false);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.createPassenger(passenger));
        assertTrue(ex.getMessage().contains("Password must contain"));
    }

    @Test
    void shouldUpdatePassenger_whenDataIsValid() {
        Passenger existing = new Passenger(1L, "Original", "old@example.com", "pass");
        Passenger updates = new Passenger(1L, "Updated", "new@example.com", null);
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(passengerRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passengerRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Passenger result = passengerService.updatePassenger(updates);

        assertEquals("Updated", result.getName());
        assertEquals("new@example.com", result.getEmail());
    }

    @Test
    void shouldThrow_whenPassengerNotFoundOnUpdate() {
        Passenger passenger = new Passenger(1L, "Updated", "new@example.com", null);
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.updatePassenger(passenger));
        assertEquals("Passenger not found", ex.getMessage());
    }

    @Test
    void shouldThrow_whenTryingToUpdatePasswordDirectly() {
        Passenger passenger = new Passenger(1L, "Test", "test@example.com", "newPass");
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.updatePassenger(passenger));
        assertEquals("Password cannot be updated", ex.getMessage());
    }

    @Test
    void shouldUpdatePassword_whenCurrentIsCorrectAndNewIsValid() {
        Passenger existing = new Passenger(1L, "Test", "test@example.com", PasswordEncoderUtil.encode("OldPass1"));
        PasswordUpdateDTO dto = new PasswordUpdateDTO("OldPass1", "NewPass1");
        Passenger request = new Passenger(1L, null, null, "OldPass1");

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(passengerRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Passenger result = passengerService.updatePassword(request, dto);

        assertNotNull(result);
        verify(passengerRepository).save(any());
    }

    @Test
    void shouldThrow_whenCurrentPasswordIsIncorrect() {
        Passenger existing = new Passenger(1L, "Test", "test@example.com", PasswordEncoderUtil.encode("RightPass"));
        PasswordUpdateDTO dto = new PasswordUpdateDTO("WrongPass", "NewPass1");
        Passenger request = new Passenger(1L, null, null, "WrongPass");

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(existing));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.updatePassword(request, dto));
        assertEquals("Current password is incorrect", ex.getMessage());
    }

    @Test
    void shouldResetPassword_whenNewPasswordIsValid() {
        Passenger passenger = new Passenger(1L, "Test", "test@example.com", null);
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(passengerRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Passenger result = passengerService.resetPassword(passenger, "ValidPass1");

        assertNotNull(result);
        verify(passengerRepository).save(any());
    }

    @Test
    void shouldThrow_whenNewPasswordIsInvalidInReset() {
        Passenger passenger = new Passenger(1L, "Test", "test@example.com", null);
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.resetPassword(passenger, "short"));
        assertTrue(ex.getMessage().contains("must contain at least"));
    }

    @Test
    void shouldReturnPassengerCount() {
        when(passengerRepository.count()).thenReturn(10L);
        assertEquals(10L, passengerService.countPassengers());
    }

    @Test
    void shouldReturnPassengerByEmail() {
        Passenger passenger = new Passenger(1L, "Test", "email@example.com", "pass");
        when(passengerRepository.findByEmail("email@example.com")).thenReturn(Optional.of(passenger));

        Passenger result = passengerService.getPassengerByEmail("email@example.com");

        assertNotNull(result);
        assertEquals("email@example.com", result.getEmail());
    }

    @Test
    void shouldThrow_whenPassengerNotFoundByEmail() {
        when(passengerRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                passengerService.getPassengerByEmail("notfound@example.com"));
        assertEquals("Passenger not found", ex.getMessage());
    }
}
