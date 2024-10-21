package com.carpool.car_pool.repositories.specifications;

import com.carpool.car_pool.repositories.entities.RideOfferEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class RideOfferSpecifications {

    /**
     * Specification to filter ride offers by start location.
     * @param startLocation The start location of the ride offer.
     * @return Specification for start location.
     */
    public static Specification<RideOfferEntity> hasStartLocation(String startLocation) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("startLocation"), startLocation);
    }

    /**
     * Specification to filter ride offers by end location.
     * @param endLocation The end location of the ride offer.
     * @return Specification for end location.
     */
    public static Specification<RideOfferEntity> hasEndLocation(String endLocation) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("endLocation"), endLocation);
    }

    /**
     * Specification to filter ride offers by departure time.
     * @param departureTime The departure time of the ride offer.
     * @return Specification for departure time.
     */
    public static Specification<RideOfferEntity> hasDepartureTime(LocalDateTime departureTime) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("departureTime"), departureTime);
    }
}
