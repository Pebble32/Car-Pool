package com.carpool.car_pool.services;

import com.carpool.car_pool.controllers.dtos.EditRideOfferRequest;
import com.carpool.car_pool.controllers.dtos.RideOfferRequest;
import com.carpool.car_pool.controllers.dtos.RideOfferResponse;
import com.carpool.car_pool.controllers.dtos.UserResponse;
import com.carpool.car_pool.repositories.RideOfferRepository;
import com.carpool.car_pool.repositories.RideRequestRepository;
import com.carpool.car_pool.repositories.common.PageResponse;
import com.carpool.car_pool.repositories.entities.*;
import com.carpool.car_pool.services.converters.RideOfferConverter;
import com.carpool.car_pool.services.converters.UserConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing ride offers.
 */
@Service
@RequiredArgsConstructor
public class RideOfferService {

    private final RideOfferRepository rideOfferRepository;
    private final RideOfferConverter rideOfferConverter;
    private final RideRequestRepository rideRequestRepository;
    private final UserConverter userConverter;

    /**
     * Retrieves all ride offers.
     *
     * @return A list of {@link RideOfferResponse} representing all ride offers.
     */
    public List<RideOfferResponse> findAllRideOffers() {
        List<RideOfferEntity> rideOfferEntities = rideOfferRepository.findAll();
        return rideOfferEntities
                .stream()
                .map(rideOfferConverter::entityToDTO)
                .toList();
    }

    // TODO: If security is implemented change to ApplicationAuditAware
    //  with UserPrincipal using Jwt tokens to keep track of who is logged in and sending requests

    /**
     * Creates a new ride offer.
     *
     * @param request The {@link RideOfferRequest} containing details of the ride offer.
     * @param user    The {@link UserEntity} creating the ride offer.
     * @return The ID of the created ride offer.
     * @throws RuntimeException if the creation fails.
     */
    @Transactional
    public Long createRideOffer(@Valid RideOfferRequest request, UserEntity user) {
        RideOfferEntity rideOffer = rideOfferConverter.dtoToEntity(request);
        rideOffer.setCreator(user);

        return rideOfferRepository.save(rideOffer).getId();
    }

    /**
     * Retrieves detailed information about a specific ride offer.
     *
     * @param id The ID of the ride offer.
     * @return The {@link RideOfferResponse} containing ride offer details.
     * @throws RuntimeException if the ride offer does not exist.
     */
    public RideOfferResponse viewRideOfferDetail(Long id) {
        return rideOfferConverter.entityToDTO(rideOfferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ride offer with this id does not exist")));
    }

    /**
     * Edits the details of an existing ride offer.
     *
     * @param editRideofferRequest The {@link EditRideOfferRequest} containing updated ride offer details.
     * @param user                 The {@link UserEntity} attempting to edit the ride offer.
     * @return The updated {@link RideOfferResponse}.
     * @throws RuntimeException if the ride offer does not exist or the user is not authorized to edit it.
     */
    public RideOfferResponse editRideOfferDetail(EditRideOfferRequest editRideofferRequest, UserEntity user) {
        RideOfferEntity ride = rideOfferRepository.findById(editRideofferRequest.getRideId())
                .orElseThrow(() -> new RuntimeException("Ride offer does not exist"));

        if (!ride.getCreator().equals(user)) {
            throw new RuntimeException("Only owner can edit ride offer");
        }

        if (!editRideofferRequest.getRideStatus().equals(ride.getStatus().toString())) {
            if (editRideofferRequest.getRideStatus().equals(RideStatus.CANCELLED.toString())) {
                ride.setStatus(RideStatus.CANCELLED);
                List<RideRequestsEntity> requests = ride.getRideRequests();
                for (RideRequestsEntity request : requests) {
                    request.setRequestStatus(RequestStatus.REJECTED);
                    rideRequestRepository.save(request);
                }
            }
            ride.setStatus(RideStatus.valueOf(editRideofferRequest.getRideStatus()));
        }

        if (!editRideofferRequest.getStartLocation().equals(ride.getStartLocation())) {
            ride.setStartLocation(editRideofferRequest.getStartLocation());
        }
        if (!editRideofferRequest.getEndLocation().equals(ride.getEndLocation())) {
            ride.setEndLocation(editRideofferRequest.getEndLocation());
        }

        if (!editRideofferRequest.getDepartureTime().equals(ride.getDepartureTime())) {
            ride.setDepartureTime(editRideofferRequest.getDepartureTime());
        }

        if (!editRideofferRequest.getAvailableSeats().equals(ride.getAvailableSeats())) {
            ride.setAvailableSeats(editRideofferRequest.getAvailableSeats());
        }

        RideOfferEntity updatedRide = rideOfferRepository.save(ride);

        return rideOfferConverter.entityToDTO(updatedRide);
    }

    /**
     * Retrieves a paginated list of ride offers.
     *
     * @param page The page number (zero-based).
     * @param size The size of the page.
     * @return A {@link PageResponse} containing ride offers.
     */
    public PageResponse<RideOfferResponse> findAllRideOffersPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("departureTime").ascending());
        Page<RideOfferEntity> rideOfferPage = rideOfferRepository.findAll(pageable);

        List<RideOfferResponse> rideOffers = rideOfferPage.stream()
                .map(rideOfferConverter::entityToDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(
                rideOffers,
                rideOfferPage.getNumber(),
                rideOfferPage.getSize(),
                rideOfferPage.getTotalElements(),
                rideOfferPage.getTotalPages(),
                rideOfferPage.isFirst(),
                rideOfferPage.isLast()
        );
    }

    /**
     * Deletes a ride offer.
     *
     * @param id   The ID of the ride offer to delete.
     * @param user The {@link UserEntity} attempting to delete the ride offer.
     * @throws RuntimeException if the ride offer does not exist or the user is not authorized to delete it.
     */
    @Transactional
    public void deleteRideOffer(long id, UserEntity user) {
        RideOfferEntity rideOffer = rideOfferRepository.findById(id)
                //TODO Add global exception handling
                .orElseThrow(() -> new RuntimeException("Ride offer does not exist"));

        if (!rideOffer.getCreator().equals(user)) {
            throw new RuntimeException("Only owner can delete this ride offer");
        }

        // TODO: When notification is implemented we should notify all requests for the ride offer here
        rideOfferRepository.delete(rideOffer);
    }

    /**
     * Retrieves all providers of ride offers.
     *
     * @return A list of {@link UserEntity} representing all providers.
     */
    public List<UserResponse> getAllProviders() {
        return rideOfferRepository.findAll()
                .stream()
                .map(RideOfferEntity::getCreator)
                .map(userConverter::entityToDTO)
                .distinct()
                .collect(Collectors.toList());
    }
}
