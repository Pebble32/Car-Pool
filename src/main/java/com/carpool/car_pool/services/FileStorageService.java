package com.carpool.car_pool.services;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class FileStorageService {

    @Value("${spring.file.upload.photos-output-path}")
    private String fileUploadPath;

    /**
     * Saves a profile picture for a specific user.
     *
     * @param file   The profile picture file to upload. Must be a valid image file.
     * @param userId The unique identifier of the user uploading the profile picture.
     * @return The relative path of the saved profile picture, or {@code null} if the upload fails.
     * @throws IllegalArgumentException if the file type is not allowed or the file size exceeds the limit.
     */
    public String saveProfilePicture(
            @NotNull MultipartFile file,
            @NotNull Long userId
    ) {
        final String fileUploadSubPath = "users" + File.separator + userId;
        return uploadFile(file, fileUploadSubPath);
    }

    /**
     * Uploads a file to the specified subdirectory.
     *
     * @param sourceFile         The source file to upload.
     * @param fileUploadSubPath  The subdirectory path where the file will be stored.
     * @return The relative path of the uploaded file, or {@code null} if the upload fails.
     * @throws IllegalArgumentException if the file type is not allowed or the file size exceeds the limit.
     */
    private String uploadFile(
            @NotNull MultipartFile sourceFile,
            @NotNull String fileUploadSubPath
    ) {
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());

        if (!isAllowedFileType(fileExtension)) {
            log.warn("Disallowed file type: {}", fileExtension);
            throw new IllegalArgumentException("Invalid file type: " + fileExtension);
        }

        // Enforce file size limit (e.g., 5MB)
        long maxFileSize = 5 * 1024 * 1024; // 5MB
        if (sourceFile.getSize() > maxFileSize) {
            log.warn("File size exceeds limit: {}", sourceFile.getSize());
            throw new IllegalArgumentException("File size exceeds the maximum allowed limit.");
        }

        final String finalUploadPath = fileUploadPath + File.separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);
        if (!targetFolder.exists()) {
            boolean folderCreated = targetFolder.mkdirs();
            if (!folderCreated) {
                log.warn("Failed to create target folder");
                return null;
            }
        }

        String targetFileName = System.currentTimeMillis() + "." + fileExtension;
        String targetFilePath = finalUploadPath + File.separator + targetFileName;
        Path targetPath = Paths.get(targetFilePath);
        try {
            Files.write(targetPath, sourceFile.getBytes());
            log.info("Successfully uploaded file to: {}", targetFilePath);
            // Return the relative path to store in the database
            return fileUploadSubPath + File.separator + targetFileName;
        } catch (IOException e) {
            log.error("Couldn't write file", e);
            return null;
        }
    }

    /**
     * Retrieves the file extension from the original filename.
     *
     * @param originalFilename The original name of the file.
     * @return The file extension in lowercase, or an empty string if none exists.
     */
    private String getFileExtension(String originalFilename) {
        if (originalFilename == null || originalFilename.isEmpty()) {
            return "";
        }
        int lastDotIndex = originalFilename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return originalFilename.substring(lastDotIndex + 1).toLowerCase();
    }

    /**
     * Checks whether the provided file extension is allowed.
     *
     * @param fileExtension The file extension to check.
     * @return {@code true} if the file type is allowed; {@code false} otherwise.
     */
    private boolean isAllowedFileType(String fileExtension) {
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");
        return allowedExtensions.contains(fileExtension.toLowerCase());
    }

    /**
     * Deletes an existing profile picture from the filesystem.
     *
     * @param profilePicturePath The relative path of the profile picture to delete.
     */
    public void deleteProfiePicture(String profilePicturePath) {
        String fullPath = fileUploadPath+ File.separator + profilePicturePath;
        Path path = Paths.get(fullPath);

        try{
           Files.deleteIfExists(path);
           log.info("Deleted old profile picture");
        } catch (IOException e){
            log.error("Could not delete old profile picture", e);
        }
    }
}