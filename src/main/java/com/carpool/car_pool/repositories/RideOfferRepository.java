package com.carpool.car_pool.repositories;

import com.carpool.car_pool.repositories.entities.RideOfferEntity;
import com.carpool.car_pool.repositories.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link RideOfferEntity} persistence.
 */
@Repository
public interface RideOfferRepository extends JpaRepository<RideOfferEntity, Long>, JpaSpecificationExecutor<RideOfferEntity> {

    /**
     * Finds ride offers created by a specific user.
     *
     * @param creator The {@link UserEntity} who created the ride offers.
     * @return An {@link Optional} containing the {@link RideOfferEntity} if found, or empty otherwise.
     */
    Optional<List<RideOfferEntity>> findByCreator(UserEntity creator);
}
