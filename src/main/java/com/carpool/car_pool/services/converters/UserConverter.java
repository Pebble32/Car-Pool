package com.carpool.car_pool.services.converters;

import com.carpool.car_pool.controllers.dtos.RegisterRequest;
import com.carpool.car_pool.controllers.dtos.UserResponse;
import com.carpool.car_pool.repositories.entities.UserEntity;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

/**
 * Converter for mapping between {@link RegisterRequest} and {@link UserEntity}.
 */
@Component
public class UserConverter {

    /**
     * Converts a {@link RegisterRequest} DTO to a {@link UserEntity}.
     *
     * @param registerRequest The {@link RegisterRequest} to convert.
     * @return The corresponding {@link UserEntity}.
     */
    public UserEntity toEntity(@Valid RegisterRequest registerRequest) {
        return UserEntity.builder()
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .phoneNumber(registerRequest.getPhoneNumber())
                .profilePicture(registerRequest.getProfilePicture())
                .build();
    }

    /**
     * Converts a {@link UserEntity} to a {@link UserResponse} DTO.
     *
     * @param userEntity The {@link UserEntity} to convert.
     * @return The corresponding {@link UserResponse}.
     */
    public UserResponse entityToDTO(@Valid UserEntity userEntity) {
        return UserResponse.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstname())
                .lastName(userEntity.getLastname())
                .profilePicture(userEntity.getProfilePicture())
                .phoneNumber(userEntity.getPhoneNumber())
                .createdDate(userEntity.getCreatedDate())
                .lastModifiedDate(userEntity.getLastModifiedDate())
                .build();
    }
}
