package com.gtu.users_management_service.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gtu.users_management_service.application.dto.PasswordUpdateDTO;
import com.gtu.users_management_service.application.dto.UserDTO;
import com.gtu.users_management_service.domain.model.Role;
import com.gtu.users_management_service.domain.model.Status;
import com.gtu.users_management_service.domain.model.User;
import com.gtu.users_management_service.domain.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserUseCase userUseCase;

    private User user;
    private UserDTO userDto;
    private PasswordUpdateDTO passwordUpdateDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Carlos Pérez");
        user.setEmail("carlos.perez@gtu.com");
        user.setPassword("Passw0rd");
        user.setRole(Role.ADMIN);
        user.setStatus(Status.ACTIVE);

        userDto = new UserDTO();
        userDto.setId(1L);
        userDto.setName("Carlos Pérez");
        userDto.setEmail("carlos.perez@gtu.com");
        userDto.setPassword("Passw0rd");
        userDto.setRole(Role.ADMIN);
        userDto.setStatus(Status.ACTIVE);

        passwordUpdateDTO = new PasswordUpdateDTO();
        passwordUpdateDTO.setCurrentPassword("Passw0rd");
        passwordUpdateDTO.setNewPassword("NewPassw0rd");
    }

    @Test
    void createUser_Success() {
        when(userService.createUser(any(User.class))).thenReturn(user);
        UserDTO result = userUseCase.createUser(userDto);

        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());
        assertEquals(userDto.getPassword(), result.getPassword());
        assertEquals(userDto.getRole(), result.getRole());
        assertEquals(userDto.getStatus(), result.getStatus());

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void createUser_UserNotFound_ThrowsException() {
        when(userService.createUser(any(User.class))).thenThrow(new IllegalArgumentException("User not found"));

        try {
            userUseCase.createUser(userDto);
        } catch (IllegalArgumentException e) {
            assertEquals("User not found", e.getMessage());
        }

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void createUser_UserServiceThrowsException() {
        when(userService.createUser(any(User.class))).thenThrow(new IllegalArgumentException("Email is already in use"));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userUseCase.createUser(userDto);
        });
        assertEquals("Email is already in use", exception.getMessage());

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void deleteUser_Success() {
        doNothing().when(userService).deleteUser(1L);
        userUseCase.deleteUser(1L);
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void deleteUser_UserServiceThrowsException() {
        doThrow(new IllegalArgumentException("User does not exist")).when(userService).deleteUser(1L);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userUseCase.deleteUser(1L);
        });
        assertEquals("User does not exist", exception.getMessage());
        verify(userService, times(1)).deleteUser(1L);
    }
    
    @Test
    void updateStatus_Success() {
        when(userService.updateStatus(1L, Status.INACTIVE)).thenReturn(user);
        user.setStatus(Status.INACTIVE);
        userDto.setStatus(Status.INACTIVE);

        UserDTO result = userUseCase.updateStatus(1L, Status.INACTIVE);

        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());
        assertEquals(userDto.getPassword(), result.getPassword());
        assertEquals(userDto.getRole(), result.getRole());
        assertEquals(Status.INACTIVE, result.getStatus());

        verify(userService, times(1)).updateStatus(1L, Status.INACTIVE);
    }

    @Test
    void updateStatus_UserServiceThrowsException() {
        doThrow(new IllegalArgumentException("User does not exist")).when(userService).updateStatus(1L, Status.INACTIVE);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userUseCase.updateStatus(1L, Status.INACTIVE);
        });

        assertEquals("User does not exist", exception.getMessage());
        verify(userService, times(1)).updateStatus(1L, Status.INACTIVE);
    }

    @Test
    void getUsersByRole_Success() {
        when(userService.getUsersByRole(Role.ADMIN)).thenReturn(List.of(user));
        List<UserDTO> result = userUseCase.getUsersByRole(Role.ADMIN);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userDto.getId(), result.get(0).getId());
        assertEquals(userDto.getName(), result.get(0).getName());
        assertEquals(userDto.getEmail(), result.get(0).getEmail());
        assertEquals(userDto.getPassword(), result.get(0).getPassword());
        assertEquals(userDto.getRole(), result.get(0).getRole());
        assertEquals(userDto.getStatus(), result.get(0).getStatus());

        verify(userService, times(1)).getUsersByRole(Role.ADMIN);
    }

    @Test
    void getUsersByRole_ReturnsEmptyList_WhenNoUsersFound() {
        when(userService.getUsersByRole(Role.DRIVER)).thenReturn(List.of());
        List<UserDTO> result = userUseCase.getUsersByRole(Role.DRIVER);

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(userService, times(1)).getUsersByRole(Role.DRIVER);
    }

    @Test
    void getUsersByRole_ThrowsException_WhenRoleIsInvalid() {
        when(userService.getUsersByRole(null)).thenThrow(new IllegalArgumentException("Invalid role"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userUseCase.getUsersByRole(null);
        });

        assertEquals("Invalid role", exception.getMessage());
        verify(userService, times(1)).getUsersByRole(null);
    }

    @Test
    void updatePassword_Success() {
        user.setPassword("NewPassw0rd");
        when(userService.updatePassword(any(User.class), any(PasswordUpdateDTO.class))).thenReturn(user);
        UserDTO result = userUseCase.updatePassword(userDto, passwordUpdateDTO);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals("NewPassw0rd", result.getPassword());

        verify(userService, times(1)).updatePassword(any(User.class), any(PasswordUpdateDTO.class));
    }

    @Test
    void updatePassword_InvalidCurrentPassword() {
        when(userService.updatePassword(any(User.class), any(PasswordUpdateDTO.class)))
                .thenThrow(new IllegalArgumentException("Invalid current password"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userUseCase.updatePassword(userDto, passwordUpdateDTO);
        });
        assertEquals("Invalid current password", exception.getMessage());

        verify(userService, times(1)).updatePassword(any(User.class), any(PasswordUpdateDTO.class));
    }

    @Test
    void getUserById_Success() {
        when(userService.getUserById(1L)).thenReturn(user);
        UserDTO result = userUseCase.getUserById(1L);

        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());
        assertEquals(userDto.getPassword(), result.getPassword());
        assertEquals(userDto.getRole(), result.getRole());
        assertEquals(userDto.getStatus(), result.getStatus());

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void getUserById_UserNotFound_ThrowsException() {
        when(userService.getUserById(1L)).thenThrow(new IllegalArgumentException("User not found"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userUseCase.getUserById(1L);
        });
        assertEquals("User not found", exception.getMessage());

        verify(userService, times(1)).getUserById(1L);
    }
}
