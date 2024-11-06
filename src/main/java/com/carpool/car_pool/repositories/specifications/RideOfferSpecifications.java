package com.carpool.car_pool.repositories.specifications;

import com.carpool.car_pool.repositories.entities.RideOfferEntity;
import com.carpool.car_pool.repositories.entities.RideStatus;
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
     * Specification to filter ride offers by their status.
     *
     * @param status The {@link RideStatus} to filter by.
     * @return A {@link Specification} for {@link RideOfferEntity}.
     */
    public static Specification<RideOfferEntity> hasStatus(RideStatus status) {
        return (root, query, builder) -> builder.equal(root.get("status"), status);
    }

    /**
     * Specification to filter ride offers that have a departure time before the specified date.
     *
     * @param finishedBefore The cutoff {@link LocalDateTime} to determine if a ride offer is finished.
     * @return A {@link Specification} for {@link RideOfferEntity}.
     */
    public static Specification<RideOfferEntity> isFinishedBefore(LocalDateTime finishedBefore) {
        return (root, query, builder) -> builder.lessThan(root.get("departureTime"), finishedBefore);
    }
    /**
     * Specification to filter ride offers by departure time.
     * @param departureTime The departure time of the ride offer.
     * @return Specification for departure time.
     */
    public static Specification<RideOfferEntity> hasDepartureTime(LocalDateTime departureTime) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("departureTime"), departureTime);
    }

    /**
     * Specification to search for ride offers by a keyword that can be either the startLocation, endLocation or the creator
     * of the ride.
     * @param keyword The keyword being used for the search
     * @return Specification for the keyword
     */
    public static Specification<RideOfferEntity> containsKeyword(String keyword){
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(root.get("startLocation"), "%" + keyword + "%"),
                criteriaBuilder.like(root.get("endLocation"), "%" + keyword + "%"),
                criteriaBuilder.like(root.join("creator").get("email"), "%" + keyword + "%"),
                criteriaBuilder.like(root.join("creator").get("firstname"), "%" + keyword + "%")
        );
    }
}
