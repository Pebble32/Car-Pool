package com.carpool.car_pool.controllers;

import com.carpool.car_pool.controllers.dtos.AnswerRideRequestRequest;
import com.carpool.car_pool.controllers.dtos.RideRequestRequest;
import com.carpool.car_pool.controllers.dtos.RideRequestResponse;
import com.carpool.car_pool.repositories.entities.UserEntity;
import com.carpool.car_pool.services.CurrentUserService;
import com.carpool.car_pool.services.RideRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("ride-requests")
@RequiredArgsConstructor
@Tag(name = "RideRequest")
public class RideRequestController {

    private final RideRequestService rideRequestService;
    private final CurrentUserService currentUserService;

    @PostMapping("/create")
    public ResponseEntity<Long> createRideRequest(
            @RequestBody @Valid RideRequestRequest rideRequestRequest
    ) {
        UserEntity currentUser = currentUserService.getCurrentUser();
        Long rideRequestId = rideRequestService.createRideRequest(rideRequestRequest, currentUser);
        return ResponseEntity.status(CREATED).body(rideRequestId);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<RideRequestResponse>> getRideOffers(
            @RequestParam Long rideOfferId
    ) {
        UserEntity currentUser = currentUserService.getCurrentUser();
        return ResponseEntity.ok(rideRequestService.getRideRequestsForRideOffer(rideOfferId, currentUser));
    }

    @PutMapping("/answer")
    public ResponseEntity<RideRequestResponse> answerRideOffer(
            @RequestBody @Valid AnswerRideRequestRequest request
    ) {
        UserEntity currentUser = currentUserService.getCurrentUser();
        var rideRequestResponse = rideRequestService.answerRideRequest(request, currentUser);
        return ResponseEntity.status(CREATED).body(rideRequestResponse);
    }
}
