package com.gtu.users_management_service.infrastructure.mappers;

import com.gtu.users_management_service.domain.model.Passenger;
import com.gtu.users_management_service.infrastructure.entities.PassengerEntity;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PassengerEntityMapper {
    
    public PassengerEntity toEntity(Passenger passenger) {
        return new PassengerEntity(
                passenger.getId(),
                passenger.getName(),
                passenger.getEmail(),
                passenger.getPassword()
        );
    }

    public Passenger toDomain(PassengerEntity passengerEntity) {
        return new Passenger(
                passengerEntity.getId(),
                passengerEntity.getName(),
                passengerEntity.getEmail(),
                passengerEntity.getPassword()
        );
    }
}
