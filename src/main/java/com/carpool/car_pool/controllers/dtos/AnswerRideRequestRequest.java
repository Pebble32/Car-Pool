package com.carpool.car_pool.controllers.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnswerRideRequestRequest {

    @NotNull(message = "Ride request id cannot be null")
    private Long rideRequestId;

    @NotNull(message = "Request status cannot be null")
    private AnswerStatus answerStatus;

    public enum AnswerStatus {
        ACCEPTED,
        REJECTED
    }

}
