package com.gtu.users_management_service.application.service;

import com.gtu.users_management_service.application.dto.PasswordUpdateDTO;
import com.gtu.users_management_service.domain.model.Passenger;
import com.gtu.users_management_service.domain.repository.PassengerRepository;
import com.gtu.users_management_service.infrastructure.logs.LogPublisher;
import com.gtu.users_management_service.infrastructure.security.PasswordEncoderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PassengerServiceImplTest {

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private LogPublisher logPublisher;

    @InjectMocks
    private PassengerServiceImpl passengerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPassenger_shouldCreateWhenDataIsValid() {
        Passenger passenger = new Passenger(null, "Test", "test@example.com", "Password1");
        when(passengerRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passengerRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Passenger result = passengerService.createPassenger(passenger);

        assertNotNull(result);
        verify(passengerRepository).save(any());
        verify(logPublisher).sendLog(anyString(), eq("users-management-service"), eq("INFO"), eq("Creating passenger"),
                argThat(map -> map.get("name").equals("Test") && map.get("email").equals("test@example.com")));
    }

    @Test
    void createPassenger_shouldThrowWhenPassengerIsNull() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.createPassenger(null));
        assertEquals("Passenger cannot be null", ex.getMessage());
    }

    @Test
    void createPassenger_shouldThrowWhenNameIsNull() {
        Passenger passenger = new Passenger(null, null, "test@example.com", "Password1");
        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.createPassenger(passenger));
        assertEquals("Name cannot be null or empty", ex.getMessage());
    }

    @Test
    void createPassenger_shouldThrowWhenEmailIsNull() {
        Passenger passenger = new Passenger(null, "Test", null, "Password1");
        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.createPassenger(passenger));
        assertEquals("Email cannot be null or empty", ex.getMessage());
    }

    @Test
    void createPassenger_shouldThrowWhenEmailAlreadyExists() {
        Passenger passenger = new Passenger(null, "Test", "test@example.com", "Password1");
        when(passengerRepository.existsByEmail("test@example.com")).thenReturn(true);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.createPassenger(passenger));
        assertEquals("Email already exists", ex.getMessage());
    }

    @Test
    void createPassenger_shouldThrowWhenPasswordIsInvalid() {
        Passenger passenger = new Passenger(null, "Test", "test@example.com", "short");
        when(passengerRepository.existsByEmail("test@example.com")).thenReturn(false);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.createPassenger(passenger));
        assertTrue(ex.getMessage().contains("Password must contain"));
    }

    @Test
    void updatePassenger_shouldUpdateWhenDataIsValid() {
        Passenger existing = new Passenger(1L, "Original", "old@example.com", "pass");
        Passenger updates = new Passenger(1L, "Updated", "new@example.com", null);
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(passengerRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passengerRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Passenger result = passengerService.updatePassenger(updates);

        assertEquals("Updated", result.getName());
        assertEquals("new@example.com", result.getEmail());
        verify(logPublisher).sendLog(anyString(), eq("users-management-service"), eq("INFO"), eq("Updating passenger"),
                argThat(map -> map.get("id").equals(1L) && map.get("name").equals("Updated") && map.get("email").equals("new@example.com")));
    }

    @Test
    void updatePassenger_shouldThrowWhenPassengerNotFound() {
        Passenger passenger = new Passenger(1L, "Updated", "new@example.com", null);
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.updatePassenger(passenger));
        assertEquals("Passenger not found", ex.getMessage());
    }

    @Test
    void updatePassenger_shouldThrowWhenEmailAlreadyExists() {
        Passenger existing = new Passenger(1L, "Original", "old@example.com", "pass");
        Passenger updates = new Passenger(1L, "Updated", "existing@example.com", null);
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(passengerRepository.existsByEmail("existing@example.com")).thenReturn(true);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.updatePassenger(updates));
        assertEquals("Email already exists", ex.getMessage());
    }

    @Test
    void updatePassenger_shouldThrowWhenTryingToUpdatePassword() {
        Passenger passenger = new Passenger(1L, "Test", "test@example.com", "newPass");
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.updatePassenger(passenger));
        assertEquals("Password cannot be updated", ex.getMessage());
    }

    @Test
    void updatePassword_shouldUpdateWhenCurrentIsCorrectAndNewIsValid() {
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
    void updatePassword_shouldThrowWhenPassengerNotFound() {
        PasswordUpdateDTO dto = new PasswordUpdateDTO("OldPass1", "NewPass1");
        Passenger request = new Passenger(1L, null, null, "OldPass1");
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.updatePassword(request, dto));
        assertEquals("Passenger not found", ex.getMessage());
    }

    @Test
    void updatePassword_shouldThrowWhenCurrentPasswordIsNull() {
        PasswordUpdateDTO dto = new PasswordUpdateDTO(null, "NewPass1");
        Passenger request = new Passenger(1L, null, null, null);
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(new Passenger(1L, "Test", "test@example.com", "OldPass1")));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.updatePassword(request, dto));
        assertEquals("Current password cannot be null or empty", ex.getMessage());
    }

    @Test
    void updatePassword_shouldThrowWhenCurrentPasswordIsIncorrect() {
        Passenger existing = new Passenger(1L, "Test", "test@example.com", PasswordEncoderUtil.encode("RightPass"));
        PasswordUpdateDTO dto = new PasswordUpdateDTO("WrongPass", "NewPass1");
        Passenger request = new Passenger(1L, null, null, "WrongPass");

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(existing));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.updatePassword(request, dto));
        assertEquals("Current password is incorrect", ex.getMessage());
    }

    @Test
    void updatePassword_shouldThrowWhenNewPasswordIsNull() {
        Passenger existing = new Passenger(1L, "Test", "test@example.com", PasswordEncoderUtil.encode("OldPass1"));
        PasswordUpdateDTO dto = new PasswordUpdateDTO("OldPass1", null);
        Passenger request = new Passenger(1L, null, null, "OldPass1");

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(existing));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.updatePassword(request, dto));
        assertEquals("New password cannot be null or empty", ex.getMessage());
    }

    @Test
    void updatePassword_shouldThrowWhenNewPasswordIsInvalid() {
        Passenger existing = new Passenger(1L, "Test", "test@example.com", PasswordEncoderUtil.encode("OldPass1"));
        PasswordUpdateDTO dto = new PasswordUpdateDTO("OldPass1", "short");
        Passenger request = new Passenger(1L, null, null, "OldPass1");

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(existing));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.updatePassword(request, dto));
        assertTrue(ex.getMessage().contains("must contain at least"));
    }

    @Test
    void resetPassword_shouldResetWhenNewPasswordIsValid() {
        Passenger passenger = new Passenger(1L, "Test", "test@example.com", null);
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(passengerRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Passenger result = passengerService.resetPassword(passenger, "ValidPass1");

        assertNotNull(result);
        verify(passengerRepository).save(any());
    }

    @Test
    void resetPassword_shouldThrowWhenPassengerNotFound() {
        Passenger passenger = new Passenger(1L, "Test", "test@example.com", null);
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.resetPassword(passenger, "ValidPass1"));
        assertEquals("Passenger not found", ex.getMessage());
    }

    @Test
    void resetPassword_shouldThrowWhenNewPasswordIsNull() {
        Passenger passenger = new Passenger(1L, "Test", "test@example.com", null);
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.resetPassword(passenger, null));
        assertEquals("New password cannot be null or empty", ex.getMessage());
    }

    @Test
    void resetPassword_shouldThrowWhenNewPasswordIsInvalid() {
        Passenger passenger = new Passenger(1L, "Test", "test@example.com", null);
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.resetPassword(passenger, "short"));
        assertTrue(ex.getMessage().contains("must contain at least"));
    }

    @Test
    void countPassengers_shouldReturnCount() {
        when(passengerRepository.count()).thenReturn(10L);
        assertEquals(10L, passengerService.countPassengers());
    }

    @Test
    void getPassengerByEmail_shouldReturnPassenger() {
        Passenger passenger = new Passenger(1L, "Test", "email@example.com", "pass");
        when(passengerRepository.findByEmail("email@example.com")).thenReturn(Optional.of(passenger));

        Passenger result = passengerService.getPassengerByEmail("email@example.com");

        assertNotNull(result);
        assertEquals("email@example.com", result.getEmail());
    }

    @Test
    void getPassengerByEmail_shouldThrowWhenNotFound() {
        when(passengerRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> passengerService.getPassengerByEmail("notfound@example.com"));
        assertEquals("Passenger not found", ex.getMessage());
    }
}