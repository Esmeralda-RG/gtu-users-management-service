package com.gtu.users_management_service.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
@Schema(description = "DTO that represents a passenger in the system")
public class PassengerDTO {
    @Schema(description = "Unique identifier of the passenger", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Name of the passenger", example = "John Doe")
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @Schema(description = "Email of the passenger", example = "johndoe@example.com")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @Schema(description = "Password of the passenger", example = "Passw0rd")
    @NotEmpty(message = "Password cannot be empty")
    private String password;
}
