package com.gtu.users_management_service.presentation.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gtu.users_management_service.application.dto.PasswordUpdateDTO;
import com.gtu.users_management_service.application.dto.ResponseDTO;
import com.gtu.users_management_service.application.dto.UserDTO;
import com.gtu.users_management_service.application.usecase.UserUseCase;
import com.gtu.users_management_service.domain.model.Role;
import com.gtu.users_management_service.domain.model.Status;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Endpoints for managing users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserUseCase userUseCase;

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Add a new user to the system.")
    public ResponseEntity<ResponseDTO<UserDTO>> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userUseCase.createUser(userDTO);
        return ResponseEntity.status(201).body(new ResponseDTO<>("User created successfully", createdUser, 201));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Remove a user from the system by its unique identifier.")
    public ResponseEntity<ResponseDTO<Void>> deleteUser(@PathVariable Long id) {
        userUseCase.deleteUser(id);
        return ResponseEntity.status(200).body(new ResponseDTO<>("User deleted successfully", null, 200));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update user status", description = "Update the status of a user by its unique identifier.")
    public ResponseEntity<ResponseDTO<UserDTO>> updateUserStatus(@PathVariable Long id, @RequestBody Status status) {
        
        UserDTO updatedUser = userUseCase.updateStatus(id, status);
        return ResponseEntity.ok(new ResponseDTO<>("User status updated successfully", updatedUser, 200));
    }

    @GetMapping
    @Operation(summary = "Get users by role", description = "Retrieve a list of users filtered by their role.")
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getUsersByRole(@RequestParam Role role) {
        List<UserDTO> users = userUseCase.getUsersByRole(role);
        return ResponseEntity.ok(new ResponseDTO<>("Users retrieved successfully", users, 200));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a user by its unique identifier.")
    public ResponseEntity<ResponseDTO<UserDTO>> getUserById(@PathVariable Long id) {
        UserDTO user = userUseCase.getUserById(id);
        return ResponseEntity.ok(new ResponseDTO<>("User retrieved successfully", user, 200));
    }

    @PutMapping("/{id}/password")
    @Operation(summary = "Update user password", description = "Update the password of an existing user.")
    public ResponseEntity<ResponseDTO<UserDTO>> updatePassword(@PathVariable Long id, @Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setPassword(passwordUpdateDTO.getCurrentPassword());
        UserDTO updatedUser= userUseCase.updatePassword(userDTO, passwordUpdateDTO);
        return ResponseEntity.ok(new ResponseDTO<>("User password updated successfully", updatedUser, 200));
    }
}