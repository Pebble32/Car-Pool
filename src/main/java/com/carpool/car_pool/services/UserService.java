package com.carpool.car_pool.services;

import com.carpool.car_pool.repositories.UserRepository;
import com.carpool.car_pool.repositories.entities.UserEntity;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final CurrentUserService currentUserService;

    public void uploadProfilePicture(@NotNull MultipartFile file) {
        UserEntity currentUser = currentUserService.getCurrentUser();

        String profilePicturePath = fileStorageService
                .saveProfilePicture(file, currentUser.getId());

        if (profilePicturePath != null) {
            currentUser.setProfilePicture(profilePicturePath);
            userRepository.save(currentUser);
        }
    }
}
