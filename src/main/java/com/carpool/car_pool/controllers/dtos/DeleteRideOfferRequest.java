package com.carpool.car_pool.controllers.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DeleteRideOfferRequest {
    private Long rideId;
}
