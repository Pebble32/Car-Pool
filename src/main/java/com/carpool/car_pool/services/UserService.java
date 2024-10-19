package com.carpool.car_pool.services;

import com.carpool.car_pool.controllers.dtos.UserResponse;
import com.carpool.car_pool.repositories.UserRepository;
import com.carpool.car_pool.services.converters.UserConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for users.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    /**
     * Retrieves all users.
     *
     * @return All users as a list of {@link com.carpool.car_pool.controllers.dtos.UserResponse}.
     */
    public List<UserResponse> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userConverter::entityToDTO)
                .toList();
    }
}
