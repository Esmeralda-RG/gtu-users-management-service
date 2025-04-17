package com.gtu.users_management_service.domain.repository;

import com.gtu.users_management_service.domain.model.Role;
import com.gtu.users_management_service.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    void deleteById(Long id);
    List<User> findByRole(Role role);
}