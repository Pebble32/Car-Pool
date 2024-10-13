package com.carpool.car_pool.repositories;

import com.carpool.car_pool.repositories.entities.RideRequestsEntity;
import com.carpool.car_pool.repositories.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RideRequestRepository extends JpaRepository<RideRequestsEntity, Long> {
    Optional<RideRequestsEntity> findByRideOfferId(Long rideOfferId);
    Optional<RideRequestsEntity> findByRequester(UserEntity requester);
    boolean existsByRideOfferIdAndRequesterId(Long rideOfferId, Long requesterId);
}
