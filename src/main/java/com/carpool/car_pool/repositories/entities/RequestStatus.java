package com.carpool.car_pool.repositories.entities;

public enum RequestStatus {
    PENDING, // Default value
    ACCEPTED, // Accepted by provider
    REJECTED, // Rejected by provider
    CANCELED // Canceled by request creator
}
