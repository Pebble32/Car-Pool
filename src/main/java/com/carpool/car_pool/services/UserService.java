package com.carpool.car_pool.services;

import com.carpool.car_pool.controllers.dtos.UserResponse;
import com.carpool.car_pool.repositories.UserRepository;
import com.carpool.car_pool.repositories.common.PageResponse;
import com.carpool.car_pool.repositories.entities.ProfilePictureEntity;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;


import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    /**
     * Uploads a profile picture for the current user.
     * Resizes if picture is too big.
     *
     * @param file The MultipartFile representing the profile picture.
     * @param user The user entity to associate the profile picture with.
     */
    @Transactional
    public void uploadProfilePicture(@NotNull MultipartFile file, @NotNull UserEntity user) {
        try {
            // image as a BufferedImage
            InputStream inputStream = new ByteArrayInputStream(file.getBytes());
            BufferedImage originalImage = ImageIO.read(inputStream);

            if (originalImage == null) {
                throw new RuntimeException("Invalid image file");
            }
        

            // check if resizing is necessary
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
            int targetWidth = 200;
            int targetHeight = 200;

            String base64ProfilePicture;

            if (originalWidth > targetWidth || originalHeight > targetHeight) {
                // Aspect ratio calc
                double widthRatio = (double) targetWidth / originalWidth;
                double heightRatio = (double) targetHeight / originalHeight;
                double scaleRatio = Math.min(widthRatio, heightRatio);

                int newWidth = (int) (originalWidth * scaleRatio);
                int newHeight = (int) (originalHeight * scaleRatio);

                // resize the image w.r.t. aspect ratio
                BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics2D = resizedImage.createGraphics();
                graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                graphics2D.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
                graphics2D.dispose();

                // BufferedImage to Base64 string
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(resizedImage, "jpeg", byteArrayOutputStream);
                byte[] downsampledImageBytes = byteArrayOutputStream.toByteArray();
                base64ProfilePicture = Base64.getEncoder().encodeToString(downsampledImageBytes);
            } else {
                base64ProfilePicture = Base64.getEncoder().encodeToString(file.getBytes());
            }

            // if the user already has profile picture
            ProfilePictureEntity existingProfilePicture = user.getProfilePicture();
            if (existingProfilePicture != null) {
                existingProfilePicture.setBase64Image(base64ProfilePicture);
            } else {
                ProfilePictureEntity newProfilePicture = ProfilePictureEntity.builder()
                        .base64Image(base64ProfilePicture)
                        .user(user)
                        .build();
                user.setProfilePicture(newProfilePicture);
            }

            userRepository.save(user);
        } catch (IOException e) {
            // TODO: Add proper exception handling
            throw new RuntimeException("Failed to upload profile picture", e);
        }
    }

    /**
     * Gets base64 image and returns it as a String.
     *
     * @param currentUser The current logged-in user.
     * @return Base64 image as a String.
     */
    @Transactional
    public String getProfilePicture(UserEntity currentUser) {
        ProfilePictureEntity profilePicture = currentUser.getProfilePicture();
        if (profilePicture == null || profilePicture.getBase64Image() == null || profilePicture.getBase64Image().isEmpty()) {
            // TODO: Add PictureNotFoundException | Global exception handling
            throw new RuntimeException("No profile picture found");
        }
        return profilePicture.getBase64Image();
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


    /**
     * Sends a ride request notification to ride offer provider.
     *
     * @param receiver    The {@link UserEntity} that should receive the notification.
     * @param rideRequest The message that goes with the notification.
     */
    public void sendRideRequestNotification(UserEntity receiver, RideRequestsEntity rideRequest) {
        emailService.sendRideRequestNotificationEmail(receiver, rideRequest);
        // TODO: Sends email and pop up notification
    }

    /**
     * Changes the first name the current user.
     *
     * @param currentUser    The {@link UserEntity} that should have their first name changed.
     * @param newName The new first name.
     */
    public void changeUsersFirstName(UserEntity currentUser, String newName) {
        currentUser.setFirstname(newName);
        userRepository.save(currentUser);
    }

    /**
     * Changes the last name of the current user.
     *
     * @param currentUser    The {@link UserEntity} that should have their last name changed.
     * @param newName The new last name.
     */
    public void changeUsersLastName(UserEntity currentUser, String newName) {
        currentUser.setLastname(newName);
        userRepository.save(currentUser);
    }

    /**
     * Changes the phone number of the current user.
     *
     * @param currentUser    The {@link UserEntity} that should have their phone number changed.
     * @param newNumber The new phone number.
     */
    public void changeUsersPhoneNumber(UserEntity currentUser, String newNumber) {
        currentUser.setPhoneNumber(newNumber);
        userRepository.save(currentUser);
    }

    /**
     * Changes the password of the current user.
     *
     * @param currentUser    The {@link UserEntity} that should have their password changed.
     * @param newPassword The new password.
     */
    public void changeUsersPassword(UserEntity currentUser, String oldPassword, String newPassword) {
        if(!currentUser.getPassword().equals(oldPassword)){
            throw new RuntimeException("Old password is incorrect");
        }
        currentUser.setPassword(newPassword);
        userRepository.save(currentUser);
    }

}
