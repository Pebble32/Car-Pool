package com.carpool.car_pool.services;

import com.carpool.car_pool.controllers.dtos.RideOfferResponse;
import com.carpool.car_pool.repositories.RideOfferRepository;
import com.carpool.car_pool.repositories.entities.RideOfferEntity;
import com.carpool.car_pool.services.converters.RideOfferConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RideOfferService {

    private final RideOfferRepository rideOfferRepository;
    private final RideOfferConverter rideOfferConverter;

    public List<RideOfferResponse> findAllRideOffers() {
        List<RideOfferEntity> rideOfferEntities = rideOfferRepository.findAll();
        return rideOfferEntities
                .stream()
                .map(rideOfferConverter::entityToDTO)
                .toList();
    }
}
