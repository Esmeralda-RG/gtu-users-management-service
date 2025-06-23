package com.gtu.users_management_service.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO that represents a password update request")
public class PasswordUpdateDTO {
   @Schema(description = "Current password of the passenger", example = "OldPassw0rd")
   private String currentPassword;
  
   @Schema(description = "New password of the passenger", example = "NewPassw0rd")
   private String newPassword;
}
