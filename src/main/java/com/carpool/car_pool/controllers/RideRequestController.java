package com.carpool.car_pool.controllers;

import com.carpool.car_pool.controllers.dtos.RideRequestRequest;
import com.carpool.car_pool.services.RideRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ride-requests")
@RequiredArgsConstructor
@Tag(name = "RideRequest")
public class RideRequestController {

    private final RideRequestService rideRequestService;

    @PostMapping
    public ResponseEntity<Long> createRideRequest(
            @RequestBody @Valid RideRequestRequest rideRequestRequest,
            // TODO: This is ugly and bad solution should be changed after implementing security
            @RequestHeader("X-User-Email") String userEmail
    ) {
        Long rideRequestId = rideRequestService.createRideRequest(rideRequestRequest, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(rideRequestId);
    }
}
