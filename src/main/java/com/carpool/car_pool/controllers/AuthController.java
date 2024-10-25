package com.carpool.car_pool.controllers;

import com.carpool.car_pool.controllers.dtos.AuthenticationRequest;
import com.carpool.car_pool.controllers.dtos.RegisterRequest;
import com.carpool.car_pool.controllers.dtos.UserResponse;
import com.carpool.car_pool.services.AuthenticationService;
import com.carpool.car_pool.services.CurrentUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling authentication and registration operations.
 */
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication & Registration")
public class AuthController {

    private final AuthenticationService authService;
    private final CurrentUserService currentUserService;

    /**
     * Endpoint to authenticate a user.
     *
     * @param authenticationRequest The authentication request containing email and password.
     * @param session               The HTTP session to store user information.
     * @return ResponseEntity with HTTP status.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody @Valid AuthenticationRequest authenticationRequest,
            HttpSession session
    ) {
        authService.authenticate(authenticationRequest);
        session.setAttribute("userEmail", authenticationRequest.getEmail());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * Endpoint to register a new user.
     *
     * @param registerRequest The registration request containing user details.
     * @return ResponseEntity with HTTP status.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody @Valid RegisterRequest registerRequest
    ) {
        authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Endpoint to log out the current user by invalidating the session.
     *
     * @param session The HTTP session to be invalidated.
     * @return ResponseEntity with HTTP status.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Endpoint to check the authentication status of the current user.
     *
     * @return ResponseEntity with the current user's information.
     */
    @GetMapping("/check")
    public ResponseEntity<?> check() {
        return ResponseEntity.ok(currentUserService.getCurrentUserEmail());
    }

    /**
     * Endpoint to get current user information
     *
     * @return {@link UserResponse}
     */
    @GetMapping("/get-user")
    public ResponseEntity<UserResponse> getUser() {
        return ResponseEntity.ok(currentUserService.getCurrentUserResponse());
    }
}
