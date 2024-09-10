package com.carpool.car_pool.repositories.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserEntity implements Principal  {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @CreatedDate
    @Column(updatable = false)
    private LocalDate createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDate lastModifiedDate;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<RideOfferEntity> rideOffers;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<RideRequestsEntity> rideRequests;

    @Override
    public String getName() {
        return email;
    }
}
