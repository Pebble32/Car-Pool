package com.carpool.car_pool.services;

import com.carpool.car_pool.controllers.dtos.RideOfferResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("offers")
@RequiredArgsConstructor
@Tag(name = "RideOffer")
public class RideOfferController {

    private final RideOfferService rideOfferService;

    @GetMapping
    public ResponseEntity<List<RideOfferResponse>> findAllRideOffers (){
        return ResponseEntity.ok(rideOfferService.findAllRideOffers());
    }

}
