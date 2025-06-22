package com.gtu.users_management_service.presentation.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.gtu.users_management_service.application.dto.PassengerDTO;
import com.gtu.users_management_service.application.dto.PasswordUpdateDTO;
import com.gtu.users_management_service.application.usecase.PassengerUseCase;
import com.gtu.users_management_service.presentation.exception.GlobalExceptionHandler;

@ExtendWith(MockitoExtension.class)
class PassengerControllerTest {

        private MockMvc mockMvc;

        @Mock
        private PassengerUseCase passengerUseCase;

        @InjectMocks
        private PassengerController passengerController;

        private ObjectMapper objectMapper;

        private PassengerDTO passengerDTO;
        private PasswordUpdateDTO passwordUpdateDTO;

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

                passwordUpdateDTO = new PasswordUpdateDTO();
                passwordUpdateDTO.setCurrentPassword("Passw0rd");
                passwordUpdateDTO.setNewPassword("NewPassw0rd");
        }

        @Test
        void shouldCreatePassenger_WhenValidInputIsProvided() throws Exception {
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

        @Test
        void shouldUpdatePassenger_WhenValidInputIsProvided() throws Exception {
                passengerDTO.setName("Jane Doe");
                passengerDTO.setEmail("janedoe@example.com");
                when(passengerUseCase.updatePassenger(any(PassengerDTO.class))).thenReturn(passengerDTO);

                mockMvc.perform(put("/passengers/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(passengerDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("Passenger updated successfully"))
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.data.id").value(passengerDTO.getId()))
                                .andExpect(jsonPath("$.data.name").value("Jane Doe"))
                                .andExpect(jsonPath("$.data.email").value("janedoe@example.com"));

                verify(passengerUseCase, times(1)).updatePassenger(any(PassengerDTO.class));
        }

        @Test
        void shouldReturnBadRequest_WhenPassengerToUpdateDoesNotExist() throws Exception {
                when(passengerUseCase.updatePassenger(any(PassengerDTO.class)))
                                .thenThrow(new IllegalArgumentException("Passenger not found"));

                mockMvc.perform(put("/passengers/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(passengerDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Passenger not found"));

                verify(passengerUseCase, times(1)).updatePassenger(any(PassengerDTO.class));
        }

        @Test
        void shouldUpdatePassword_WhenCurrentPasswordIsValid() throws Exception {
                PassengerDTO updatedPassengerDTO = new PassengerDTO();
                updatedPassengerDTO.setId(1L);
                updatedPassengerDTO.setName("John Doe");
                updatedPassengerDTO.setEmail("johndoe@example.com");
                updatedPassengerDTO.setPassword("NewPassw0rd");

                when(passengerUseCase.updatePassword(any(PassengerDTO.class), any(PasswordUpdateDTO.class)))
                                .thenReturn(updatedPassengerDTO);

                mockMvc.perform(put("/passengers/1/password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(passwordUpdateDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("Passenger password updated successfully"))
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.data.id").value(updatedPassengerDTO.getId()))
                                .andExpect(jsonPath("$.data.name").value(updatedPassengerDTO.getName()))
                                .andExpect(jsonPath("$.data.email").value(updatedPassengerDTO.getEmail()))
                                .andExpect(jsonPath("$.data.password").value("NewPassw0rd"));

                verify(passengerUseCase, times(1)).updatePassword(any(PassengerDTO.class),
                                any(PasswordUpdateDTO.class));
        }

        @Test
        void shouldReturnBadRequest_WhenCurrentPasswordIsInvalid() throws Exception {
                when(passengerUseCase.updatePassword(any(PassengerDTO.class), any(PasswordUpdateDTO.class)))
                                .thenThrow(new IllegalArgumentException("Invalid current password"));

                mockMvc.perform(put("/passengers/1/password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(passwordUpdateDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Invalid current password"));

                verify(passengerUseCase, times(1)).updatePassword(any(PassengerDTO.class),
                                any(PasswordUpdateDTO.class));
        }

        @Test
        void shouldReturnBadRequest_WhenNewPasswordIsEmpty() throws Exception {
                passwordUpdateDTO.setNewPassword(" ");

                when(passengerUseCase.updatePassword(any(), any()))
                                .thenThrow(new IllegalArgumentException("New password cannot be empty"));

                mockMvc.perform(put("/passengers/1/password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(passwordUpdateDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("New password cannot be empty"));
        }

        @Test
        void shouldReturnBadRequest_WhenNewPasswordEqualsCurrent() throws Exception {
                passwordUpdateDTO.setNewPassword("Passw0rd");

                when(passengerUseCase.updatePassword(any(), any()))
                        .thenThrow(new IllegalArgumentException("New password must be different from current password"));

                mockMvc.perform(put("/passengers/1/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordUpdateDTO)))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").value("New password must be different from current password"));
        }

        @Test
        void shouldReturnTotalPassengerCount_WhenRequested() throws Exception {
                when(passengerUseCase.countPassengers()).thenReturn(42L);

                mockMvc.perform(get("/passengers/count"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("Total number of passengers"))
                                .andExpect(jsonPath("$.status").value(200))
                                .andExpect(jsonPath("$.data").value(42));

                verify(passengerUseCase, times(1)).countPassengers();
        }
}
