package com.gtu.users_management_service.presentation.rest.internal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gtu.users_management_service.domain.model.User;
import com.gtu.users_management_service.domain.service.UserService;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
@RequestMapping("/internal/users")
@Hidden
public class UserController {
    

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    public User getUserByEmail(@RequestParam String email){
        return userService.getUserByEmail(email);
    }
    
}
