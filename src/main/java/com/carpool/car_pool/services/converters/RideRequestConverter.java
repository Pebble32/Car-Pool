package com.carpool.car_pool.services.converters;

import com.carpool.car_pool.controllers.dtos.RideRequestResponse;
import com.carpool.car_pool.repositories.entities.RideRequestsEntity;
import org.springframework.stereotype.Component;

@Component
public class RideRequestConverter {

    public RideRequestResponse entityToDTO(RideRequestsEntity rideRequestsEntity) {
        return RideRequestResponse.builder()
                .id(rideRequestsEntity.getId())
                .requestStatus(rideRequestsEntity.getRequestStatus().name())
                .rideOfferId(rideRequestsEntity.getRideOffer().getId())
                .requesterEmail(rideRequestsEntity.getRequester().getEmail())
                .requestDate(rideRequestsEntity.getCreatedDate())
                .build();
    }
}
