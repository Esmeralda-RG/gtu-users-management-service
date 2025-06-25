package com.gtu.users_management_service.presentation.rest.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtu.users_management_service.application.dto.PassengerDTO;
import com.gtu.users_management_service.domain.exception.ResourceNotFoundException;
import com.gtu.users_management_service.domain.model.Passenger;
import com.gtu.users_management_service.domain.service.PassengerService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class PassengerInternalControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PassengerService passengerService;

    @InjectMocks
    private PassengerInternalController passengerInternalController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(passengerInternalController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void getPassengerByEmail_ReturnsPassenger() throws Exception {
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setName("John Doe");
        passenger.setEmail("john.doe@example.com");

        Mockito.when(passengerService.getPassengerByEmail("john.doe@example.com"))
               .thenReturn(passenger);

        mockMvc.perform(get("/internal/passengers")
                .param("email", "john.doe@example.com"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("John Doe")))
            .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    void getPassengerByEmail_ReturnsNotFound_WhenPassengerDoesNotExist() throws Exception {
        Mockito.when(passengerService.getPassengerByEmail("notfound@example.com"))
            .thenThrow(new ResourceNotFoundException("Passenger not found with email: notfound@example.com"));

        mockMvc.perform(get("/internal/passengers")
                .param("email", "notfound@example.com"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message").value("Passenger not found with email: notfound@example.com"))
            .andExpect(jsonPath("$.path").value("/internal/passengers"));
    }

    @Test
    void resetPassword_ReturnsUpdatedPassenger() throws Exception {
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setPassword("NewPassw0rd");

        Mockito.when(passengerService.resetPassword(any(Passenger.class), eq("NewPassw0rd")))
               .thenReturn(passenger);

        mockMvc.perform(put("/internal/passengers/1/reset-password")
                .param("newPassword", "NewPassw0rd"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.password", is("NewPassw0rd")));
    }

    @Test
    void resetPassword_ReturnsNotFound_WhenPassengerDoesNotExist() throws Exception {
        Mockito.when(passengerService.resetPassword(any(Passenger.class), eq("noSuchPass")))
            .thenThrow(new ResourceNotFoundException("Passenger not found with ID: 99"));

        mockMvc.perform(put("/internal/passengers/99/reset-password")
                .param("newPassword", "noSuchPass"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message").value("Passenger not found with ID: 99"))
            .andExpect(jsonPath("$.path").value("/internal/passengers/99/reset-password"));
    }

    @Test
    void resetPassword_ReturnsBadRequest_WhenInvalidPassword() throws Exception {
        Passenger passenger = new Passenger();
        passenger.setId(1L);

        Mockito.when(passengerService.resetPassword(any(Passenger.class), eq("123")))
            .thenThrow(new IllegalArgumentException("Password too short"));

        mockMvc.perform(put("/internal/passengers/1/reset-password")
                .param("newPassword", "123"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Bad Request"))
            .andExpect(jsonPath("$.message").value("Password too short"));
    }

    @Test
    void postMethodName_CreatesPassengerSuccessfully() throws Exception {
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setId(2L);
        passengerDTO.setName("Alice");
        passengerDTO.setEmail("alice@example.com");
        passengerDTO.setPassword("StrongPass123");

        Passenger createdPassenger = new Passenger(
            passengerDTO.getId(),
            passengerDTO.getName(),
            passengerDTO.getEmail(),
            passengerDTO.getPassword()
        );

        Mockito.when(passengerService.createPassenger(any(Passenger.class)))
            .thenReturn(createdPassenger);

        mockMvc.perform(post("/internal/passengers")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(passengerDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(2)))
            .andExpect(jsonPath("$.name", is("Alice")))
            .andExpect(jsonPath("$.email", is("alice@example.com")))
            .andExpect(jsonPath("$.password", is("StrongPass123")));
    }

    @Test
    void postMethodName_ReturnsBadRequest_WhenInvalidInput() throws Exception {
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setId(3L);
        passengerDTO.setEmail("invalid@example.com");
        passengerDTO.setPassword("pass");

        Mockito.when(passengerService.createPassenger(any(Passenger.class)))
            .thenThrow(new IllegalArgumentException("Name is required"));

        mockMvc.perform(post("/internal/passengers")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(passengerDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Bad Request"))
            .andExpect(jsonPath("$.message").value("Name is required"));
    }
}

