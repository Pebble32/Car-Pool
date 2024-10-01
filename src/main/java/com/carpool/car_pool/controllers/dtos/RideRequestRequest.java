package com.carpool.car_pool.controllers.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestRequest {

    @NotNull(message = "Ride offer ID cannot be null")
    private Long rideOfferId;
}
