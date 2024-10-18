package com.carpool.car_pool.controllers;

import com.carpool.car_pool.controllers.dtos.AuthenticationRequest;
import com.carpool.car_pool.controllers.dtos.RegisterRequest;
import com.carpool.car_pool.services.AuthenticationService;
import com.carpool.car_pool.services.CurrentUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication and registration operations.
 */
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
            // TODO: Add HttpServletRequest so when page is reloaded we don't have to log in again
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
     * @param session The HTTP session containing user information.
     * @return ResponseEntity with the current user's information.
     */
    @GetMapping("/check")
    public ResponseEntity<?> check(HttpSession session) {
        return ResponseEntity.ok(currentUserService.getCurrentUser());
    }
}
