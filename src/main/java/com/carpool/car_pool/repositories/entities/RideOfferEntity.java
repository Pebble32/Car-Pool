package com.carpool.car_pool.repositories.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@EntityListeners(AuditingEntityListener.class)
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RideOfferEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String startLocation;

    @Column(nullable = false)
    private String endLocation;

    private Integer availableSeats;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    private RideStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private UserEntity creator;

    @OneToMany(mappedBy = "rideOffer")
    private List<RideRequestsEntity> rideRequests;

    //@CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    //@LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
}
