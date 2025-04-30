package com.gtu.users_management_service.domain.service;

import java.util.List;

import com.gtu.users_management_service.application.dto.PasswordUpdateDTO;
import com.gtu.users_management_service.domain.model.Role;
import com.gtu.users_management_service.domain.model.Status;
import com.gtu.users_management_service.domain.model.User;

public interface UserService {
    User createUser(User user);
    void deleteUser(Long id);
    User updateStatus(Long id, Status status);
    List<User> getUsersByRole(Role role);
    User updatePassword(User user, PasswordUpdateDTO passwordUpdateDTO);
}