package com.carpool.car_pool.services;

import com.carpool.car_pool.controllers.dtos.UserResponse;
import com.carpool.car_pool.repositories.UserRepository;
import com.carpool.car_pool.repositories.common.PageResponse;
import com.carpool.car_pool.repositories.entities.RideRequestsEntity;
import com.carpool.car_pool.repositories.entities.UserEntity;
import com.carpool.car_pool.services.converters.UserConverter;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private final EmailService emailService;

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

    public void uploadProfilePicture(@NotNull MultipartFile file) {
        UserEntity currentUser = currentUserService.getCurrentUser();

        String profilePicturePath = fileStorageService
                .saveProfilePicture(file, currentUser.getId());

        if (profilePicturePath != null) {
            currentUser.setProfilePicture(profilePicturePath);
            userRepository.save(currentUser);
        }
    }

    /**
     * Sends a notification to a given {@link UserEntity} with a given message.
     *
     * @param user    The {@link UserEntity} that should receive the notification.
     * @param message The message that goes with the notification.
     */
    public void sendNotification(UserEntity user, String message) {
        emailService.sendSimpleEmail(user.getEmail(), "some subject", message);
        // TODO: Sends email and pop up notification
    }

    public void sendRideRequestNotification(UserEntity receiver, RideRequestsEntity rideRequest) {
        emailService.sendRideRequestNotificationEmail(receiver, rideRequest);
        // TODO: Sends email and pop up notification
    }
}
