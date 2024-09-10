package com.carpool.car_pool.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
public class RideOfferEntity {

    @Id
    @GeneratedValue
    private Long id;


    @ManyToMany(mappedBy = "rideOffers")
    @JsonIgnore
    private List<UserEntity> users;

    @OneToMany(mappedBy = "rideOffer")
    private List<RideRequestsEntity> rideRequests;
}
