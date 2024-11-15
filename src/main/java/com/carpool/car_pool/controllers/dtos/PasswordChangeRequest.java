package com.carpool.car_pool.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordChangeRequest {

    private String oldPassword;

    @Size(min = 8, message = "Password should be at least 8 characters long")
    @NotBlank(message = "Password can't be empty")
    private String newPassword;
}
