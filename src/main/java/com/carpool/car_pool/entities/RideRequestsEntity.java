package com.carpool.car_pool.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RideRequestsEntity {
    
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private RideOfferEntity rideOffer;

    @ManyToMany(mappedBy = "rideRequests")
    @JsonIgnore
    private List<UserEntity> user;
}
