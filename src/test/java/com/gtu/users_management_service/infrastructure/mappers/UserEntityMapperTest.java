package com.gtu.users_management_service.infrastructure.mappers;

import com.gtu.users_management_service.domain.model.Role;
import com.gtu.users_management_service.domain.model.Status;
import com.gtu.users_management_service.domain.model.User;
import com.gtu.users_management_service.infrastructure.entities.UserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityMapperTest {

    @Test
    void shouldMapUserToEntityCorrectly() {
        User user = new User(1L, "Test User", "test@example.com", "secret", Role.ADMIN, Status.ACTIVE);

        UserEntity entity = UserEntityMapper.toEntity(user);

        assertNotNull(entity);
        assertEquals(user.getId(), entity.getId());
        assertEquals(user.getName(), entity.getName());
        assertEquals(user.getEmail(), entity.getEmail());
        assertEquals(user.getPassword(), entity.getPassword());
        assertEquals(user.getRole(), entity.getRole());
        assertEquals(user.getStatus(), entity.getStatus());
    }

    @Test
    void shouldMapEntityToUserCorrectly() {
        UserEntity entity = new UserEntity(1L, "Entity User", "entity@example.com", "hashed", Role.SUPERADMIN, Status.INACTIVE);

        User user = UserEntityMapper.toDomain(entity);

        assertNotNull(user);
        assertEquals(entity.getId(), user.getId());
        assertEquals(entity.getName(), user.getName());
        assertEquals(entity.getEmail(), user.getEmail());
        assertEquals(entity.getPassword(), user.getPassword());
        assertEquals(entity.getRole(), user.getRole());
        assertEquals(entity.getStatus(), user.getStatus());
    }
}
