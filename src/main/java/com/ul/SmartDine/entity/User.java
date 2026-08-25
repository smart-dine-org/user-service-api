package com.ul.SmartDine.entity;

import com.ul.SmartDine.entity.enums.AuthProvider;
import com.ul.SmartDine.entity.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "keycloak_id", unique = true)
    private String keycloakId;
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;
    @Column(name = "password_hash")
    private String passwordHash;
    @Column(name = "phone", length = 20)
    private String phoneNumber;
    @Column(name = "profile_picture", columnDefinition = "TEXT")
    private String profilePictureUrl;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.PENDING_VERIFICATION;
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    @Builder.Default
    private AuthProvider provider = AuthProvider.LOCAL;
    @Column(name = "provider_id", nullable = false)
    private String providerId;
    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private boolean emailVerified = false;
    @Column(name = "phone_verified", nullable = false)
    @Builder.Default
    private boolean phoneVerified = false;
    @Column(name = "two_factor_enabled", nullable = false)
    @Builder.Default
    private boolean twoFactorEnabled = false;
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalTime createdAt;
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalTime updatedAt;
    @Column(name = "deleted_at")
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
