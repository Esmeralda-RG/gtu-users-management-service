package com.gtu.users_management_service.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {
    private int status;
    private String error;
    private Object message;
    private String path;
}