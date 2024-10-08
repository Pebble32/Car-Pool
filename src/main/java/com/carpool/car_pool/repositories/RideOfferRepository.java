package com.carpool.car_pool.repositories;

import com.carpool.car_pool.repositories.entities.RideOfferEntity;
import com.carpool.car_pool.repositories.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RideOfferRepository extends JpaRepository<RideOfferEntity, Long> {
    Optional<RideOfferEntity> findByCreator(UserEntity creator);
}
