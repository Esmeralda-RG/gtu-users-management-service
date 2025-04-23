package com.gtu.users_management_service.presentation.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtu.users_management_service.application.dto.PassengerDTO;
import com.gtu.users_management_service.application.usecase.PassengerUseCase;
import com.gtu.users_management_service.presentation.exception.GlobalExceptionHandler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PassengerControllerTest {
    
    private MockMvc mockMvc;

    @Mock
    private PassengerUseCase passengerUseCase;

    @InjectMocks
    private PassengerController passengerController;

    private ObjectMapper objectMapper;

    private PassengerDTO passengerDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(passengerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
        
        passengerDTO = new PassengerDTO();
        passengerDTO.setId(1L);
        passengerDTO.setName("John Doe");
        passengerDTO.setEmail("johndoe@example.com");
        passengerDTO.setPassword("Passw0rd");
    }

    @Test
    void createPassenger_Success() throws Exception {
        when(passengerUseCase.createPassenger(any(PassengerDTO.class))).thenReturn(passengerDTO);

        mockMvc.perform(post("/passengers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passengerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Passenger created successfully"))
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.id").value(passengerDTO.getId()))
                .andExpect(jsonPath("$.data.name").value(passengerDTO.getName()))
                .andExpect(jsonPath("$.data.email").value(passengerDTO.getEmail()));
        
                verify(passengerUseCase, times(1)).createPassenger(any(PassengerDTO.class));
    }   
}
