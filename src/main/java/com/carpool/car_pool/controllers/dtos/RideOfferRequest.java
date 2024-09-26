package com.carpool.car_pool.controllers.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RideOfferRequest {

    @NotBlank(message = "Start location cannot be empty")
    private String startLocation;

    @NotBlank(message = "Destination cannot be empty")
    private String endLocation;

    @NotNull(message = "Departure time cannot be null")
    @Future(message = "Departure time must be in the future")
    private LocalDateTime departureTime;

    @NotNull(message = "Available seats cannot be null")
    @Min(value = 1, message = "There must be at least one available seat")
    private Integer availableSeats;
}
