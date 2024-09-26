package com.carpool.car_pool.controllers.dtos;

import com.carpool.car_pool.repositories.entities.RideStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RideOfferResponse {

    private Long id;
    private String startLocation;
    private Integer availableSeats;
    private LocalDateTime departureTime;
    private RideStatus status;
    private String creatorEmail;
    private LocalDateTime lastModified;
    private LocalDateTime createdAt;
}
