package com.carpool.car_pool.services;

import com.carpool.car_pool.controllers.dtos.AuthenticationRequest;
import com.carpool.car_pool.controllers.dtos.RegisterRequest;
import com.carpool.car_pool.repositories.UserRepository;
import com.carpool.car_pool.repositories.entities.UserEntity;
import com.carpool.car_pool.services.converters.UserConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public void authenticate(@Valid AuthenticationRequest authenticationRequest) {
        UserEntity userEntity = userRepository
                .findByEmail(authenticationRequest.getEmail()) //TODO: Better exception handling| UserNotFoundException
                .orElseThrow(() -> new RuntimeException("User with this email address does not exist"));

        if (!userEntity.getPassword().equals(authenticationRequest.getPassword())) {
            throw new RuntimeException("Wrong password");
            // TODO: Better exception handling | InvalidCredentialsException
        }
    }

    public void register(@Valid RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
            // TODO: Better exception handling | UserAlreadyExistsException
        }
        UserEntity user = userConverter.toEntity(registerRequest);

        userRepository.save(user);
    }
}
