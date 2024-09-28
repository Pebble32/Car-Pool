package com.carpool.car_pool.services;

import com.carpool.car_pool.controllers.dtos.RideRequestRequest;
import com.carpool.car_pool.repositories.RideOfferRepository;
import com.carpool.car_pool.repositories.UserRepository;
import com.carpool.car_pool.repositories.entities.RideRequestsEntity;
import com.carpool.car_pool.services.converters.RideRequestConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.carpool.car_pool.repositories.entities.RequestStatus.PENDING;

@Service
@RequiredArgsConstructor
public class RideRequestService {


    private final RideOfferRepository rideOfferRepository;
    private final UserRepository userRepository;
    private final RideRequestConverter rideRequestConverter;

    public Long createRideOffer(@Valid RideRequestRequest request, String userEmail) {
        var rideOffer = rideOfferRepository.findById(request.getRideOfferId())
                // TODO: Better exception handling RideOfferNotFoundException
                .orElseThrow(() -> new RuntimeException("Ride Offer Not Found"));

        var rideRequster = userRepository.findByEmail(userEmail)
                // TODO: Better exception handling UserNotFoundException
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (rideOffer.getAvailableSeats() <= 0) throw new RuntimeException("Ride Offer Not Available");

        var rideRequestEntity = RideRequestsEntity.builder()
                .rideOffer(rideOffer)
                .requester(rideRequster)
                .requestStatus(PENDING)
                .build();


    }
}
