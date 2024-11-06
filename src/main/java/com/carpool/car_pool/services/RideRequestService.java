package com.carpool.car_pool.services;


import com.carpool.car_pool.controllers.dtos.AnswerRideRequestRequest;
import com.carpool.car_pool.controllers.dtos.EditRideRequestRequest;
import com.carpool.car_pool.controllers.dtos.RideRequestRequest;
import com.carpool.car_pool.controllers.dtos.RideRequestResponse;
import com.carpool.car_pool.repositories.RideOfferRepository;
import com.carpool.car_pool.repositories.RideRequestRepository;
import com.carpool.car_pool.repositories.common.PageResponse;
import com.carpool.car_pool.repositories.entities.RequestStatus;
import com.carpool.car_pool.repositories.entities.RideOfferEntity;
import com.carpool.car_pool.repositories.entities.RideRequestsEntity;
import com.carpool.car_pool.repositories.entities.RideStatus;
import com.carpool.car_pool.repositories.entities.UserEntity;
import com.carpool.car_pool.repositories.specifications.RideRequestSpecifications;
import com.carpool.car_pool.services.converters.RideRequestConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.carpool.car_pool.repositories.entities.RequestStatus.CANCELED;
import static com.carpool.car_pool.repositories.entities.RequestStatus.PENDING;
import static com.carpool.car_pool.repositories.entities.RideStatus.UNAVAILABLE;

/**
 * Service for managing ride requests.
 */
@Service
@RequiredArgsConstructor
public class RideRequestService {

    private final RideOfferRepository rideOfferRepository;
    private final RideRequestRepository rideRequestRepository;
    private final RideRequestConverter rideRequestConverter;
    private final UserService userService;

    /**
     * Creates a new ride request for a specific ride offer.
     *
     * @param request      The {@link RideRequestRequest} containing ride offer ID and request details.
     * @param currentUser The {@link UserEntity} making the ride request.
     * @return The ID of the created ride request.
     * @throws RuntimeException if the ride offer is not available, the user is the creator, or a request already exists.
     */
    @Transactional
    public Long createRideRequest(@Valid RideRequestRequest request, UserEntity currentUser) {
        var rideOffer = rideOfferRepository.findById(request.getRideOfferId())
                // TODO: Better exception handling RideOfferNotFoundException
                .orElseThrow(() -> new RuntimeException("Ride Offer Not Found"));

        if (rideOffer.getAvailableSeats() <= 0) {
            rideOffer.setStatus(UNAVAILABLE);
            rideOfferRepository.save(rideOffer);
            throw new RuntimeException("Ride Offer Not Available");
        }

        if (rideOffer.getCreator().equals(currentUser)) {
            throw new RuntimeException("You can not request a ride on your own offer");
        }

        if (!rideOffer.getStatus().equals(RideStatus.AVAILABLE)) {
            throw new RuntimeException("Ride Offer Not Available");
        }

        boolean alreadyRequested = rideRequestRepository.existsByRideOfferIdAndRequesterId(request.getRideOfferId(), currentUser.getId());
        if (alreadyRequested) {
            throw new RuntimeException("You have already requested this Ride Offer");
        }

        var rideRequestEntity = RideRequestsEntity.builder()
                .rideOffer(rideOffer)
                .requester(currentUser)
                .requestStatus(PENDING)
                .build();

        userService.sendRideRequestNotification(rideOffer.getCreator(), rideRequestEntity);

        return rideRequestRepository.save(rideRequestEntity).getId();
    }

    /**
     * Retrieves all ride requests for a specific ride offer.
     *
     * @param rideOfferId  The ID of the ride offer.
     * @param currentUser The {@link UserEntity} requesting the ride requests.
     * @return A list of {@link RideRequestResponse} representing the ride requests.
     * @throws RuntimeException if the ride offer is not found or the user is not authorized.
     */
    public List<RideRequestResponse> getRideRequestsForRideOffer(Long rideOfferId, UserEntity currentUser) {
        var rideOffer = rideOfferRepository.findById(rideOfferId)
                // TODO: Global exception handler RideOfferNotFoundException
                .orElseThrow(() -> new RuntimeException("Ride Offer Not Found"));

        if (!rideOffer.getCreator().equals(currentUser)) {
            // TODO: Global exception handler UnauthorizedAccessException
            throw new RuntimeException("Ride Offer Not Found");
        }

        return rideRequestRepository.findByRideOfferId(rideOfferId)
                .stream()
                .map(rideRequestConverter::entityToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Processes an answer to a ride request, either accepting or rejecting it.
     *
     * @param request      The {@link AnswerRideRequestRequest} containing the ride request ID and the answer status.
     * @param currentUser The {@link UserEntity} answering the ride request.
     * @return The updated {@link RideRequestResponse}.
     * @throws RuntimeException if the ride request is invalid or the user is not authorized.
     */
    public RideRequestResponse answerRideRequest(@Valid AnswerRideRequestRequest request, UserEntity currentUser) {
        var rideRequest = rideRequestRepository.findById(request.getRideRequestId())
                .orElseThrow(() -> new RuntimeException("Ride Request Not Found"));

        // This is Ride Request status
        if (rideRequest.getRequestStatus().equals(CANCELED)) {
            throw new RuntimeException("Ride Request is Canceled");
        }

        var rideOffer = rideRequest.getRideOffer();

        if (!rideOffer.getStatus().equals(RideStatus.AVAILABLE)) {
            throw new RuntimeException("Ride Offer is not Available");
        }

        if (!rideOffer.getCreator().equals(currentUser)) {
            //TODO: Global exception handler UnauthorizedAccessException
            throw new RuntimeException("You are not authorized to answer this ride request.");
        }

        if (request.getAnswerStatus() == AnswerRideRequestRequest.AnswerStatus.ACCEPTED) {
            if (rideRequest.getRequestStatus().equals(RequestStatus.ACCEPTED)) {
                throw new RuntimeException("Ride Request already accepted");
            }

            if (rideOffer.getAvailableSeats() <= 0) {
                throw new RuntimeException("No available seats to accept this ride request");
            }

            rideRequest.setRequestStatus(RequestStatus.ACCEPTED);
            rideOffer.setAvailableSeats(rideOffer.getAvailableSeats() - 1);

            if (rideOffer.getAvailableSeats() == 0) {
                rideOffer.setStatus(UNAVAILABLE);
            }

        } else if (request.getAnswerStatus() == AnswerRideRequestRequest.AnswerStatus.REJECTED) {
            rideRequest.setRequestStatus(RequestStatus.REJECTED);
        } else {
            throw new RuntimeException("Invalid Request Status");
        }

        rideRequestRepository.save(rideRequest);
        rideOfferRepository.save(rideOffer);

        return rideRequestConverter.entityToDTO(rideRequest);
    }

    /**
     * Retrieves all ride requests made by the current user.
     *
     * @param currentUser The {@link UserEntity} whose ride requests are to be retrieved.
     * @return A list of {@link RideRequestResponse} representing the user's ride requests.
     */
    public List<RideRequestResponse> getRideRequestsForUser(UserEntity currentUser) {
        Specification<RideRequestsEntity> spec = Specification.where(RideRequestSpecifications.hasRequester(currentUser));

        return rideRequestRepository.findAll(spec)
                .stream()
                .map(rideRequestConverter::entityToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a paginated list of ride requests for a specific ride offer.
     *
     * @param rideOfferId  The ID of the ride offer.
     * @param currentUser The {@link UserEntity} requesting the ride requests.
     * @param page         The page number (zero-based).
     * @param size         The size of the page.
     * @return A {@link PageResponse} containing the paginated ride requests.
     * @throws RuntimeException if the ride offer is not found or the user is not authorized.
     */
    public PageResponse<RideRequestResponse> getRideRequestsForRideOfferPaginated(Long rideOfferId, UserEntity currentUser, int page, int size) {
        var rideOffer = rideOfferRepository.findById(rideOfferId)
                .orElseThrow(() -> new RuntimeException("Ride Offer Not Found"));

        if (!rideOffer.getCreator().equals(currentUser)) {
            throw new RuntimeException("Unauthorized Access");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Specification<RideRequestsEntity> spec = Specification.where(RideRequestSpecifications.hasRideOfferId(rideOfferId));

        return getRideRequestResponsePageResponse(pageable, spec);
    }

    /**
     * Retrieves a paginated list of ride requests made by the current user.
     *
     * @param currentUser The {@link UserEntity} whose ride requests are to be retrieved.
     * @param page        The page number (zero-based).
     * @param size        The size of the page.
     * @return A {@link PageResponse} containing the paginated ride requests.
     */
    public PageResponse<RideRequestResponse> getRideRequestsForUserPaginated(UserEntity currentUser, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Specification<RideRequestsEntity> spec = Specification.where(RideRequestSpecifications.hasRequester(currentUser));

        return getRideRequestResponsePageResponse(pageable, spec);
    }

    /**
     * Helper method to create a {@link PageResponse} from a paginated list of ride requests.
     *
     * @param pageable The {@link Pageable} object defining pagination parameters.
     * @param spec     The {@link Specification} to filter ride requests.
     * @return A {@link PageResponse} containing the paginated ride requests.
     */
    private PageResponse<RideRequestResponse> getRideRequestResponsePageResponse(Pageable pageable, Specification<RideRequestsEntity> spec) {
        Page<RideRequestsEntity> rideRequestsPage = rideRequestRepository.findAll(spec, pageable);

        List<RideRequestResponse> rideRequests = rideRequestsPage.stream()
                .map(rideRequestConverter::entityToDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(
                rideRequests,
                rideRequestsPage.getNumber(),
                rideRequestsPage.getSize(),
                rideRequestsPage.getTotalElements(),
                rideRequestsPage.getTotalPages(),
                rideRequestsPage.isFirst(),
                rideRequestsPage.isLast()
        );
    }


    /**
     * Deletes ride request
     *
     * @param id   The ID of the ride offer to delete.
     * @param currentUser The {@link UserEntity} attempting to delete the ride offer.
     * @throws RuntimeException if the ride offer does not exist or the user is not authorized to delete it.
     */
    @Transactional
    public void deleteRequest(UserEntity currentUser, Long id) {
        RideRequestsEntity rideRequest = rideRequestRepository.findById(id)
                //TODO Add global exception handling
                .orElseThrow(() -> new RuntimeException("Ride request does not exist"));

        if (!rideRequest.getRequester().equals(currentUser)) {
            throw new RuntimeException("Only owner can delete this ride offer");
        }

        RideOfferEntity rideOffer = rideOfferRepository
                .findById(rideRequest.getRideOffer().getId())
                        .orElseThrow(() -> new RuntimeException("Ride Offer Not Found"));

        if (!rideOffer.getStatus().equals(RideStatus.AVAILABLE)) {
            throw new RuntimeException("Can not delete ride that is no longer available");
        }
        rideOffer.setAvailableSeats(rideOffer.getAvailableSeats() + 1);

        rideRequestRepository.delete(rideRequest);
    }

    /**
     * Allows the requester to edit their ride request.
     *
     * @param request The {@link EditRideRequestRequest} containing the ride request ID and new status.
     * @param currentUser The {@link UserEntity} making the edit.
     * @return The updated {@link RideRequestResponse}.
     * @throws RuntimeException if the ride request cannot be edited.
     */
    @Transactional
    public RideRequestResponse editRideRequest(EditRideRequestRequest request, UserEntity currentUser) {

        RideRequestsEntity rideRequest = rideRequestRepository.findById(request.getRideRequestId())
                .orElseThrow(() -> new RuntimeException("Ride request not found"));

        if(!rideRequest.getRequester().equals(currentUser)){
            throw new RuntimeException("You are not authorized to edit this ride request");
        }

        RideOfferEntity rideOffer = rideRequest.getRideOffer();

        if (rideOffer.getStatus() != RideStatus.AVAILABLE){
            throw new RuntimeException("Cannot edit ride request as the ride offer is no longer available");
        }

        RequestStatus currentStatus = rideRequest.getRequestStatus();
        RequestStatus newStatus = request.getRequestStatus();

        if(currentStatus == RequestStatus.PENDING){
            if(newStatus == RequestStatus.CANCELED){
                rideRequest.setRequestStatus(RequestStatus.CANCELED);
                rideOffer.setAvailableSeats(rideOffer.getAvailableSeats()+1);

                if(rideOffer.getStatus() == RideStatus.UNAVAILABLE && rideOffer.getAvailableSeats() > 0){
                    rideOffer.setStatus(RideStatus.AVAILABLE);
                }
            } else {
                throw new RuntimeException("Invalid status change. From PENDING, you can only cancel the request");
            }
        } else if (currentStatus==RequestStatus.CANCELED){
            if(newStatus == RequestStatus.PENDING){

                if(rideOffer.getAvailableSeats() <= 0){
                    throw new RuntimeException("No available seats to uncancel this ride request");
                }

                rideRequest.setRequestStatus(RequestStatus.PENDING);
                rideOffer.setAvailableSeats(rideOffer.getAvailableSeats()-1);

                if (rideOffer.getAvailableSeats() == 0){
                    rideOffer.setStatus(RideStatus.UNAVAILABLE);
                }
            } else{
                throw new RuntimeException("Invalid status change. From CANCELED, you can only change to PENDING.");
            }
        } else{
            throw new RuntimeException("Only requests in PENDING or CANCELED status can be edited by the requester.");
        }

        rideOfferRepository.save(rideOffer);
        rideRequestRepository.save(rideRequest);

        return rideRequestConverter.entityToDTO(rideRequest);
    }
}
