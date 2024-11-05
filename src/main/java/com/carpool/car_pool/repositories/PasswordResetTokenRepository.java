package com.carpool.car_pool.repositories;

import com.carpool.car_pool.repositories.entities.PasswordResetToken;
import com.carpool.car_pool.repositories.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    void deleteByUser(UserEntity user);
}
