package com.carpool.car_pool.controllers;

import com.carpool.car_pool.controllers.dtos.UserResponse;
import com.carpool.car_pool.repositories.common.PageResponse;
import com.carpool.car_pool.repositories.entities.UserEntity;
import com.carpool.car_pool.services.CurrentUserService;
import com.carpool.car_pool.services.FileStorageService;

import com.carpool.car_pool.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;


/**
 * Controller for managing users.
 * Controller for user-related operations.
 */
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService userService;
    private final CurrentUserService currentUserService;

    /**
     * Retrieves all users.
     *
     * @param page The page number (zero-based).
     * @param size The size of the page.
     * @return ResponseEntity containing a {@link PageResponse} of {@link UserResponse}.
     */
    @GetMapping("/all/paginated")
    public ResponseEntity<PageResponse<UserResponse>> getUsersPaginated(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        PageResponse<UserResponse> response = userService.findAllUsersPaginated(page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to upload or update the user's profile picture.
     *
     * @param file The profile picture file.
     * @return ResponseEntity with HTTP status.
     */
    @PostMapping(value = "/profile-picture", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadProfilePicture(
            @RequestPart("file") MultipartFile file
    ) {
        userService.uploadProfilePicture(file);
        return ResponseEntity.accepted().build();
    }


    /**
     * Get profile-picture of the current user
     * @return Profile picture as String
     */
    @GetMapping("/profile-picture")
    public ResponseEntity<String> getProfilePicture() {
        UserEntity currentUser = currentUserService.getCurrentUser();
        return ResponseEntity.ok().body(userService.getProfilePicture(currentUser));
    }
}
