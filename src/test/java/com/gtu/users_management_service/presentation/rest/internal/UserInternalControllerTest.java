package com.gtu.users_management_service.presentation.rest.internal;

import com.gtu.users_management_service.domain.model.User;
import com.gtu.users_management_service.domain.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserInternalControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserInternalController userInternalController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserByEmail_shouldReturnUserWhenFound() {
        User user = new User(1L, "Carlos Pérez", "carlos.perez@gtu.com", "Passw0rd", null, null);
        when(userService.getUserByEmail("carlos.perez@gtu.com")).thenReturn(user);

        ResponseEntity<User> response = userInternalController.getUserByEmail("carlos.perez@gtu.com");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService).getUserByEmail("carlos.perez@gtu.com");
    }

    @Test
    void getUserByEmail_shouldReturnEmptyUserWhenNotFound() {
        when(userService.getUserByEmail("notfound@gtu.com"))
                .thenThrow(new IllegalArgumentException("User does not exist"));

        ResponseEntity<User> response = userInternalController.getUserByEmail("notfound@gtu.com");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getId()); // Verifica que es un User vacío
        verify(userService).getUserByEmail("notfound@gtu.com");
    }

    @Test
    void getUserByEmail_shouldReturnInternalServerErrorWhenOtherIllegalArgumentException() {
        when(userService.getUserByEmail("invalid@gtu.com"))
                .thenThrow(new IllegalArgumentException("Invalid input"));

        ResponseEntity<User> response = userInternalController.getUserByEmail("invalid@gtu.com");

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).getUserByEmail("invalid@gtu.com");
    }

    @Test
    void getUserByEmail_shouldReturnInternalServerErrorWhenGenericException() {
        when(userService.getUserByEmail("error@gtu.com"))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<User> response = userInternalController.getUserByEmail("error@gtu.com");

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).getUserByEmail("error@gtu.com");
    }

    @Test
    void resetPassword_shouldReturnUpdatedUserWhenSuccessful() {
        User user = new User(1L, "Carlos Pérez", "carlos.perez@gtu.com", "OldPass", null, null);
        when(userService.resetPassword(any(User.class), eq("NewPass1"))).thenReturn(user);

        User result = userInternalController.resetPassword(1L, "NewPass1");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Carlos Pérez", result.getName());
        verify(userService).resetPassword(any(User.class), eq("NewPass1"));
    }

    @Test
    void resetPassword_shouldThrowExceptionWhenServiceFails() {
        when(userService.resetPassword(any(User.class), eq("NewPass1")))
                .thenThrow(new IllegalArgumentException("Invalid password"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            userInternalController.resetPassword(1L, "NewPass1"));

        assertEquals("Invalid password", exception.getMessage());
        verify(userService).resetPassword(any(User.class), eq("NewPass1"));
    }
}