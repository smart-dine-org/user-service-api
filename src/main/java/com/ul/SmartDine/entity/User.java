package com.ul.SmartDine.entity;

import com.ul.SmartDine.entity.enums.AuthProvider;
import com.ul.SmartDine.entity.enums.UserStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private UUID id;
    private String keycloakId;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private String phoneNumber;
    private String profilePictureUrl;
    private UserStatus status = UserStatus.PENDING_VERIFICATION;
    private AuthProvider provider = AuthProvider.LOCAL;
    private String providerId;
    private boolean emailVerified = false;
    private boolean phoneVerified = false;
    private boolean twoFactorEnabled = false;
    private LocalDateTime lastLoginAt;

    private Set<Role> roles = new HashSet<>();
    private LocalTime createdAt;
    private LocalTime updatedAt;
    private LocalTime deletedAt;

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void safeDelete() {
    }
}
