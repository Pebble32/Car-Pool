package com.carpool.car_pool.services.converters;

import com.carpool.car_pool.controllers.dtos.RideOfferRequest;
import com.carpool.car_pool.controllers.dtos.RideOfferResponse;
import com.carpool.car_pool.repositories.entities.RideOfferEntity;
import com.carpool.car_pool.repositories.entities.RideStatus;
import org.springframework.stereotype.Component;

@Component
public class RideOfferConverter {

    public RideOfferEntity dtoToEntity(RideOfferRequest rq) {
        return RideOfferEntity.builder()
                .startLocation(rq.getStartLocation())
                .endLocation(rq.getEndLocation())
                .departureTime(rq.getDepartureTime())
                .availableSeats(rq.getAvailableSeats())
                .status(RideStatus.AVAILABLE)
                .build();
    }

    public RideOfferResponse entityToDTO(RideOfferEntity rideOfferEntity) {
        return RideOfferResponse.builder()
                .id(rideOfferEntity.getId())
                .startLocation(rideOfferEntity.getStartLocation())
                .endLocation(rideOfferEntity.getEndLocation())
                .departureTime(rideOfferEntity.getDepartureTime())
                .availableSeats(rideOfferEntity.getAvailableSeats())
                .status(rideOfferEntity.getStatus())
                .creatorEmail(rideOfferEntity.getCreator().getEmail())
                .createdAt(rideOfferEntity.getCreatedDate())
                .lastModified(rideOfferEntity.getLastModifiedDate())
                .build();
    }
}
