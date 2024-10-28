package com.carpool.car_pool.services;

import com.carpool.car_pool.controllers.dtos.UserResponse;
import com.carpool.car_pool.repositories.UserRepository;
import com.carpool.car_pool.repositories.common.PageResponse;
import com.carpool.car_pool.repositories.entities.UserEntity;
import com.carpool.car_pool.services.converters.UserConverter;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

/**
 * Service for users.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final FileStorageService fileStorageService;
    private final CurrentUserService currentUserService;

    /**
     * Retrieves paginated list of all users.
     *
     * @param page The page number (zero-based).
     * @param size The size of the page.
     * @return A {@link PageResponse} containing the paginated {@link UserResponse}.
     */
    public PageResponse<UserResponse> findAllUsersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<UserEntity> usersPage = userRepository.findAll(pageable);

        List<UserResponse> users = usersPage.stream()
                .map(userConverter::entityToDTO)
                .toList();

        return new PageResponse<>(
                users,
                usersPage.getNumber(),
                usersPage.getSize(),
                usersPage.getTotalElements(),
                usersPage.getTotalPages(),
                usersPage.isFirst(),
                usersPage.isLast()
        );
    }


    /**
     * Uploads a profile picture for the current user.
     *
     * @param file The MultipartFile representing the profile picture.
     */
    public void uploadProfilePicture(@NotNull MultipartFile file) {
        UserEntity currentUser = currentUserService.getCurrentUser();

        try {
            currentUser.setProfilePicture(file.getBytes());
            userRepository.save(currentUser);
        } catch (IOException e) {
            // TODO add UploadPictureException | Global exception handling
            throw new RuntimeException("Failed to upload profile picture", e);
        }
    }


    /**
     * Gets base64 image and turns it into string
     * @param currentUser logged in
     * @return Base64 image as a String
     */
    public String getProfilePicture(UserEntity currentUser) {
        byte[] image = currentUser.getProfilePicture();
        if (image == null) {
            //TODO Add PictureNotFoundException | Global Exception handling
            throw new RuntimeException("No profile picture found");
        }
        return Base64.getEncoder().encodeToString(image);
    }
}
