package com.gtu.users_management_service.application.usecase;

import com.gtu.users_management_service.application.dto.UserDTO;
import com.gtu.users_management_service.application.mapper.UserMapper;
import com.gtu.users_management_service.domain.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserUseCase {

    private final UserService userService;

    public UserUseCase(UserService userService) {
        this.userService = userService;
    }

    public UserDTO createUser(UserDTO userDTO) {
        return UserMapper.toDTO(userService.createUser(UserMapper.toDomain(userDTO)));
    }
}