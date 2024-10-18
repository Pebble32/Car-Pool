package com.carpool.car_pool.services.converters;

import com.carpool.car_pool.controllers.dtos.RideRequestResponse;
import com.carpool.car_pool.repositories.entities.RideRequestsEntity;
import org.springframework.stereotype.Component;

/**
 * Converter for mapping between {@link RideRequestsEntity} and {@link RideRequestResponse} DTO.
 */
@Component
public class RideRequestConverter {

    /**
     * Converts a {@link RideRequestsEntity} to a {@link RideRequestResponse} DTO.
     *
     * @param rideRequestsEntity The {@link RideRequestsEntity} to convert.
     * @return The corresponding {@link RideRequestResponse}.
     */
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
