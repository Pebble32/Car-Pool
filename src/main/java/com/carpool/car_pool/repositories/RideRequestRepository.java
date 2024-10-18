package com.carpool.car_pool.repositories;

import com.carpool.car_pool.repositories.entities.RideRequestsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link RideRequestsEntity} persistence.
 */
@Repository
public interface RideRequestRepository extends JpaRepository<RideRequestsEntity, Long>, JpaSpecificationExecutor<RideRequestsEntity> {

    /**
     * Finds a ride request by its associated ride offer ID.
     *
     * @param rideOfferId The ID of the ride offer.
     * @return An {@link Optional} containing the {@link RideRequestsEntity} if found, or empty otherwise.
     */
    Optional<RideRequestsEntity> findByRideOfferId(Long rideOfferId);

    /**
     * Checks if a ride request exists for a given ride offer ID and requester ID.
     *
     * @param rideOfferId The ID of the ride offer.
     * @param requesterId The ID of the requester.
     * @return {@code true} if such a ride request exists, {@code false} otherwise.
     */
    boolean existsByRideOfferIdAndRequesterId(Long rideOfferId, Long requesterId);
}
