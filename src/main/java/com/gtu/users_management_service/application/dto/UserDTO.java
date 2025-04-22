package com.gtu.users_management_service.application.dto;

import com.gtu.users_management_service.domain.model.Role;
import com.gtu.users_management_service.domain.model.Status;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO that represents a user in the system") 
public class UserDTO {
    @Schema(description = "Unique identifier of the user", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Name of the user", example = "John Doe")
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @Schema(description = "Email of the user", example = "jhon.doe@gtu.com")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @Schema(description = "Password of the user", example = "Passw0rd")
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @Schema(description = "Role of the user", example = "ADMIN")
    @NotNull(message = "Role cannot be null")
    private Role role;

    @Schema(description = "Status of the user", example = "ACTIVE")
    private Status status;
}