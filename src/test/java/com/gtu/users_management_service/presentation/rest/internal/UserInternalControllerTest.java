package com.gtu.users_management_service.presentation.rest.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtu.users_management_service.domain.exception.ResourceNotFoundException;
import com.gtu.users_management_service.domain.model.Role;
import com.gtu.users_management_service.domain.model.Status;
import com.gtu.users_management_service.domain.model.User;
import com.gtu.users_management_service.domain.service.UserService;
import com.gtu.users_management_service.presentation.exception.GlobalExceptionHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserInternalControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserInternalController userInternalController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(userInternalController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void getUserByEmail_ReturnsUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Carlos Pérez");
        user.setEmail("carlos.perez@gtu.com");
        user.setRole(Role.ADMIN);
        user.setStatus(Status.ACTIVE);

        Mockito.when(userService.getUserByEmail("carlos.perez@gtu.com"))
               .thenReturn(user);

        mockMvc.perform(get("/internal/users")
                .param("email", "carlos.perez@gtu.com"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("Carlos Pérez")))
            .andExpect(jsonPath("$.email", is("carlos.perez@gtu.com")))
            .andExpect(jsonPath("$.role", is("ADMIN")))
            .andExpect(jsonPath("$.status", is("ACTIVE")));
    }

    @Test
    void getUserByEmail_ReturnsNotFound_WhenUserDoesNotExist() throws Exception {
        Mockito.when(userService.getUserByEmail("no.exists@gtu.com"))
            .thenThrow(new ResourceNotFoundException("User not found with email: no.exists@gtu.com"));

        mockMvc.perform(get("/internal/users")
                .param("email", "no.exists@gtu.com"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message").value("User not found with email: no.exists@gtu.com"))
            .andExpect(jsonPath("$.path").value("/internal/users"));
    }

    @Test
    void resetPassword_ReturnsUpdatedUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setPassword("NewSecurePass");

        Mockito.when(userService.resetPassword(any(User.class), eq("NewSecurePass")))
               .thenReturn(user);

        mockMvc.perform(put("/internal/users/1/reset-password")
                .param("newPassword", "NewSecurePass"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.password", is("NewSecurePass")));
    }

    @Test
    void resetPassword_ReturnsNotFound_WhenUserDoesNotExist() throws Exception {
        Mockito.when(userService.resetPassword(any(User.class), eq("noSuchPass")))
            .thenThrow(new ResourceNotFoundException("User not found with ID: 99"));

        mockMvc.perform(put("/internal/users/99/reset-password")
                .param("newPassword", "noSuchPass"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message").value("User not found with ID: 99"))
            .andExpect(jsonPath("$.path").value("/internal/users/99/reset-password"));
    }

    @Test
    void resetPassword_ReturnsBadRequest_WhenInvalidPassword() throws Exception {
        Mockito.when(userService.resetPassword(any(User.class), eq("123")))
            .thenThrow(new IllegalArgumentException("Password is too weak"));

        mockMvc.perform(put("/internal/users/1/reset-password")
                .param("newPassword", "123"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Bad Request"))
            .andExpect(jsonPath("$.message").value("Password is too weak"))
            .andExpect(jsonPath("$.path").value("/internal/users/1/reset-password"));
    }

}
