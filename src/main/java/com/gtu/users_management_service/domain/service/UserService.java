package com.gtu.users_management_service.domain.service;

import com.gtu.users_management_service.domain.model.Status;
import com.gtu.users_management_service.domain.model.User;

public interface UserService {
    User createUser(User user);
    void deleteUser(Long id);
    User updateStatus(Long id, Status status);
}