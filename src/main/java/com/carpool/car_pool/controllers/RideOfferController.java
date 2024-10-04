package com.carpool.car_pool.controllers;

import com.carpool.car_pool.controllers.dtos.RideOfferRequest;
import com.carpool.car_pool.controllers.dtos.RideOfferResponse;
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

    @GetMapping("/all")
    public ResponseEntity<List<RideOfferResponse>> findAllRideOffers (){
        return ResponseEntity.ok(rideOfferService.findAllRideOffers());
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createRideOffer(
            @RequestBody @Valid RideOfferRequest rideOfferRequest,
            // TODO: This is ugly and bad solution should be changed after implementing security
            @RequestHeader("X-User-Email") String userEmail
    ) {
        return ResponseEntity.ok(rideOfferService.createRideOffer(rideOfferRequest, userEmail));
    }

    @GetMapping("/details")
    public ResponseEntity<RideOfferResponse> viewRideOfferDetails(
            @RequestParam @Valid Long ID
            )
        {
        return ResponseEntity.ok(rideOfferService.viewRideOfferDetail(ID));
    }
}
