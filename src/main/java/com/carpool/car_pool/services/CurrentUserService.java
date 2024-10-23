package com.carpool.car_pool.services;

import com.carpool.car_pool.controllers.dtos.UserResponse;
import com.carpool.car_pool.repositories.UserRepository;
import com.carpool.car_pool.repositories.entities.UserEntity;
import com.carpool.car_pool.services.converters.UserConverter;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for retrieving the currently authenticated user.
 */
@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;
    private final HttpSession httpSession;
    private final UserConverter userConverter;

    /**
     * Retrieves the currently authenticated user from the session.
     *
     * @return The {@link UserEntity} representing the current user.
     * @throws RuntimeException if the user is not authenticated or not found.
     */
    public UserEntity getCurrentUser() {
        String userEmail = (String) httpSession.getAttribute("userEmail");
        if (userEmail == null) {
            throw new RuntimeException("User not authenticated. Email not found in session.");
        }
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    /**
     * Retrieves the currently authenticated user email from the session
     *
     * @return The user email as String
     */
    public String getCurrentUserEmail() {
        if (httpSession.getAttribute("userEmail") == null) {
            throw new RuntimeException("No user authenticated. Email not found in session.");
        }
        return httpSession.getAttribute("userEmail").toString();
    }


    /**
     * Retrieves the current user information
     *
     * @return {@link UserResponse}
     */
    public UserResponse getCurrentUserResponse() {
        String userEmail = (String) httpSession.getAttribute("userEmail");

        return userRepository.findByEmail(userEmail)
                .map(userConverter::entityToDTO)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
