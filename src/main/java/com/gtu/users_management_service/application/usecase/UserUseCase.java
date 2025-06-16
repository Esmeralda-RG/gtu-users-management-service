package com.gtu.users_management_service.application.usecase;

import com.gtu.users_management_service.application.dto.PasswordUpdateDTO;
import com.gtu.users_management_service.application.dto.UserDTO;
import com.gtu.users_management_service.application.mapper.UserMapper;
import com.gtu.users_management_service.domain.model.Role;
import com.gtu.users_management_service.domain.model.Status;
import com.gtu.users_management_service.domain.service.UserService;

import java.util.List;

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

    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }

    public UserDTO updateStatus(Long id, Status status) {
        return UserMapper.toDTO(userService.updateStatus(id, status));
    }

    public List<UserDTO> getUsersByRole(Role role) {
        return UserMapper.toDTOList(userService.getUsersByRole(role));
    }

    public UserDTO updatePassword(UserDTO userDTO, PasswordUpdateDTO passwordUpdateDTO) {
        return UserMapper.toDTO(
            userService.updatePassword(
                UserMapper.toDomain(userDTO),
                passwordUpdateDTO
            )
        );
    }

    public UserDTO getUserById(Long id){
        return UserMapper.toDTO(userService.getUserById(id));
    }
}