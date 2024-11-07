package com.carpool.car_pool.controllers.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PasswordResetConfirmRequest {
    @NotBlank(message = "Reset token cannot be blank")
    private String token;
}
