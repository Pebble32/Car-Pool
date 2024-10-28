package com.carpool.car_pool.controllers;

import com.carpool.car_pool.controllers.dtos.EditRideOfferRequest;
import com.carpool.car_pool.controllers.dtos.RideOfferRequest;
import com.carpool.car_pool.controllers.dtos.RideOfferResponse;
import com.carpool.car_pool.controllers.dtos.UserResponse;
import com.carpool.car_pool.repositories.common.PageResponse;
import com.carpool.car_pool.repositories.entities.UserEntity;
import com.carpool.car_pool.services.CurrentUserService;
import com.carpool.car_pool.services.RideOfferService;
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


import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for managing ride offers.
 */
@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
@Tag(name = "RideOffer")
public class RideOfferController {

    private final RideOfferService rideOfferService;
    private final CurrentUserService currentUserService;

    /**
     * Retrieves a list of all available ride offers.
     *
     * @return ResponseEntity containing a list of {@link RideOfferResponse}.
     */
    @GetMapping("/all")
    public ResponseEntity<List<RideOfferResponse>> findAllRideOffers (){
        return ResponseEntity.ok(rideOfferService.findAllRideOffers());
    }

    /**
     * Creates a new ride offer.
     *
     * @param rideOfferRequest The request containing details for the new ride offer.
     * @return ResponseEntity containing the ID of the created ride offer.
     */
    @PostMapping("/create")
    public ResponseEntity<Long> createRideOffer(
            @RequestBody @Valid RideOfferRequest rideOfferRequest
    ) {
        UserEntity currentUser = currentUserService.getCurrentUser();
        return ResponseEntity.ok(rideOfferService.createRideOffer(rideOfferRequest, currentUser));
    }

    /**
     * Retrieves detailed information about a specific ride offer.
     *
     * @param ID The unique identifier of the ride offer.
     * @return ResponseEntity containing the {@link RideOfferResponse} details.
     */
    @GetMapping("/details")
    public ResponseEntity<RideOfferResponse> viewRideOfferDetails(
            @RequestParam @Valid Long ID
    ) {
        return ResponseEntity.ok(rideOfferService.viewRideOfferDetail(ID));
    }

    /**
     * Edits the details of an existing ride offer.
     *
     * @param editRideOfferRequest The request containing updated ride offer details.
     * @return ResponseEntity containing the updated {@link RideOfferResponse}.
     */
    @PutMapping("/details")
    public ResponseEntity<RideOfferResponse>  editRideOfferDetails(
            @RequestBody EditRideOfferRequest editRideOfferRequest
    ){
        UserEntity currentUser = currentUserService.getCurrentUser();
        return ResponseEntity.ok(rideOfferService.editRideOfferDetail(editRideOfferRequest, currentUser));
    }

    /**
     * Deletes a specific ride offer.
     *
     * @param id The unique identifier of the ride offer to be deleted.
     * @return ResponseEntity with HTTP status.
     */
    @DeleteMapping("/details/{id}")
    public ResponseEntity<Void> deleteRideOffer(
            @PathVariable Long id
    ) {
        UserEntity currentUser = currentUserService.getCurrentUser();
        rideOfferService.deleteRideOffer(id, currentUser);
        return ResponseEntity.ok().build();
    }

    /**
     * Returns a paginated list of all ride offers.
     *
     * @param page The page number (zero-based).
     * @param size The size of the page.
     * @return Paginated list of ride offers.
     */
    @GetMapping("/all/paginated")
    public ResponseEntity<PageResponse<RideOfferResponse>> findAllRideOffersPaginated(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        PageResponse<RideOfferResponse> response = rideOfferService.findAllRideOffersPaginated(page, size);
        return ResponseEntity.ok(response);
    }

    /**
     * Returns a list of all providers
     * @return ResponseEntity containing a list of {@link UserEntity}.
     */
    @GetMapping("/all/providers")
    public ResponseEntity<List<UserResponse>> showAllProviders() {
        return ResponseEntity.ok(rideOfferService.getAllProviders());
    }

    /**
     * Searches for ride offers based on the provided parameters.
     * @param page The page number (zero-based).
     * @param size The size of the page.
     * @param startLocation The starting location of the ride.
     * @param endLocation The destination of the ride.
     * @param departureTime The departure time of the ride.
     * @return ResponseEntity containing a paginated list of {@link RideOfferResponse}.
     */
    @GetMapping("/filter")
    public ResponseEntity<PageResponse<RideOfferResponse>> FilterRideOffers(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "startLocation", required = false) String startLocation,
            @RequestParam(name = "endLocation", required = false) String endLocation,
            @RequestParam(name = "departureTime", required = false) LocalDateTime departureTime
    ) {
        return ResponseEntity.ok(rideOfferService.filterRides(startLocation, endLocation, departureTime, page, size));
    }

    /**
     * Searches for ride offers based on the provided keyword.
     * @param page The page number (zero-based).
     * @param size The size of the page.
     * @param keyword The keyword to search for.
     * @return ResponseEntity containing a paginated list of {@link RideOfferResponse}.
     */
    @GetMapping("/search")
    public ResponseEntity<PageResponse<RideOfferResponse>> searchForRides(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "keyword", required = true) String keyword
    ) {
        return ResponseEntity.ok(rideOfferService.searchForRides(keyword, page, size));
    }
}
