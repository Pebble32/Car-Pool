package com.carpool.car_pool.controllers;

import com.carpool.car_pool.controllers.dtos.RideOfferRequest;
import com.carpool.car_pool.controllers.dtos.RideRequestRequest;
import com.carpool.car_pool.services.RideRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("requests")
@RequiredArgsConstructor
@Tag(name = "RideOffer")
public class RideRequestController {

    private final RideRequestService rideRequestService;

    @PostMapping
    public ResponseEntity<Long> createRideOffer(
            @RequestBody @Valid RideRequestRequest rideRequestRequest,
            // TODO: This is ugly and bad solution should be changed after implementing security
            @RequestHeader("X-User-Email") String userEmail
    ) {
        return ResponseEntity.ok(rideRequestService.createRideOffer(rideRequestRequest, userEmail));
    }

}
