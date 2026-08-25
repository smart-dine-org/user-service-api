package com.ul.SmartDine.entity;

import com.ul.SmartDine.entity.enums.OtpType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "token", nullable = false, unique = true, columnDefinition = "TEXT")
    private String token;
    @Column(name = "revoked", nullable = false)
    @Builder.Default
    private boolean revoked = false;
    @Column(name = "expiresAt", nullable = false)
    private LocalDateTime expiresAt;
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalTime createdAt;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isValid(String code) {
        return !revoked && !isExpired();
    }
}
