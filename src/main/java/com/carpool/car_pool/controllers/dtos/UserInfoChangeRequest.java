package com.carpool.car_pool.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserInfoChangeRequest {

    @NotBlank(message = "First name can't be empty")
    private String firstName;

    @NotBlank(message = "Last name can't be empty")
    private String lastName;

    @NotBlank(message = "phone number can't be empty")
    private String phoneNumber;

}
