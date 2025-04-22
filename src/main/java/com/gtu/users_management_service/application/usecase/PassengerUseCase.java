package com.gtu.users_management_service.application.usecase;

import org.springframework.stereotype.Service;

import com.gtu.users_management_service.application.dto.PassengerDTO;
import com.gtu.users_management_service.application.mapper.PassengerMapper;
import com.gtu.users_management_service.domain.service.PassengerService;

@Service
public class PassengerUseCase {
    
    private final PassengerService passengerService;

    public PassengerUseCase(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    public PassengerDTO createPassenger(PassengerDTO passengerDTO) {
        return PassengerMapper.toDTO(
            passengerService.createPassenger(
                PassengerMapper.toDomain(passengerDTO)
            )
        );
    }
}
