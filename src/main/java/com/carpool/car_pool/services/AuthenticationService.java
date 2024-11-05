package com.carpool.car_pool.services;

import com.carpool.car_pool.controllers.dtos.AuthenticationRequest;
import com.carpool.car_pool.controllers.dtos.RegisterRequest;
import com.carpool.car_pool.repositories.PasswordResetTokenRepository;
import com.carpool.car_pool.repositories.UserRepository;
import com.carpool.car_pool.repositories.entities.PasswordResetToken;
import com.carpool.car_pool.repositories.entities.UserEntity;
import com.carpool.car_pool.services.converters.UserConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service for handling user authentication and registration.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    @Value("${password.reset.toekn.expiration.minutes:60}")
    private int tokenExpirationMinutes;

    /**
     * Authenticates a user based on the provided authentication request.
     *
     * @param authenticationRequest The request containing the user's email and password.
     * @throws RuntimeException if the user is not found or the password is incorrect.
     */
    public void authenticate(@Valid AuthenticationRequest authenticationRequest) {
        UserEntity userEntity = userRepository
                .findByEmail(authenticationRequest.getEmail()) //TODO: Better exception handling| UserNotFoundException
                .orElseThrow(() -> new RuntimeException("User with this email address does not exist"));

        if (!userEntity.getPassword().equals(authenticationRequest.getPassword())) {
            throw new RuntimeException("Wrong password");
            // TODO: Better exception handling | InvalidCredentialsException
        }
    }

    /**
     * Registers a new user based on the provided registration request.
     *
     * @param registerRequest The request containing the user's registration details.
     * @throws RuntimeException if a user with the provided email already exists.
     */
    public void register(@Valid RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
            // TODO: Better exception handling | UserAlreadyExistsException
        }
        UserEntity user = userConverter.toEntity(registerRequest);

        userRepository.save(user);
    }

    public void createPasswordResetToken(String email){
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with this email does not exist"));

        passwordResetTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expirDate(LocalDateTime.now().plusMinutes(tokenExpirationMinutes))
                .build();
        passwordResetTokenRepository.save(resetToken);

        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        String subject = "Password reset request";
        String message = "CLick the link to reset your password: " + resetLink;

        emailService.sendEmail(user.getEmail(), subject, message);
    }


    public void resetPassword(String token, String newPassword){
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invaild or expired password reset token"));

        if (resetToken.getExpirDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Password reset token has be expired");
        }

        UserEntity user = resetToken.getUser();
        user.setPassword(newPassword);
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }

}
