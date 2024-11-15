package com.carpool.car_pool.controllers;

import com.carpool.car_pool.controllers.dtos.UserResponse;
import com.carpool.car_pool.repositories.common.PageResponse;
import com.carpool.car_pool.repositories.entities.UserEntity;
import com.carpool.car_pool.services.CurrentUserService;
import com.carpool.car_pool.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;


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
    @PostMapping(value = "/profile-picture", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfilePicture(
            @RequestPart("file") MultipartFile file
    ) {
        UserEntity currentUser = currentUserService.getCurrentUser();
        userService.uploadProfilePicture(file, currentUser);
        return ResponseEntity.accepted().build();
    }


    /**
     * Retrieves profile picture
     * @return ResponseEntity containing a profile picture as a String
     */
    @GetMapping("/profile-picture")
    public ResponseEntity<String> getProfilePicture() {
        UserEntity currentUser = currentUserService.getCurrentUser();
        return ResponseEntity.ok().body(userService.getProfilePicture(currentUser));
    }

    /**
     * Changes the profile picture of the current user.
     * This endpoint performs the same function as {@code uploadProfilePicture} and is provided
     * for semantic clarity in the API.
     *
     * @param file The profile picture file.
     * @return ResponseEntity with HTTP status.
     */
    @PutMapping(value = "/profile-picture", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> changeProfilePicture(
            @RequestPart("file") MultipartFile file
    ){
        UserEntity currentUser = currentUserService.getCurrentUser();
        userService.uploadProfilePicture(file, currentUser);
        return ResponseEntity.accepted().build();
    }

    /**
     * Updates the user's information.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param phoneNumber The user's phone number.
     * @return ResponseEntity with HTTP status.
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "lastName", required = false) String lastName,
            @RequestParam(name = "phoneNumber", required = false) String phoneNumber
    ) {
        UserEntity currentUser = currentUserService.getCurrentUser();

        if (firstName != null) {
            userService.changeUsersFirstName(currentUser, firstName);
        }
        if (lastName != null) {
            userService.changeUsersLastName(currentUser, lastName);
        }
        if (phoneNumber != null) {
            userService.changeUsersPhoneNumber(currentUser, phoneNumber);
        }

        return ResponseEntity.accepted().build();
    }

    /**
     * Updates the user's password.
     * @param oldPassword The user's old password.
     * @param newPassword The user's new password.
     * @return ResponseEntity with HTTP status.
     */
    @PutMapping("/update/password")
    public ResponseEntity<?> updateUserPassword(
            @RequestParam(name = "oldPassword") String oldPassword,
            @RequestParam(name = "newPassword") String newPassword
    ) {
        UserEntity currentUser = currentUserService.getCurrentUser();
        userService.changeUsersPassword(currentUser, oldPassword, newPassword);
        return ResponseEntity.accepted().build();
    }
}
