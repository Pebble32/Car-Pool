package com.carpool.car_pool.controllers;

import com.carpool.car_pool.controllers.dtos.AnswerRideRequestRequest;
import com.carpool.car_pool.controllers.dtos.RideRequestRequest;
import com.carpool.car_pool.controllers.dtos.RideRequestResponse;
import com.carpool.car_pool.repositories.common.PageResponse;
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

    /**
     * Returns all the ride requests that have been made by the current user
     * @return List of ride requests
     */
    @GetMapping("/user-requests")
    public ResponseEntity<List<RideRequestResponse>> viewUsersRideRequests(
    ){
        UserEntity currentUser = currentUserService.getCurrentUser();
        return ResponseEntity.ok(rideRequestService.getRideRequestsForUser(currentUser));
    }

    @GetMapping("/requests/paginated")
    public ResponseEntity<PageResponse<RideRequestResponse>> getRideRequestsForRideOfferPaginated(
            @RequestParam Long rideOfferId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        UserEntity currentUser = currentUserService.getCurrentUser();
        PageResponse<RideRequestResponse> response = rideRequestService.getRideRequestsForRideOfferPaginated(rideOfferId, currentUser, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-requests/paginated")
    public ResponseEntity<PageResponse<RideRequestResponse>> viewUsersRideRequestsPaginated(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        UserEntity currentUser = currentUserService.getCurrentUser();
        PageResponse<RideRequestResponse> response = rideRequestService.getRideRequestsForUserPaginated(currentUser, page, size);
        return ResponseEntity.ok(response);
    }
}