package com.gtu.users_management_service.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gtu.users_management_service.application.dto.PasswordUpdateDTO;
import com.gtu.users_management_service.domain.exception.ResourceNotFoundException;
import com.gtu.users_management_service.domain.model.Role;
import com.gtu.users_management_service.domain.model.Status;
import com.gtu.users_management_service.domain.model.User;
import com.gtu.users_management_service.domain.repository.UserRepository;
import com.gtu.users_management_service.infrastructure.security.PasswordEncoderUtil;
import com.gtu.users_management_service.infrastructure.security.PasswordValidator;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

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
       
    }

    @Test
    void createUser_ThrowsException_WhenNameIsEmpty() {
        user.setName("");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(user));
        assertEquals("User name cannot be empty", exception.getMessage());
    }

    @Test
    void createUser_ThrowsException_WhenEmailIsEmpty() {
        user.setEmail("");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(user));
        assertEquals("Email cannot be empty", exception.getMessage());
    }

    @Test
    void createUser_ThrowsException_WhenPasswordIsInvalid() {
        user.setPassword("short");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(user));
        assertEquals("Password must contain at least 8 characters, including uppercase letters and numbers",
                exception.getMessage());
    }

    @Test
    void createUser_ThrowsException_WhenRoleIsNull() {
        user.setRole(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(user));
        assertEquals("Role cannot be null or invalid", exception.getMessage());
    }

    @Test
    void createUser_ThrowsException_WhenEmailAlreadyExists() {
        when(userRepository.existsByEmail("carlos.perez@gtu.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(user));
        assertEquals("Email is already in use", exception.getMessage());
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_ThrowsException_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteUser(1L));

        assertEquals("User does not exist", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(0)).deleteById(1L);
    }

    @Test
    void updateStatus_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = userService.updateStatus(1L, Status.INACTIVE);

        assertNotNull(updatedUser);
        assertEquals(Status.INACTIVE, updatedUser.getStatus());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateStatus_ThrowsException_WhenStatusIsInvalid() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateStatus(1L, null));

        assertEquals("Invalid status value. Only ACTIVE or INACTIVE are allowed.", exception.getMessage());
        verify(userRepository, times(0)).findById(1L);
        verify(userRepository, times(0)).save(user);
    }

    @Test
    void updateStatus_ThrowsException_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateStatus(1L, Status.ACTIVE));

        assertEquals("User does not exist", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(0)).save(user);
    }

    @Test
    void getUsersByRole_Success() {
        when(userRepository.findByRole(Role.ADMIN)).thenReturn(List.of(user));
        List<User> users = userService.getUsersByRole(Role.ADMIN);

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("Carlos Pérez", users.get(0).getName());
        verify(userRepository, times(1)).findByRole(Role.ADMIN);
    }

    @Test
    void getUsersByRole_ThrowsException_WhenRoleIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.getUsersByRole(null));
        assertEquals("Invalid role value. Only ADMIN or DRIVER are allowed.", exception.getMessage());
    }

    @Test
    void getUsersByRole_ReturnsEmptyList_WhenNoUsersFound() {
        when(userRepository.findByRole(Role.DRIVER)).thenReturn(List.of());
        List<User> users = userService.getUsersByRole(Role.DRIVER);

        assertNotNull(users);
        assertEquals(0, users.size());
        verify(userRepository, times(1)).findByRole(Role.DRIVER);
    }

    @Test
    void updatePassword_Success() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("John Doe");
        existingUser.setEmail("johndoe@example.com");
        existingUser.setPassword("encodedPassw0rd"); 

        PasswordUpdateDTO passwordUpdateDTO = new PasswordUpdateDTO();
        passwordUpdateDTO.setNewPassword("NewPassw0rd");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        try (MockedStatic<PasswordEncoderUtil> passwordEncoderUtilMock = Mockito.mockStatic(PasswordEncoderUtil.class);
            MockedStatic<PasswordValidator> passwordValidatorMock = Mockito.mockStatic(PasswordValidator.class)) {
            
            passwordEncoderUtilMock.when(() -> PasswordEncoderUtil.matches("Passw0rd", "encodedPassw0rd")).thenReturn(true);
            passwordValidatorMock.when(() -> PasswordValidator.isValid("NewPassw0rd")).thenReturn(true);
            passwordEncoderUtilMock.when(() -> PasswordEncoderUtil.encode("NewPassw0rd")).thenReturn("encodedNewPassw0rd");

            User result = userService.updatePassword(user, passwordUpdateDTO);

            assertNotNull(result);
            assertEquals("encodedNewPassw0rd", result.getPassword());
            verify(userRepository, times(1)).findById(1L);
            verify(userRepository, times(1)).save(any(User.class));
        }
    }

    @Test
    void updatePassword_PassengerNotFound() {
        PasswordUpdateDTO passwordUpdateDTO = new PasswordUpdateDTO();
        passwordUpdateDTO.setNewPassword("NewPassw0rd");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.updatePassword(user, passwordUpdateDTO);
        });

        assertEquals("Passenger not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void updatePassword_IncorrectCurrentPassword() {
        user.setPassword(PasswordEncoderUtil.encode("Passw0rd"));

        PasswordUpdateDTO passwordUpdateDTO = new PasswordUpdateDTO();
        passwordUpdateDTO.setNewPassword("NewPassw0rd");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        try (MockedStatic<PasswordEncoderUtil> passwordEncoderUtilMock = Mockito.mockStatic(PasswordEncoderUtil.class);
            MockedStatic<PasswordValidator> passwordValidatorMock = Mockito.mockStatic(PasswordValidator.class)) {
            passwordEncoderUtilMock.when(() -> PasswordEncoderUtil.matches("Passw0rd", user.getPassword())).thenReturn(false);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                userService.updatePassword(user, passwordUpdateDTO);
            });

            assertEquals("Current password is incorrect", exception.getMessage());
            verify(userRepository, times(1)).findById(1L);
            verify(userRepository, never()).save(any());
        }
    }

    @Test
    void updatePassword_NullNewPassword() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setPassword(PasswordEncoderUtil.encode("Passw0rd"));

        PasswordUpdateDTO passwordUpdateDTO = new PasswordUpdateDTO();
        passwordUpdateDTO.setNewPassword(null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        try (MockedStatic<PasswordEncoderUtil> passwordEncoderUtilMock = Mockito.mockStatic(PasswordEncoderUtil.class);
            MockedStatic<PasswordValidator> passwordValidatorMock = Mockito.mockStatic(PasswordValidator.class)) {
            passwordEncoderUtilMock.when(() -> PasswordEncoderUtil.matches("Passw0rd", existingUser.getPassword())).thenReturn(true);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                userService.updatePassword(user, passwordUpdateDTO);
            });
    
            assertEquals("New password cannot be null or empty", exception.getMessage());
            verify(userRepository, times(1)).findById(1L);
            verify(userRepository, never()).save(any());
        }   
    }

    @Test
    void updatePassword_InvalidNewPassword() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setPassword(PasswordEncoderUtil.encode("Passw0rd"));

        PasswordUpdateDTO passwordUpdateDTO = new PasswordUpdateDTO();
        passwordUpdateDTO.setNewPassword("invalid");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        try (MockedStatic<PasswordEncoderUtil> passwordEncoderUtilMock = Mockito.mockStatic(PasswordEncoderUtil.class);
            MockedStatic<PasswordValidator> passwordValidatorMock = Mockito.mockStatic(PasswordValidator.class)) {
            passwordEncoderUtilMock.when(() -> PasswordEncoderUtil.matches("Passw0rd", existingUser.getPassword())).thenReturn(true);
            passwordValidatorMock.when(() -> PasswordValidator.isValid("invalid")).thenReturn(false);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                userService.updatePassword(user, passwordUpdateDTO);
            });
            
            assertEquals("New password must contain at least 8 characters, including uppercase letters and numbers", exception.getMessage());
            verify(userRepository, times(1)).findById(1L);
            verify(userRepository, never()).save(any());
        }
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals("Carlos Pérez", foundUser.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_ThrowsException_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserById(1L));

        assertEquals("User does not exist", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }
}
