package com.gtu.users_management_service.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gtu.users_management_service.infrastructure.entities.UserEntity;
import java.util.List;
import com.gtu.users_management_service.domain.model.Role;


@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE u.email = ?1")
    Optional<UserEntity> findByEmail(String email);
    @Query("SELECT u FROM UserEntity u WHERE u.role = ?1")
    List<UserEntity> findByRole(Role role);

}
