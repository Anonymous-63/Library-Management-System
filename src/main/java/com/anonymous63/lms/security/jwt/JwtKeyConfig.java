package com.anonymous63.lms.security.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.security.*;

@Configuration
public class JwtKeyConfig {
    @Bean
    public KeyPair keyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048); // Strong key
        return generator.generateKeyPair();
    }
}
