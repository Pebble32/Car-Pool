package com.carpool.car_pool.services;

import com.carpool.car_pool.controllers.dtos.AuthenticationRequest;
import com.carpool.car_pool.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    public void authenticate(@Valid AuthenticationRequest authenticationRequest) {
        var userEntity = userRepository
                .findByEmail(authenticationRequest.getEmail()).orElseThrow(
                        () -> new RuntimeException("User with this email address does not exist")
                );
        if (!userEntity.getPassword().equals(authenticationRequest.getPassword())) {
            throw new RuntimeException("Wrong password");
        }
    }
}
