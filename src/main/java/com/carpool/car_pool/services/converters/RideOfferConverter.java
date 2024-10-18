package com.carpool.car_pool.services.converters;

import com.carpool.car_pool.controllers.dtos.RideOfferRequest;
import com.carpool.car_pool.controllers.dtos.RideOfferResponse;
import com.carpool.car_pool.repositories.entities.RideOfferEntity;
import com.carpool.car_pool.repositories.entities.RideStatus;
import org.springframework.stereotype.Component;

/**
 * Converter for mapping between {@link RideOfferRequest}, {@link RideOfferResponse}, and {@link RideOfferEntity}.
 */
@Component
public class RideOfferConverter {

    /**
     * Converts a {@link RideOfferRequest} DTO to a {@link RideOfferEntity}.
     *
     * @param rq The {@link RideOfferRequest} to convert.
     * @return The corresponding {@link RideOfferEntity}.
     */
    public RideOfferEntity dtoToEntity(RideOfferRequest rq) {
        return RideOfferEntity.builder()
                .startLocation(rq.getStartLocation())
                .endLocation(rq.getEndLocation())
                .departureTime(rq.getDepartureTime())
                .availableSeats(rq.getAvailableSeats())
                .status(RideStatus.AVAILABLE)
                .build();
    }

    /**
     * Converts a {@link RideOfferEntity} to a {@link RideOfferResponse} DTO.
     *
     * @param rideOfferEntity The {@link RideOfferEntity} to convert.
     * @return The corresponding {@link RideOfferResponse}.
     */
    public RideOfferResponse entityToDTO(RideOfferEntity rideOfferEntity) {
        return RideOfferResponse.builder()
                .id(rideOfferEntity.getId())
                .startLocation(rideOfferEntity.getStartLocation())
                .endLocation(rideOfferEntity.getEndLocation())
                .departureTime(rideOfferEntity.getDepartureTime())
                .availableSeats(rideOfferEntity.getAvailableSeats())
                .status(rideOfferEntity.getStatus())
                .creatorEmail(rideOfferEntity.getCreator().getEmail())
                .build();
    }
}
