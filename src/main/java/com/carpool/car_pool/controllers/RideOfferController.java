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

    @GetMapping("/details")
    public ResponseEntity<RideOfferResponse> viewRideOfferDetails(
            @RequestParam @Valid Long ID
            ) {
        return ResponseEntity.ok(rideOfferService.viewRideOfferDetail(ID));
    }

    @PutMapping("/details")
    public ResponseEntity<RideOfferResponse>  editRideOfferDetails(
            @RequestBody EditRideOfferRequest editRideofferRequest
    ){
        UserEntity currentUser = currentUserService.getCurrentUser();
        return ResponseEntity.ok(rideOfferService.editRideOfferDetail(editRideofferRequest, currentUser));
    }

}
