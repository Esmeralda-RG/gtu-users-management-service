package com.gtu.users_management_service.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    private String name;

    @Schema(description = "Email of the passenger", example = "johndoe@example.com")
    private String email;

    @Schema(description = "Password of the passenger", example = "Passw0rd")
    private String password;
}
