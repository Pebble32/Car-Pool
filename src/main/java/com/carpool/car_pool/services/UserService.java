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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
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
    public void uploadProfilePicture(@NotNull MultipartFile file, @NotNull UserEntity user) {
        try {
            // read the image as a BufferedImage
            InputStream inputStream = new ByteArrayInputStream(file.getBytes());
            BufferedImage originalImage = ImageIO.read(inputStream);

            // determine if resizing is necessary
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
            //Might need to change later w/h
            int targetWidth = 200;
            int targetHeight = 200;

            if (originalWidth > targetWidth || originalHeight > targetHeight) {
                // aspect ratio to maintain proportions
                double widthRatio = (double) targetWidth / originalWidth;
                double heightRatio = (double) targetHeight / originalHeight;
                double scaleRatio = Math.min(widthRatio, heightRatio);

                int newWidth = (int) (originalWidth * scaleRatio);
                int newHeight = (int) (originalHeight * scaleRatio);

                // resize the image while maintaining aspect ratio
                BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
                Graphics2D graphics2D = resizedImage.createGraphics();
                graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                graphics2D.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
                graphics2D.dispose();

                // convert the resized BufferedImage back to byte[]
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(resizedImage, "jpeg", byteArrayOutputStream);
                byte[] downsampledImageBytes = byteArrayOutputStream.toByteArray();

                user.setProfilePicture(downsampledImageBytes);
            } else {
                user.setProfilePicture(file.getBytes());
            }

            userRepository.save(user);
        } catch (IOException e) {
            //TODO FailedFileUploadException | Global exception handling
            throw new RuntimeException("Failed to upload profile picture");
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
