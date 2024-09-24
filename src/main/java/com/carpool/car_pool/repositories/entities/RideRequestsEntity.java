package com.carpool.car_pool.repositories.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
public class RideRequestsEntity {
    
    @Id
    @GeneratedValue
    private Long id;

    private RequestStatus requestStatus;

    @ManyToOne
    private RideOfferEntity rideOffer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private UserEntity requester;

    //@CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    //@LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
}
