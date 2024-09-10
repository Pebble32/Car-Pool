package com.carpool.car_pool.services.converters;

import com.carpool.car_pool.controllers.dtos.RideOfferResponse;
import com.carpool.car_pool.repositories.entities.RideOfferEntity;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class RideOfferConverter {

    public RideOfferResponse entityToDTO(RideOfferEntity rideOfferEntity) {
        return RideOfferResponse.builder()
                .availableSeats(rideOfferEntity.getAvailableSeats())
                .departureTime(rideOfferEntity.getDepartureTime())
                .startLocation(rideOfferEntity.getStartLocation())
                .status(rideOfferEntity.getStatus())
                .build();
    }
}
