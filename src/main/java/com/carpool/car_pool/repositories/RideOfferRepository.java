package com.carpool.car_pool.repositories;

import com.carpool.car_pool.repositories.entities.RideOfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideOfferRepository extends JpaRepository<RideOfferEntity, Long> {
}
