package com.ul.SmartDine.repository;

import com.ul.SmartDine.entity.User;
import com.ul.SmartDine.entity.enums.AuthProvider;
import com.ul.SmartDine.entity.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Optional<User> findByKeycloakId(String keycloakId);

    Optional<User> findByProviderAndProviderId(AuthProvider provider, String providerId);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Page<User> findAllByStatus(UserStatus status, Pageable pageable);

    @Query("SELECT u FROM user u WHERE u.email=:email AND u.deletedAt IS NULL")
    Optional<User> findActiveByEmail(@Param("email") String email);

    @Modifying
    @Query("UPDATE user u SET u.lastLoginAt=:loginAt WHERE u.id=:userId")
    Optional<User> findActiveByEmail(@Param("userId") UUID userId, @Param("loginAt") LocalDateTime loginAt);

    @Query("SELECT COUNT(u) FROM user u WHERE u.createdAt>=:since")
    long countNewUserSince(@Param("since") LocalDateTime since);
}
