package com.gtu.users_management_service.presentation.rest.internal;

import com.gtu.users_management_service.domain.model.Passenger;
import com.gtu.users_management_service.domain.service.PassengerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@WebMvcTest(PassengerInternalController.class)
class PassengerInternalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private PassengerService passengerService;

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
}
