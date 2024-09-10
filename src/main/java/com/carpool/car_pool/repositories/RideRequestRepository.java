package com.carpool.car_pool.repositories;

import com.carpool.car_pool.repositories.entities.RideRequestsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRequestRepository extends JpaRepository<RideRequestsEntity, Long> {
}
