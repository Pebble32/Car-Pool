package com.carpool.car_pool.repositories;

import com.carpool.car_pool.repositories.entities.PasswordResetTokenEntity;
import com.carpool.car_pool.repositories.entities.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link PasswordResetTokenEntity} persistence.
 * Provides methods for finding a {@link PasswordResetTokenEntity} by its token or associated user.
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {
    /**
     * Retrieves an {@link Optional} containing the {@link PasswordResetTokenEntity} associated with the given token.
     * If no token is found, an empty {@link Optional} is returned.
     *
     * @param token The token string used to identify the {@link PasswordResetTokenEntity}.
     * @return An {@link Optional} containing the found {@link PasswordResetTokenEntity}, or empty if no token is found.
     */
    Optional<PasswordResetTokenEntity> findByToken(String token);
    /**
     * Finds a {@link PasswordResetTokenEntity} associated with the given user.
     *
     * @param user The {@link UserEntity} for which the password reset token is being searched.
     * @return An {@link Optional} containing the {@link PasswordResetTokenEntity} if found, or empty otherwise.
     */
    Optional<PasswordResetTokenEntity> findByUser(UserEntity user);
}
