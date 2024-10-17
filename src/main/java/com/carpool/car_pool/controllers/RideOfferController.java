package com.carpool.car_pool.controllers;

import com.carpool.car_pool.controllers.dtos.EditRideOfferRequest;
import com.carpool.car_pool.controllers.dtos.RideOfferRequest;
import com.carpool.car_pool.controllers.dtos.RideOfferResponse;
import com.carpool.car_pool.repositories.common.PageResponse;
import com.carpool.car_pool.repositories.entities.UserEntity;
import com.carpool.car_pool.services.CurrentUserService;
import com.carpool.car_pool.services.RideOfferService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
@Tag(name = "RideOffer")
public class RideOfferController {

    private final RideOfferService rideOfferService;
    private final CurrentUserService currentUserService;

    @GetMapping("/all")
    public ResponseEntity<List<RideOfferResponse>> findAllRideOffers (){
        return ResponseEntity.ok(rideOfferService.findAllRideOffers());
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createRideOffer(
            @RequestBody @Valid RideOfferRequest rideOfferRequest
    ) {
        UserEntity currentUser = currentUserService.getCurrentUser();
        return ResponseEntity.ok(rideOfferService.createRideOffer(rideOfferRequest, currentUser));
    }

    @GetMapping("/details")
    public ResponseEntity<RideOfferResponse> viewRideOfferDetails(
            @RequestParam @Valid Long id
    ) {
        return ResponseEntity.ok(rideOfferService.viewRideOfferDetail(id));
    }

    @PutMapping("/details")
    public ResponseEntity<RideOfferResponse>  editRideOfferDetails(
            @RequestBody EditRideOfferRequest editRideOfferRequest
    ){
        UserEntity currentUser = currentUserService.getCurrentUser();
        return ResponseEntity.ok(rideOfferService.editRideOfferDetail(editRideOfferRequest, currentUser));
    }

  
    @DeleteMapping("/details/{id}")
    public ResponseEntity<Void> deleteRideOffer(
            @PathVariable Long id
    ) {
        UserEntity currentUser = currentUserService.getCurrentUser();
        rideOfferService.deleteRideOffer(id, currentUser);
        return ResponseEntity.ok().build();
    }
  
    /**
     * Returns paginated list of all ride offers.
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

}
