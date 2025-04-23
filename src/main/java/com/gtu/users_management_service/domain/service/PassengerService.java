package com.gtu.users_management_service.domain.service;

import com.gtu.users_management_service.application.dto.PasswordUpdateDTO;
import com.gtu.users_management_service.domain.model.Passenger;

public interface PassengerService {
    Passenger createPassenger(Passenger passenger);
    Passenger updatePassenger(Passenger passenger);
    Passenger updatePassword(Passenger passenger, PasswordUpdateDTO passwordUpdateDTO);
    Long countPassengers();
}
