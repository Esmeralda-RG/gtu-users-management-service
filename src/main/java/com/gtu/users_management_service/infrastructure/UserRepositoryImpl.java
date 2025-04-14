package com.gtu.users_management_service.infrastructure;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.gtu.users_management_service.domain.model.User;
import com.gtu.users_management_service.domain.repository.UserRepository;
import com.gtu.users_management_service.infrastructure.entities.UserEntity;
import com.gtu.users_management_service.infrastructure.mappers.UserEntityMapper;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = UserEntityMapper.toEntity(user);
        UserEntity savedEntity = jpaUserRepository.save(userEntity);
        return UserEntityMapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
                .map(UserEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id)
                .map(UserEntityMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaUserRepository.deleteById(id);
    }
    
}
