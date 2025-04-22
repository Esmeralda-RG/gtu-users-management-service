package com.gtu.users_management_service.application.mapper;

import java.util.List;

import com.gtu.users_management_service.application.dto.PassengerDTO;
import com.gtu.users_management_service.domain.model.Passenger;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PassengerMapper {
    
    public Passenger toDomain(PassengerDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Passenger(
            dto.getId(),
            dto.getName(),
            dto.getEmail(),
            dto.getPassword()
        );
    }

    public PassengerDTO toDTO(Passenger passenger) {
        if (passenger == null) {
            return null;
        }
        PassengerDTO dto = new PassengerDTO();
        dto.setId(passenger.getId());
        dto.setName(passenger.getName());
        dto.setEmail(passenger.getEmail());
        dto.setPassword(passenger.getPassword());
        return dto;
    }

    public static List<PassengerDTO> toDTOList(List<Passenger> domainList) {
        return domainList == null ? List.of() : domainList.stream()
            .map(PassengerMapper::toDTO)
            .toList();
    }

    public static List<Passenger> toDomainList(List<PassengerDTO> dtoList) {
        return dtoList == null ? List.of() : dtoList.stream()
            .map(PassengerMapper::toDomain)
            .toList();
    }
}
