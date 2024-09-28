package com.carpool.car_pool.services.converters;

import com.carpool.car_pool.controllers.dtos.RegisterRequest;
import com.carpool.car_pool.repositories.entities.UserEntity;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class UserConverter {
    public UserEntity toEntity(@Valid RegisterRequest registerRequest) {
        return UserEntity.builder()
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .phoneNumber(registerRequest.getPhoneNumber())
                .build();
    }

}
