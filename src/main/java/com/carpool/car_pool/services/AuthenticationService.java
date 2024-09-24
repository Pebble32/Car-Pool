package com.carpool.car_pool.services;

import com.carpool.car_pool.controllers.dtos.AuthenticationRequest;
import com.carpool.car_pool.controllers.dtos.RegisterRequest;
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
                .findByEmail(authenticationRequest.getEmail()) //TODO: Better exception handling| UserNotFoundException
                .orElseThrow(() -> new RuntimeException("User with this email address does not exist"));

        if (!userEntity.getPassword().equals(authenticationRequest.getPassword())) {
            throw new RuntimeException("Wrong password");
            // TODO: Better exception handling | InvalidCredentialsException
        }
    }

    public void register(@Valid RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        UserEntity user = UserEntity.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .phoneNumber(registerRequest.getPhoneNumber())
                .build();

        userRepository.save(user);
    }
}
