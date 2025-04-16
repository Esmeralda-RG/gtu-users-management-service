package com.gtu.users_management_service.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gtu.users_management_service.domain.model.Role;
import com.gtu.users_management_service.domain.model.Status;
import com.gtu.users_management_service.domain.model.User;
import com.gtu.users_management_service.domain.repository.UserRepository;
import com.gtu.users_management_service.infrastructure.email.EmailServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailServiceImpl emailService;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Carlos Pérez");
        user.setEmail("carlos.perez@gtu.com");
        user.setPassword("Passw0rd");
        user.setRole(Role.ADMIN);
        user.setStatus(Status.ACTIVE);
    }

    @Test
    void createUser_Success() {
        when(userRepository.existsByEmail("carlos.perez@gtu.com")).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("Carlos Pérez", createdUser.getName());

        verify(userRepository, times(1)).existsByEmail("carlos.perez@gtu.com");
        verify(userRepository, times(1)).save(user);
        verify(emailService, times(1)).sendCredentials(
                "carlos.perez@gtu.com",
                "Carlos Pérez",
                "Passw0rd");
    }

    @Test
    void createUser_ThrowsException_WhenNameIsEmpty() {
        user.setName("");

        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(user));
        assertEquals("User name cannot be empty", exception.getMessage());
    }

    @Test
    void createUser_ThrowsException_WhenEmailIsEmpty() {
        user.setEmail("");

        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(user));
        assertEquals("Email cannot be empty", exception.getMessage());
    }

    @Test
    void createUser_ThrowsException_WhenPasswordIsInvalid() {
        user.setPassword("short");

        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(user));
        assertEquals("Password must contain at least 8 characters, including uppercase letters and numbers",
                exception.getMessage());
    }

    @Test
    void createUser_ThrowsException_WhenRoleIsNull() {
        user.setRole(null);

        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(user));
        assertEquals("Role cannot be null", exception.getMessage());
    }

    @Test
    void createUser_ThrowsException_WhenEmailAlreadyExists() {
        when(userRepository.existsByEmail("carlos.perez@gtu.com")).thenReturn(true);

        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(user));
        assertEquals("Email is already in use", exception.getMessage());
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        userService.deleteUser(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_ThrowsException_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteUser(1L));

        assertEquals("User does not exist", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(0)).deleteById(1L);
    }

    @Test
    void updateStatus_Success() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = userService.updateStatus(1L, Status.INACTIVE);

        assertNotNull(updatedUser);
        assertEquals(Status.INACTIVE, updatedUser.getStatus());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateStatus_ThrowsException_WhenStatusIsInvalid() {
        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateStatus(1L, null));

        assertEquals("Invalid status value. Only ACTIVE or INACTIVE are allowed.", exception.getMessage());
        verify(userRepository, times(0)).findById(1L);
        verify(userRepository, times(0)).save(user);
    }

    @Test
    void updateStatus_ThrowsException_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateStatus(1L, Status.ACTIVE));

        assertEquals("User does not exist", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(0)).save(user);
    }

}
