package com.carpool.car_pool.services;

import com.carpool.car_pool.controllers.dtos.AnswerRideRequestRequest;
import com.carpool.car_pool.controllers.dtos.RideRequestRequest;
import com.carpool.car_pool.controllers.dtos.RideRequestResponse;
import com.carpool.car_pool.repositories.RideOfferRepository;
import com.carpool.car_pool.repositories.RideRequestRepository;
import com.carpool.car_pool.repositories.common.PageResponse;
import com.carpool.car_pool.repositories.entities.RequestStatus;
import com.carpool.car_pool.repositories.entities.RideRequestsEntity;
import com.carpool.car_pool.repositories.entities.RideStatus;
import com.carpool.car_pool.repositories.entities.UserEntity;
import com.carpool.car_pool.repositories.specifications.RideRequestSpecifications;
import com.carpool.car_pool.services.converters.RideRequestConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

import static com.carpool.car_pool.repositories.entities.RequestStatus.CANCELED;
import static com.carpool.car_pool.repositories.entities.RequestStatus.PENDING;
import static com.carpool.car_pool.repositories.entities.RideStatus.UNAVAILABLE;

@Service
@RequiredArgsConstructor
public class RideRequestService {


    private final RideOfferRepository rideOfferRepository;
    private final RideRequestRepository rideRequestRepository;
    private final RideRequestConverter rideRequestConverter;

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

        boolean alreadyRequested = rideRequestRepository.existsByRideOfferIdAndRequesterId(request.getRideOfferId(), currentUser.getId());
        if (alreadyRequested) {
            throw new RuntimeException("You have already requested this Ride Offer");
        }

        var rideRequestEntity = RideRequestsEntity.builder()
                .rideOffer(rideOffer)
                .requester(currentUser)
                .requestStatus(PENDING)
                .build();

        return rideRequestRepository.save(rideRequestEntity).getId();


    }

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

    public List<RideRequestResponse> getRideRequestsForUser(UserEntity currentUser) {
        Specification<RideRequestsEntity> spec = Specification.where(RideRequestSpecifications.hasRequester(currentUser));

        return rideRequestRepository.findAll(spec)
                .stream()
                .map(rideRequestConverter::entityToDTO)
                .collect(Collectors.toList());
    }

    public PageResponse<RideRequestResponse> getRideRequestsForRideOfferPaginated(Long rideOfferId, UserEntity currentUser, int page, int size) {
        var rideOffer = rideOfferRepository.findById(rideOfferId)
                .orElseThrow(() -> new RuntimeException("Ride Offer Not Found"));

        if (!rideOffer.getCreator().equals(currentUser)) {
            throw new RuntimeException("Unauthorized Access");
        }

        Pageable pageable = (Pageable) PageRequest.of(page, size, Sort.by("createdDate").descending());
        Specification<RideRequestsEntity> spec = Specification.where(RideRequestSpecifications.hasRideOfferId(rideOfferId));

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

    public PageResponse<RideRequestResponse> getRideRequestsForUserPaginated(UserEntity currentUser, int page, int size) {
        Pageable pageable = (Pageable) PageRequest.of(page, size, Sort.by("createdDate").descending());
        Specification<RideRequestsEntity> spec = Specification.where(RideRequestSpecifications.hasRequester(currentUser));

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

}