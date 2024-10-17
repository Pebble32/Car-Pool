package com.carpool.car_pool.repositories.specifications;

import com.carpool.car_pool.repositories.entities.RideRequestsEntity;
import com.carpool.car_pool.repositories.entities.RequestStatus;
import com.carpool.car_pool.repositories.entities.UserEntity;
import org.springframework.data.jpa.domain.Specification;

public class RideRequestSpecifications {

    /**
     * Specification to filter ride requests by requester.
     *
     * @param requester The user who made the ride request.
     * @return Specification for requester.
     */
    public static Specification<RideRequestsEntity> hasRequester(UserEntity requester) {
        return (root, query, builder) -> builder.equal(root.get("requester"), requester);
    }

    /**
     * Specification to filter ride requests by status.
     *
     * @param status The status of the ride request.
     * @return Specification for status.
     */
    public static Specification<RideRequestsEntity> hasStatus(RequestStatus status) {
        return (root, query, builder) -> builder.equal(root.get("requestStatus"), status);
    }

    /**
     * Specification to filter ride requests by ride offer ID.
     *
     * @param rideOfferId The ID of the ride offer.
     * @return Specification for ride offer ID.
     */
    public static Specification<RideRequestsEntity> hasRideOfferId(Long rideOfferId) {
        return (root, query, builder) -> builder.equal(root.get("rideOffer").get("id"), rideOfferId);
    }
}
