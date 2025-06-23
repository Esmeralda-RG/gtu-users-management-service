package com.gtu.users_management_service.infrastructure.mappers;

import com.gtu.users_management_service.domain.model.User;
import com.gtu.users_management_service.infrastructure.entities.UserEntity;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserEntityMapper {

    public UserEntity toEntity(User user) {
        return new UserEntity(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPassword(),
            user.getRole(),
            user.getStatus()
        );
    }

    public User toDomain(UserEntity entity) {
        return new User(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getPassword(),
            entity.getRole(),
            entity.getStatus()
        );
    }
}
