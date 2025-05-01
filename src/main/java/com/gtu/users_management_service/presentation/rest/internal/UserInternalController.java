package com.gtu.users_management_service.presentation.rest.internal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gtu.users_management_service.domain.model.User;
import com.gtu.users_management_service.domain.service.UserService;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/internal/users")
@Hidden
public class UserInternalController {
    

    private final UserService userService;

    public UserInternalController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public User getUserByEmail(@RequestParam String email){
        return userService.getUserByEmail(email);
    }
    
}
