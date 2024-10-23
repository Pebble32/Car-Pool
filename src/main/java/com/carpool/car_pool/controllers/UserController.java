package com.carpool.car_pool.controllers;

import com.carpool.car_pool.repositories.entities.UserEntity;
import com.carpool.car_pool.services.CurrentUserService;
import com.carpool.car_pool.services.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for user-related operations.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final CurrentUserService currentUserService;
    private final FileStorageService fileStorageService;

    /**
     * Endpoint to upload or update the user's profile picture.
     *
     * @param file The profile picture file.
     * @return ResponseEntity with HTTP status.
     */
    @PostMapping(value = "/profile-picture", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadProfilePicture(
            @RequestParam("file") MultipartFile file
    ) {
        UserEntity currentUser = currentUserService.getCurrentUser();
        fileStorageService.saveProfilePicture(file, currentUser);
        return ResponseEntity.accepted().build();
    }
}
