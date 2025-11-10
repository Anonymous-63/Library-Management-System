package com.anonymous63.lms.security.jwt;

import com.anonymous63.lms.entity.Privilege;
import com.anonymous63.lms.entity.Role;
import com.anonymous63.lms.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private static final Duration ACCESS_TOKEN_VALIDITY = Duration.ofMinutes(15);
    private static final Duration REFRESH_TOKEN_VALIDITY = Duration.ofDays(7);
    private final SecretKey jwtSecretKey;

    // 🔹 Generate Access Token
    public String generateAccessToken(User user) {
        return buildToken(user, ACCESS_TOKEN_VALIDITY);
    }

    // 🔹 Generate Refresh Token
    public String generateRefreshToken(User user) {
        return buildToken(user, REFRESH_TOKEN_VALIDITY);
    }

    private String buildToken(User user, Duration validity) {
        Instant now = Instant.now();

        Set<String> roles = user.getRoles().stream()
                .map(Role::getName).collect(Collectors.toSet());
        Set<String> privileges = user.getRoles().stream()
                .flatMap(r -> r.getPrivileges().stream())
                .map(Privilege::getName)
                .collect(Collectors.toSet());

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("roles", roles)
                .claim("privileges", privileges)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(validity)))
                .signWith(jwtSecretKey, Jwts.SIG.HS256) // ✅ HMAC instead of RSA
                .compact();
    }

    // 🔹 Validate + Parse Claims
    public Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .verifyWith(jwtSecretKey) // ✅ same secret for verification
                .build()
                .parseSignedClaims(token);
    }

    // 🔹 Extract Username
    public String extractUsername(String token) {
        return parseToken(token).getPayload().getSubject();
    }
}
