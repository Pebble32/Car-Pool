package com.carpool.car_pool.controllers;

import com.carpool.car_pool.controllers.dtos.EditRideOfferRequest;
import com.carpool.car_pool.controllers.dtos.RideOfferRequest;
import com.carpool.car_pool.controllers.dtos.RideOfferResponse;
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

    @GetMapping("/details/{id}")
    public ResponseEntity<RideOfferResponse> viewRideOfferDetails(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(rideOfferService.viewRideOfferDetail(id));
    }

    @PutMapping("/details/{id}")
    public ResponseEntity<RideOfferResponse>  editRideOfferDetails(
            @RequestBody EditRideOfferRequest editRideOfferRequest,
            @PathVariable String id // TODO: does this make sense? should I check if id == editRideOfferRequest.id?
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
        return ResponseEntity.noContent().build();
    }
}
