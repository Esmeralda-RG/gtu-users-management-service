package com.gtu.users_management_service.presentation.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtu.users_management_service.application.dto.PasswordUpdateDTO;
import com.gtu.users_management_service.application.dto.UserDTO;
import com.gtu.users_management_service.application.usecase.UserUseCase;
import com.gtu.users_management_service.domain.exception.ResourceNotFoundException;
import com.gtu.users_management_service.domain.model.Role;
import com.gtu.users_management_service.domain.model.Status;
import com.gtu.users_management_service.presentation.exception.GlobalExceptionHandler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
        private MockMvc mockMvc;

        @Mock
        private UserUseCase userUseCase;

        @InjectMocks
        private UserController userController;

        private ObjectMapper objectMapper;

        private UserDTO userDTO;
        private PasswordUpdateDTO passwordUpdateDTO;

        @BeforeEach
        void setUp() {
                objectMapper = new ObjectMapper();

                mockMvc = MockMvcBuilders.standaloneSetup(userController)
                                .setControllerAdvice(new GlobalExceptionHandler())
                                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                                .build();

                userDTO = new UserDTO();
                userDTO.setId(1L);
                userDTO.setName("Carlos Pérez");
                userDTO.setEmail("carlos.perez@gtu.com");
                userDTO.setPassword("Passw0rd");
                userDTO.setRole(Role.ADMIN);
                userDTO.setStatus(Status.ACTIVE);

                passwordUpdateDTO = new PasswordUpdateDTO();
                passwordUpdateDTO.setCurrentPassword("Passw0rd");
                passwordUpdateDTO.setNewPassword("NewPassw0rd");
        }

        @Test
        void shouldCreateUser_WhenValidInputProvided() throws Exception {
                when(userUseCase.createUser(any(UserDTO.class))).thenReturn(userDTO);

                mockMvc.perform(post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.message").value("User created successfully"))
                                .andExpect(jsonPath("$.data.id").value(userDTO.getId()))
                                .andExpect(jsonPath("$.data.name").value(userDTO.getName()))
                                .andExpect(jsonPath("$.data.email").value(userDTO.getEmail()))
                                .andExpect(jsonPath("$.data.role").value(userDTO.getRole().name()))
                                .andExpect(jsonPath("$.data.status").value(userDTO.getStatus().name()));

                verify(userUseCase, times(1)).createUser(any(UserDTO.class));
        }

        @Test
        void shouldReturnBadRequest_WhenInvalidUserDataProvided() throws Exception {
                when(userUseCase.createUser(any(UserDTO.class)))
                                .thenThrow(new IllegalArgumentException("Invalid user data"));

                mockMvc.perform(post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Invalid user data"))
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(jsonPath("$.data").doesNotExist());

                verify(userUseCase, times(1)).createUser(any(UserDTO.class));
        }

        @Test
        void shouldDeleteUser_WhenValidIdProvided() throws Exception {
                doNothing().when(userUseCase).deleteUser(1L);

                mockMvc.perform(delete("/users/1"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.message").value("User deleted successfully"))
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.data").doesNotExist());

                verify(userUseCase, times(1)).deleteUser(1L);
        }

        @Test
        void shouldReturnBadRequest_WhenUserDoesNotExist() throws Exception {
                doThrow(new IllegalArgumentException("User does not exist")).when(userUseCase).deleteUser(1L);

                mockMvc.perform(delete("/users/1"))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.message").value("User does not exist"))
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(jsonPath("$.data").doesNotExist());

                verify(userUseCase, times(1)).deleteUser(1L);
        }

        @Test
        void updateUserStatus_Success() throws Exception {
                userDTO.setStatus(Status.INACTIVE);
                when(userUseCase.updateStatus(1L, Status.INACTIVE)).thenReturn(userDTO);

                mockMvc.perform(put("/users/1/status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(Status.INACTIVE)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("User status updated successfully"))
                                .andExpect(jsonPath("$.data.id").value(userDTO.getId()))
                                .andExpect(jsonPath("$.data.name").value(userDTO.getName()))
                                .andExpect(jsonPath("$.data.email").value(userDTO.getEmail()))
                                .andExpect(jsonPath("$.data.role").value(userDTO.getRole().name()))
                                .andExpect(jsonPath("$.data.status").value(userDTO.getStatus().name()));

                verify(userUseCase, times(1)).updateStatus(1L, Status.INACTIVE);
        }

        @Test
        void updateUserStatus_Failure() throws Exception {
                doThrow(new IllegalArgumentException("Invalid status")).when(userUseCase).updateStatus(1L,
                                Status.INACTIVE);

                mockMvc.perform(put("/users/1/status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(Status.INACTIVE)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Invalid status"))
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(jsonPath("$.data").doesNotExist());

                verify(userUseCase, times(1)).updateStatus(1L, Status.INACTIVE);
        }

        @Test
        void getUsersByRole_Success() throws Exception {
                when(userUseCase.getUsersByRole(Role.ADMIN)).thenReturn(List.of(userDTO));

                mockMvc.perform(get("/users")
                                .param("role", "ADMIN")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("Users retrieved successfully"))
                                .andExpect(jsonPath("$.data[0].id").value(userDTO.getId()))
                                .andExpect(jsonPath("$.data[0].name").value(userDTO.getName()))
                                .andExpect(jsonPath("$.data[0].email").value(userDTO.getEmail()))
                                .andExpect(jsonPath("$.data[0].role").value(userDTO.getRole().name()))
                                .andExpect(jsonPath("$.data[0].status").value(userDTO.getStatus().name()));

                verify(userUseCase, times(1)).getUsersByRole(Role.ADMIN);
        }

        @Test
        void shouldReturnEmptyList_WhenNoUsersMatchRole() throws Exception {
                when(userUseCase.getUsersByRole(Role.DRIVER)).thenReturn(List.of());

                mockMvc.perform(get("/users")
                                .param("role", "DRIVER")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("Users retrieved successfully"))
                                .andExpect(jsonPath("$.data").isEmpty());

                verify(userUseCase, times(1)).getUsersByRole(Role.DRIVER);
        }

        @Test
        void shouldUpdatePassword_WhenCurrentPasswordIsValid() throws Exception {
                UserDTO updateduserDTO = new UserDTO();
                updateduserDTO.setId(1L);
                updateduserDTO.setName("John Doe");
                updateduserDTO.setEmail("johndoe@example.com");
                updateduserDTO.setPassword("NewPassw0rd");

                when(userUseCase.updatePassword(any(UserDTO.class), any(PasswordUpdateDTO.class)))
                                .thenReturn(updateduserDTO);

                mockMvc.perform(put("/users/1/password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(passwordUpdateDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("User password updated successfully"))
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.data.id").value(updateduserDTO.getId()))
                                .andExpect(jsonPath("$.data.name").value(updateduserDTO.getName()))
                                .andExpect(jsonPath("$.data.email").value(updateduserDTO.getEmail()))
                                .andExpect(jsonPath("$.data.password").value("NewPassw0rd"));

                verify(userUseCase, times(1)).updatePassword(any(UserDTO.class), any(PasswordUpdateDTO.class));
        }

        @Test
        void shouldUpdatePassword_WhenCurrentPasswordIsInvalid() throws Exception {
                when(userUseCase.updatePassword(any(UserDTO.class), any(PasswordUpdateDTO.class)))
                                .thenThrow(new IllegalArgumentException("Invalid current password"));

                mockMvc.perform(put("/users/1/password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(passwordUpdateDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Invalid current password"));

                verify(userUseCase, times(1)).updatePassword(any(UserDTO.class), any(PasswordUpdateDTO.class));
        }

        @Test
        void getUserById_Success() throws Exception {
                when(userUseCase.getUserById(1L)).thenReturn(userDTO);

                mockMvc.perform(get("/users/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("User retrieved successfully"))
                                .andExpect(jsonPath("$.data.id").value(userDTO.getId()))
                                .andExpect(jsonPath("$.data.name").value(userDTO.getName()))
                                .andExpect(jsonPath("$.data.email").value(userDTO.getEmail()))
                                .andExpect(jsonPath("$.data.role").value(userDTO.getRole().name()))
                                .andExpect(jsonPath("$.data.status").value(userDTO.getStatus().name()));

                verify(userUseCase, times(1)).getUserById(1L);
        }

        @Test
        void getUserById_NotFound() throws Exception {
                when(userUseCase.getUserById(1L)).thenThrow(new ResourceNotFoundException("User not found"));

                mockMvc.perform(get("/users/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("User not found"));

                verify(userUseCase, times(1)).getUserById(1L);
        }

        @Test
        void getUserById_InvalidId() throws Exception {
                when(userUseCase.getUserById(1L)).thenThrow(new IllegalArgumentException("Invalid user ID"));

                mockMvc.perform(get("/users/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Invalid user ID"));

                verify(userUseCase, times(1)).getUserById(1L);
        }

        @Test
        void shouldReturnBadRequest_WhenCreatingUserWithInvalidData() throws Exception {
                userDTO.setEmail(null);

                mockMvc.perform(post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userDTO)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturnBadRequest_WhenStatusIsNull() throws Exception {
                mockMvc.perform(put("/users/1/status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("null"))
                                .andExpect(status().isBadRequest());
        }
}
