package com.carpool.car_pool.controllers;

import com.carpool.car_pool.controllers.dtos.AnswerRideRequestRequest;
import com.carpool.car_pool.controllers.dtos.EditRideRequestRequest;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * Controller for managing ride requests.
 */
@RestController
@RequestMapping("ride-requests")
@RequiredArgsConstructor
@Tag(name = "RideRequest")
public class RideRequestController {

    private final RideRequestService rideRequestService;
    private final CurrentUserService currentUserService;

    /**
     * Creates a new ride request.
     *
     * @param rideRequestRequest The request containing details for the new ride request.
     * @return ResponseEntity containing the ID of the created ride request.
     */
    @PostMapping("/create")
    public ResponseEntity<Long> createRideRequest(
            @RequestBody @Valid RideRequestRequest rideRequestRequest
    ) {
        UserEntity currentUser = currentUserService.getCurrentUser();
        Long rideRequestId = rideRequestService.createRideRequest(rideRequestRequest, currentUser);
        return ResponseEntity.status(CREATED).body(rideRequestId);
    }

    /**
     * Retrieves all ride requests for a specific ride offer.
     *
     * @param rideOfferId The unique identifier of the ride offer.
     * @return ResponseEntity containing a list of {@link RideRequestResponse}.
     */
    @GetMapping("/requests")
    public ResponseEntity<List<RideRequestResponse>> getRideOffers(
            @RequestParam Long rideOfferId
    ) {
        UserEntity currentUser = currentUserService.getCurrentUser();
        return ResponseEntity.ok(rideRequestService.getRideRequestsForRideOffer(rideOfferId, currentUser));
    }

    /**
     * Answers a ride request by accepting or rejecting it.
     *
     * @param request The request containing the ride request ID and the response.
     * @return ResponseEntity containing the updated {@link RideRequestResponse}.
     */
    @PutMapping("/answer")
    public ResponseEntity<RideRequestResponse> answerRideOffer(
            @RequestBody @Valid AnswerRideRequestRequest request
    ) {
        UserEntity currentUser = currentUserService.getCurrentUser();
        var rideRequestResponse = rideRequestService.answerRideRequest(request, currentUser);
        return ResponseEntity.status(CREATED).body(rideRequestResponse);
    }

    /**
     * Returns all the ride requests that have been made by the current user.
     *
     * @return ResponseEntity containing a list of the user's ride requests.
     */
    @GetMapping("/user-requests")
    public ResponseEntity<List<RideRequestResponse>> viewUsersRideRequests() {
        UserEntity currentUser = currentUserService.getCurrentUser();
        return ResponseEntity.ok(rideRequestService.getRideRequestsForUser(currentUser));
    }

    /**
     * Retrieves a paginated list of ride requests for a specific ride offer.
     *
     * @param rideOfferId The unique identifier of the ride offer.
     * @param page        The page number (zero-based).
     * @param size        The size of the page.
     * @return ResponseEntity containing a {@link PageResponse} of ride requests.
     */
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

    /**
     * Retrieves a paginated list of ride requests made by the current user.
     *
     * @param page The page number (zero-based).
     * @param size The size of the page.
     * @return ResponseEntity containing a {@link PageResponse} of the user's ride requests.
     */
    @GetMapping("/user-requests/paginated")
    public ResponseEntity<PageResponse<RideRequestResponse>> viewUsersRideRequestsPaginated(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        UserEntity currentUser = currentUserService.getCurrentUser();
        PageResponse<RideRequestResponse> response = rideRequestService.getRideRequestsForUserPaginated(currentUser, page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a request by the request owner
     *
     * @return ResponseEntity with HTTP status.
     */
    @DeleteMapping("/delete-request/{id}")
    public ResponseEntity<Void> deleteRideRequest(
            @PathVariable Long id
    ) {
        UserEntity currentUser = currentUserService.getCurrentUser();
        rideRequestService.deleteRequest(currentUser, id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/edit-request")
    public ResponseEntity<RideRequestResponse> editRideRequestStatus(
            @RequestBody@Valid EditRideRequestRequest editRequest
    ){
        UserEntity currentUser = currentUserService.getCurrentUser();
        RideRequestResponse updateRequest = rideRequestService.editRideRequestStatus(editRequest, currentUser);

        return ResponseEntity.ok(updateRequest);
    }
}
