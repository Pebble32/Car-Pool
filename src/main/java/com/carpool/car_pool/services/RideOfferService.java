package com.carpool.car_pool.services;

import com.carpool.car_pool.controllers.dtos.EditRideOfferRequest;
import com.carpool.car_pool.controllers.dtos.RideOfferRequest;
import com.carpool.car_pool.controllers.dtos.RideOfferResponse;
import com.carpool.car_pool.controllers.dtos.UserResponse;
import com.carpool.car_pool.repositories.RideOfferRepository;
import com.carpool.car_pool.repositories.RideRequestRepository;
import com.carpool.car_pool.repositories.common.PageResponse;
import com.carpool.car_pool.repositories.entities.RequestStatus;
import com.carpool.car_pool.repositories.entities.RideOfferEntity;
import com.carpool.car_pool.repositories.entities.RideRequestsEntity;
import com.carpool.car_pool.repositories.entities.RideStatus;
import com.carpool.car_pool.repositories.entities.UserEntity;
import com.carpool.car_pool.repositories.specifications.RideOfferSpecifications;
import com.carpool.car_pool.services.converters.RideOfferConverter;
import com.carpool.car_pool.services.converters.UserConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
     * Filters ride offers based on the provided parameters.
     * @param startLocation The starting location of the ride.
     * @param endLocation The destination of the ride.
     * @param departureTime The departure time of the ride.
     * @param page The page number (zero-based).
     * @param size The size of the page.
     * @return A {@link PageResponse} containing ride offers.
     */
    public PageResponse<RideOfferResponse> filterRides(String startLocation, String endLocation, LocalDateTime departureTime, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("departureTime").ascending());

        Specification<RideOfferEntity> spec = Specification.where(null);

        if (startLocation != null && !startLocation.isEmpty()) {
            spec = spec.and(RideOfferSpecifications.hasStartLocation(startLocation));
        }
        if (endLocation != null && !endLocation.isEmpty()) {
            spec = spec.and(RideOfferSpecifications.hasEndLocation(endLocation));
        }
        if (departureTime != null) {
            spec = spec.and(RideOfferSpecifications.hasDepartureTime(departureTime));
        }

        return getRideOfferResponsePageResponse(pageable, spec);

    }

    /**
     * Searches for ride offers based on the provided keyword
     * @param keyword The keyword used for the search
     * @param page The page number (zero-based).
     * @param size The size of the page.
     * @return  A {@link PageResponse} containing ride offers.
     */
    public PageResponse<RideOfferResponse> searchForRides(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("departureTime").ascending());

        Specification<RideOfferEntity> spec = Specification.where(RideOfferSpecifications.containsKeyword(keyword));

        return getRideOfferResponsePageResponse(pageable, spec);
    }

    /**
     * Helper function to reduce duplicate code
     * @param pageable PageRequest
     * @param spec Specifications
     * @return PageResponse
     */
    private PageResponse<RideOfferResponse> getRideOfferResponsePageResponse(Pageable pageable, Specification<RideOfferEntity> spec) {
        Page<RideOfferEntity> rideOfferPage = rideOfferRepository.findAll(spec, pageable);

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
        return new ArrayList<>(rideOfferRepository.findAll()
                .stream()
                .map(RideOfferEntity::getCreator)
                .map(userConverter::entityToDTO)
                .collect(Collectors.toMap(
                        UserResponse::getId,
                        user -> user,
                        (existing, replacement) -> existing
                ))
                .values());
    }


    /**
     * Scheduled task to delete ride offers that have finished two years ago along with their associated ride requests.
     * Runs daily at 2 AM.
     */
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void deleteFinishedRide() {
        // Cutoff date is two years ago from now
        LocalDateTime twoYearsAgo = LocalDateTime.now().minusYears(2);

        // Specifications to find all ride offers that are finished or cancelled before the cutoff date
        List<RideOfferEntity> rideOffersToDelete = rideOfferRepository.findAll(
                RideOfferSpecifications.hasStatus(RideStatus.FINISHED)
                        .or(RideOfferSpecifications.hasStatus(RideStatus.CANCELLED))
                        .and(RideOfferSpecifications.isFinishedBefore(twoYearsAgo))
        );

        //TODO add some logging mechanism here


        rideOfferRepository.deleteAll(rideOffersToDelete);

        //log.info("Deleted {} finished ride offers and their associated ride requests.", rideOffersToDelete.size());
    }

    /**
     * Marks a ride offer as finished.
     *
     * @param id   The ID of the ride offer to mark as finished.
     * @param user The {@link UserEntity} attempting to mark the ride as finished.
     * @throws RuntimeException   if the ride offer does not exist.
     * @throws RuntimeException if the user is not authorized to mark the ride as finished.
     */
    @Transactional
    public void rideFinished(Long id, UserEntity user) {
        RideOfferEntity rideOffer = rideOfferRepository.findById(id)
                // TODO add RideOfferNotFoundException | Global exception handler
                .orElseThrow(() -> new RuntimeException("Ride offer does not exist"));

        if (!rideOffer.getCreator().equals(user)) {
            //TODO add UnauthorizedActionException |  Global exception handler
            throw new RuntimeException("Only owner can mark this ride offer as finished");
        }

        rideOffer.setStatus(RideStatus.FINISHED);
        rideOfferRepository.save(rideOffer);

        List<RideRequestsEntity> rideRequests = rideOffer.getRideRequests();
        for (RideRequestsEntity request : rideRequests) {
            if (request.getRequestStatus() == RequestStatus.PENDING) {
                request.setRequestStatus(RequestStatus.REJECTED);
                rideRequestRepository.save(request);
            }
        }
    }

    /**
     * Scheduled task to automatically mark rides as finished the day after their departure.
     * Runs daily at 1 AM.
     */
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void autoMarkRidesAsFinished() {
        // cutoff date is one day ago from now
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);

        // specifications to find all ride offers that departed yesterday and are not marked finished yet
        List<RideOfferEntity> ridesToMarkFinished = rideOfferRepository.findAll(
                RideOfferSpecifications.hasStatus(RideStatus.AVAILABLE)
                        .or(RideOfferSpecifications.hasStatus(RideStatus.UNAVAILABLE))
                        .and(RideOfferSpecifications.isFinishedBefore(yesterday))
        );

        // TODO add some logging here in case no ride was found

        // Mark each ride as finished
        for (RideOfferEntity rideOffer : ridesToMarkFinished) {
            rideFinished(rideOffer.getId(), rideOffer.getCreator());
        }

    }
}
