package com.gtu.users_management_service.presentation.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
import com.gtu.users_management_service.application.dto.UserDTO;
import com.gtu.users_management_service.application.usecase.UserUseCase;
import com.gtu.users_management_service.domain.model.Role;
import com.gtu.users_management_service.domain.model.Status;
import com.gtu.users_management_service.presentation.exception.GlobalExceptionHandler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserUseCase userUseCase;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    private UserDTO userDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        userDto = new UserDTO();
        userDto.setId(1L);
        userDto.setName("Carlos Pérez");
        userDto.setEmail("carlos.perez@gtu.com");
        userDto.setPassword("Passw0rd");
        userDto.setRole(Role.ADMIN);
        userDto.setStatus(Status.ACTIVE);
    }

    @Test
    void createUser_Success() throws Exception {
        when(userUseCase.createUser(any(UserDTO.class))).thenReturn(userDto);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User created successfully"))
                .andExpect(jsonPath("$.data.id").value(userDto.getId()))
                .andExpect(jsonPath("$.data.name").value(userDto.getName()))
                .andExpect(jsonPath("$.data.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.data.role").value(userDto.getRole().name()))
                .andExpect(jsonPath("$.data.status").value(userDto.getStatus().name()));

        verify(userUseCase, times(1)).createUser(any(UserDTO.class));
    }

    @Test
    void deleteUser_Success() throws Exception {
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
    void updateUserStatus_Success() throws Exception {
        userDto.setStatus(Status.INACTIVE);
        when(userUseCase.updateStatus(1L, Status.INACTIVE)).thenReturn(userDto);

        mockMvc.perform(put("/users/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Status.INACTIVE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User status updated successfully"))
                .andExpect(jsonPath("$.data.id").value(userDto.getId()))
                .andExpect(jsonPath("$.data.name").value(userDto.getName()))
                .andExpect(jsonPath("$.data.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.data.role").value(userDto.getRole().name()))
                .andExpect(jsonPath("$.data.status").value(userDto.getStatus().name()));

        verify(userUseCase, times(1)).updateStatus(1L, Status.INACTIVE);
    }
}
