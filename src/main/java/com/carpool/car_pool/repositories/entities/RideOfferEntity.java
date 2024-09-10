package com.carpool.car_pool.repositories.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
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

    private String startLocation;

    private Integer availableSeats;

    private LocalDateTime departureTime;

    private RideStatus status;

    @ManyToMany(mappedBy = "rideOffers")
    @JsonIgnore
    private List<UserEntity> users;

    @OneToMany(mappedBy = "rideOffer")
    private List<RideRequestsEntity> rideRequests;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
}
