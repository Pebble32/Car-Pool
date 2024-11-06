package com.carpool.car_pool.controllers.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EditRideRequestRequest {

    @NotNull(message = "Ride request ID cannot be null")
    private Long rideRequestId;

    @NotNull(message = "New request status cannot be null")
    private RideRequestStatus status;

    public enum RideRequestStatus{
        CANCELED,
        REACTIVATED
    }

}
