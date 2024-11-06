package com.carpool.car_pool.repositories;

import com.carpool.car_pool.repositories.entities.PasswordResetTokenEntity;
import com.carpool.car_pool.repositories.entities.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {
    Optional<PasswordResetTokenEntity> findByToken(String token);
    Optional<PasswordResetTokenEntity> findByUser(UserEntity user);
}
