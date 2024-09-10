package com.carpool.car_pool.controllers;

import com.carpool.car_pool.controllers.dtos.AuthenticationRequest;
import com.carpool.car_pool.services.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.ACCEPTED;


@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<?> authenticate (
            @RequestBody @Valid AuthenticationRequest authenticationRequest
    ) {
        authenticationService.authenticate(authenticationRequest);
        return ResponseEntity.accepted().build();
    }
}
