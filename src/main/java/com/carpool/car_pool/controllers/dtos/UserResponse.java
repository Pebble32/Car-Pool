package com.carpool.car_pool.controllers.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String profilePicture;
}


