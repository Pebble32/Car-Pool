package com.carpool.car_pool.controllers;

import com.carpool.car_pool.controllers.dtos.*;
import com.carpool.car_pool.services.RideRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("ride-requests")
@RequiredArgsConstructor
@Tag(name = "RideRequest")
public class RideRequestController {

    private final RideRequestService rideRequestService;

    @PostMapping("/create")
    public ResponseEntity<Long> createRideRequest(
            @RequestBody @Valid RideRequestRequest rideRequestRequest,
            // TODO: This is ugly and bad solution should be changed after implementing security
            @RequestHeader("X-User-Email") String userEmail
    ) {
        Long rideRequestId = rideRequestService.createRideRequest(rideRequestRequest, userEmail);
        return ResponseEntity.status(CREATED).body(rideRequestId);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<RideRequestResponse>> getRideOffers(
            @RequestHeader("X-User-Email") String userEmail,
            @RequestHeader("X-Request-ID") Long rideOfferId
    ) {
        return ResponseEntity.ok(rideRequestService.getRideRequestsForRideOffer(userEmail, rideOfferId));
    }

    @PutMapping("/answer")
    public ResponseEntity<RideRequestResponse> answerRideOffer(
            @RequestBody @Valid AnswerRideRequestRequest request,
            @RequestHeader("X-User-Email") String userEmail
    ) {
        var rideRequestResponse = rideRequestService.answerRideRequest(request, userEmail);
        return ResponseEntity.status(CREATED).body(rideRequestResponse);
    }
}
