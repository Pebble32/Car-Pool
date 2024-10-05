package com.carpool.car_pool.services;

import com.carpool.car_pool.repositories.UserRepository;
import com.carpool.car_pool.repositories.entities.UserEntity;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    public UserEntity getCurrentUser() {
        Long userId = (Long) httpSession.getAttribute("userEmail");
        if (userId == null) {
            throw new RuntimeException("User not authenticated");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
