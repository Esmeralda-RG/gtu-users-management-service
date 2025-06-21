package com.gtu.users_management_service.presentation.rest.internal;

import com.gtu.users_management_service.domain.model.Role;
import com.gtu.users_management_service.domain.model.Status;
import com.gtu.users_management_service.domain.model.User;
import com.gtu.users_management_service.domain.service.UserService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserInternalController.class) 
class UserInternalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean 
    private UserService userService;

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
}
