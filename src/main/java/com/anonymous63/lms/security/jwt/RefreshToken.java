package com.anonymous63.lms.security.jwt;

import com.anonymous63.lms.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "refreshToken", indexes = {
        @Index(name = "idx_refresh_token_user", columnList = "user_id")
})
public class RefreshToken {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true, length = 512)
    private String tokenHash;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private Instant createdAt = Instant.now();
    private Instant expiresAt;
    private boolean revoked = false;
    private String deviceInfo;
}
