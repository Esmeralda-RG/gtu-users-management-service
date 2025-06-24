package com.gtu.users_management_service.infrastructure;

import com.gtu.users_management_service.domain.model.Role;
import com.gtu.users_management_service.domain.model.User;
import com.gtu.users_management_service.infrastructure.entities.UserEntity;
import com.gtu.users_management_service.infrastructure.mappers.UserEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRepositoryImplTest {

    private JpaUserRepository jpaUserRepository;
    private UserRepositoryImpl userRepository;

    @BeforeEach
    void setUp() {
        jpaUserRepository = mock(JpaUserRepository.class);
        userRepository = new UserRepositoryImpl(jpaUserRepository);
    }

    @Test
    void shouldSaveAndReturnUser_whenUserIsValid() {
        User user = new User(); // Simula un usuario de dominio
        UserEntity entity = UserEntityMapper.toEntity(user);
        when(jpaUserRepository.save(any())).thenReturn(entity);

        User result = userRepository.save(user);

        assertNotNull(result);
        verify(jpaUserRepository).save(any());
    }

    @Test
    void shouldReturnTrue_whenUserExistsByEmail() {
        String email = "test@example.com";
        when(jpaUserRepository.findByEmail(email)).thenReturn(Optional.of(new UserEntity()));

        boolean exists = userRepository.existsByEmail(email);

        assertTrue(exists);
        verify(jpaUserRepository).findByEmail(email);
    }

    @Test
    void shouldReturnUser_whenUserFoundByEmail() {
        String email = "test@example.com";
        when(jpaUserRepository.findByEmail(email)).thenReturn(Optional.of(new UserEntity()));

        Optional<User> result = userRepository.findByEmail(email);

        assertTrue(result.isPresent());
        verify(jpaUserRepository).findByEmail(email);
    }

    @Test
    void shouldReturnUser_whenUserFoundById() {
        Long id = 1L;
        when(jpaUserRepository.findById(id)).thenReturn(Optional.of(new UserEntity()));

        Optional<User> result = userRepository.findById(id);

        assertTrue(result.isPresent());
        verify(jpaUserRepository).findById(id);
    }

    @Test
    void shouldDeleteUser_whenUserIdIsGiven() {
        Long id = 1L;

        userRepository.deleteById(id);

        verify(jpaUserRepository).deleteById(id);
    }

    @Test
    void shouldReturnUserList_whenUsersFoundByRole() {
        Role role = Role.ADMIN;
        List<UserEntity> userEntities = List.of(new UserEntity(), new UserEntity());
        when(jpaUserRepository.findByRole(role)).thenReturn(userEntities);

        List<User> result = userRepository.findByRole(role);

        assertEquals(2, result.size());
        verify(jpaUserRepository).findByRole(role);
    }
}
