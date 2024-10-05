package com.carpool.car_pool.controllers;

import com.carpool.car_pool.controllers.dtos.AuthenticationRequest;
import com.carpool.car_pool.controllers.dtos.RegisterRequest;
import com.carpool.car_pool.services.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication & Registration")
public class AuthController {

    private final AuthenticationService authService;

    /**
     * Endpoint to authenticate a user.
     *
     * @param authenticationRequest The authentication request containing email and password.
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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
