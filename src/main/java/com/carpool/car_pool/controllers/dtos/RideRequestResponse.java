package com.carpool.car_pool.controllers.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class RideRequestResponse {

    private Long id;
    private String requestStatus;
    private Long rideOfferId;
    private String requesterEmail;
    private LocalDateTime requestDate;
}
