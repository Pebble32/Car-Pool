package com.carpool.car_pool.services;

import com.carpool.car_pool.controllers.dtos.EditRideOfferRequest;
import com.carpool.car_pool.controllers.dtos.RideOfferRequest;
import com.carpool.car_pool.controllers.dtos.RideOfferResponse;
import com.carpool.car_pool.repositories.RideOfferRepository;
import com.carpool.car_pool.repositories.UserRepository;
import com.carpool.car_pool.repositories.entities.RideOfferEntity;
import com.carpool.car_pool.repositories.entities.RideStatus;
import com.carpool.car_pool.services.converters.RideOfferConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RideOfferService {

    private final RideOfferRepository rideOfferRepository;
    private final RideOfferConverter rideOfferConverter;
    private final UserRepository userRepository;

    public List<RideOfferResponse> findAllRideOffers() {
        List<RideOfferEntity> rideOfferEntities = rideOfferRepository.findAll();
        return rideOfferEntities
                .stream()
                .map(rideOfferConverter::entityToDTO)
                .toList();
    }

    // TODO: If security is implemented change to ApplicationAuditAware
    //  with UserPrincipal using Jwt tokens to keep track of who is logged in and sending requests
    @Transactional
    public Long createRideOffer(
            @Valid RideOfferRequest request,
            String userEmail) {
        var user = userRepository.findByEmail(userEmail)
                //  TODO: Better exception handling UserNotFoundException
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        RideOfferEntity rideOffer = rideOfferConverter.dtoToEntity(request);
        rideOffer.setCreator(user);

        // TODO: Delete this line after AuditAware is implemented
        rideOffer.setCreatedDate(LocalDateTime.now());

        return rideOfferRepository.save(rideOffer).getId();

    }

    public RideOfferResponse viewRideOfferDetail(Long id) {
        return rideOfferConverter.entityToDTO(rideOfferRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Ride offer with this id does not exist")));
    }

    public RideOfferResponse editRideOfferDetail(EditRideOfferRequest editRideofferRequest, String email) {
        RideOfferEntity ride = rideOfferRepository.findById( editRideofferRequest.getRideId())
                .orElseThrow(()-> new RuntimeException("Ride offer does not exist"));


        if (!ride.getCreator().getEmail().equals(email)){
            throw new RuntimeException("Email is not the same");
        }
        if (!editRideofferRequest.getStartLocation().equals(ride.getStartLocation())){
            ride.setStartLocation(editRideofferRequest.getStartLocation());
        }
        if (!editRideofferRequest.getEndLocation().equals(ride.getEndLocation())){
            ride.setEndLocation(editRideofferRequest.getEndLocation());
        }

        if (!editRideofferRequest.getDepartureTime().equals(ride.getDepartureTime())){
            ride.setDepartureTime(editRideofferRequest.getDepartureTime());
        }

        if (!editRideofferRequest.getAvailableSeats().equals(ride.getAvailableSeats())){
            ride.setAvailableSeats(editRideofferRequest.getAvailableSeats());
        }

        if (!editRideofferRequest.getRideStatus().equals(ride.getStatus().toString())){

            ride.setStatus(RideStatus.valueOf(editRideofferRequest.getRideStatus()));
        }

        RideOfferEntity updatedRide = rideOfferRepository.save(ride);

        return rideOfferConverter.entityToDTO(updatedRide);


    }
}
