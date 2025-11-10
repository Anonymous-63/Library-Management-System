package com.anonymous63.lms.security.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

    @Value("${app.jwt.secret}")
    private String secret;

    @Bean
    public SecretKey jwtSecretKey() {
        System.out.println("✅ Loaded jwt.secret: " + secret); // 🔍 should print at startup

        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        System.out.println("🔑 JWT Secret length: " + keyBytes.length);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}