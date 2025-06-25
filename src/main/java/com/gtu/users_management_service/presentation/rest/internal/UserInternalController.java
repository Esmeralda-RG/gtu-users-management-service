package com.gtu.users_management_service.presentation.rest.internal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gtu.users_management_service.domain.model.User;
import com.gtu.users_management_service.domain.service.UserService;

import io.swagger.v3.oas.annotations.Hidden;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/internal/users")
@Hidden
public class UserInternalController {
    

    private final UserService userService;

    private static final String USER_NOT_FOUND = "User does not exist";

    public UserInternalController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        try {
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(user); 
        } catch (IllegalArgumentException e) {
            if (USER_NOT_FOUND.equals(e.getMessage())) {
                return ResponseEntity.ok(new User()); 
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/{id}/reset-password")
    public User resetPassword(@PathVariable Long id, @RequestParam String newPassword) {
        User user = new User();
        user.setId(id);
        return userService.resetPassword(user, newPassword);
    }
}
