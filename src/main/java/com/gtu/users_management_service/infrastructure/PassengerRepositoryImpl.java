package com.gtu.users_management_service.infrastructure;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.gtu.users_management_service.domain.model.Passenger;
import com.gtu.users_management_service.domain.repository.PassengerRepository;
import com.gtu.users_management_service.infrastructure.entities.PassengerEntity;
import com.gtu.users_management_service.infrastructure.mappers.PassengerEntityMapper;

@Repository
public class PassengerRepositoryImpl implements PassengerRepository{
    private final JpaPassengerRepository jpaPassengerRepository;

    public PassengerRepositoryImpl(JpaPassengerRepository jpaPassengerRepository) {
        this.jpaPassengerRepository = jpaPassengerRepository;
    }

    @Override
    public Passenger save(Passenger passenger) {
        PassengerEntity passengerEntity = PassengerEntityMapper.toEntity(passenger);
        PassengerEntity savedEntity = jpaPassengerRepository.save(passengerEntity);
        return PassengerEntityMapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaPassengerRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean existsById(Long id) {
        return jpaPassengerRepository.existsById(id);
    }

    @Override
    public Optional<Passenger> findById(Long id) {
        return jpaPassengerRepository.findById(id)
                .map(PassengerEntityMapper::toDomain);
    }

    @Override
    public Long count() {
        return jpaPassengerRepository.count();
    }

    @Override
    public Optional<Passenger> findByEmail(String email) {
        return jpaPassengerRepository.findByEmail(email)
                .map(PassengerEntityMapper::toDomain);
                
    }
}
