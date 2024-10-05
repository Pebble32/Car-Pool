package com.carpool.car_pool.repositories.entities;

public enum RideStatus {
    CANCELLED, // Set by user
    FINISHED, // Set automatically after ride is finished
    AVAILABLE, // Default value
    UNAVAILABLE, // When seats are full
}
