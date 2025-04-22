package com.gtu.users_management_service.application.usecase;

import org.springframework.stereotype.Service;

import com.gtu.users_management_service.application.dto.PassengerDTO;
import com.gtu.users_management_service.application.dto.PasswordUpdateDTO;
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

    public PassengerDTO updatePassenger(PassengerDTO passengerDTO) {
        return PassengerMapper.toDTO(
            passengerService.updatePassenger(
                PassengerMapper.toDomain(passengerDTO)
            )
        );
    }

    public PassengerDTO updatePassword(PassengerDTO passengerDTO, PasswordUpdateDTO passwordUpdateDTO) {
        return PassengerMapper.toDTO(
            passengerService.updatePassword(
                PassengerMapper.toDomain(passengerDTO),
                passwordUpdateDTO
            )
        );
    }
}
