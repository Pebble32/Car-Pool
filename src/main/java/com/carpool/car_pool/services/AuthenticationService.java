package com.carpool.car_pool.services;

import com.carpool.car_pool.controllers.dtos.AuthenticationRequest;
import com.carpool.car_pool.controllers.dtos.RegisterRequest;
import com.carpool.car_pool.repositories.UserRepository;
import com.carpool.car_pool.repositories.entities.UserEntity;
import com.carpool.car_pool.services.converters.UserConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for handling user authentication and registration.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    /**
     * Authenticates a user based on the provided authentication request.
     *
     * @param authenticationRequest The request containing the user's email and password.
     * @throws RuntimeException if the user is not found or the password is incorrect.
     */
    public void authenticate(@Valid AuthenticationRequest authenticationRequest) {
        UserEntity userEntity = userRepository
                .findByEmail(authenticationRequest.getEmail()) //TODO: Better exception handling| UserNotFoundException
                .orElseThrow(() -> new RuntimeException("User with this email address does not exist"));

        if (!userEntity.getPassword().equals(authenticationRequest.getPassword())) {
            throw new RuntimeException("Wrong password");
            // TODO: Better exception handling | InvalidCredentialsException
        }
    }

    /**
     * Registers a new user based on the provided registration request.
     *
     * @param registerRequest The request containing the user's registration details.
     * @throws RuntimeException if a user with the provided email already exists.
     */
    public void register(@Valid RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
            // TODO: Better exception handling | UserAlreadyExistsException
        }
        UserEntity user = userConverter.toEntity(registerRequest);

        userRepository.save(user);
    }
}
