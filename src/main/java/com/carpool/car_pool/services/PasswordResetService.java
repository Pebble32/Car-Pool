package com.carpool.car_pool.services;

import com.carpool.car_pool.repositories.PasswordResetTokenRepository;
import com.carpool.car_pool.repositories.UserRepository;
import com.carpool.car_pool.repositories.entities.PasswordResetTokenEntity;
import com.carpool.car_pool.repositories.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for handling password reset functionality.
 *
 * This service provides methods to create password reset tokens,
 * send password reset emails, and reset user passwords based on
 * provided tokens.
 */
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    private final static long EXPIRATION_TIME_IN_MINUTES = 60;

    /**
     * Creates a password reset token for a user identified by the given email. If the user already has an existing
     * token, it will be replaced with a new one. An email with a password reset link will be sent to the user's email address.
     *
     * @param email The email address of the user requesting the password reset.
     */
    @Transactional
    public void createPasswordResetToken(String email) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with this email does not exist"));

                String token = UUID.randomUUID().toString();

        Optional<PasswordResetTokenEntity> existingToken = passwordResetTokenRepository.findByUser(user);
        PasswordResetTokenEntity tokenEntity;

        if (existingToken.isPresent()){
            tokenEntity = existingToken.get();
            tokenEntity.setToken(token);
            tokenEntity.setExpiryDate(LocalDateTime.now().plusMinutes(EXPIRATION_TIME_IN_MINUTES));
        } else {
            tokenEntity = PasswordResetTokenEntity.builder()
                    .token(token)
                    .user(user)
                    .expiryDate(LocalDateTime.now().plusMinutes(EXPIRATION_TIME_IN_MINUTES))
                    .build();
        }

        passwordResetTokenRepository.save(tokenEntity);

        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        String subject = "Password Reset Request";
        String message = "Dear " + user.getFirstname() + ",\n\n" +
                "We received a request to reset your password. Please use the following link to reset your password. " +
                "This link is valid for 60 minutes.\n\n" +
                resetLink + "\n\n" +
                "If you did not request a password reset, please ignore this email.\n\n" +
                "Best regards,\n" +
                "Car Pool Team Iceland";

        emailService.sendSimpleEmail(user.getEmail(),subject,message);
    }


    /**
     * Resets the user's password based on the provided password reset token.
     * This method will generate a new password for the user, update the user's record,
     * send an email with the new password, and remove the used token.
     *
     * @param token The password reset token used to identify the user and validate the request.
     * @throws RuntimeException If the token is invalid or has expired.
     */
    @Transactional
    public void resetPassword(String token){

        PasswordResetTokenEntity tokenEntity = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid password reset token"));

        if (tokenEntity.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Password reset token has expired");
        }

        UserEntity user = tokenEntity.getUser();

        String newPassword = generateRandomPassword(8);

        user.setPassword(newPassword);
        userRepository.save(user);

        String subject = "Your new password";
        String message = "Dear " + user.getFirstname() + ",\n\n" +
                "Your password has been successfully reset. Here is your new password:\n\n" +
                newPassword + "\n\n" +
                "Please log in and change your password as soon as possible.\n\n" +
                "Best regards,\n" +
                "Car Pool Team";

        emailService.sendSimpleEmail(user.getEmail(),subject,message);

        passwordResetTokenRepository.delete(tokenEntity);
    }


    /**
     * Generates a random password consisting of uppercase letters, lowercase letters, and digits.
     *
     * @param length The length of the generated password.
     * @return A randomly generated password of the specified length.
     */
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }
}
