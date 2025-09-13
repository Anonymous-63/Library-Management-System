package com.anonymous63.lms.controller;

import com.anonymous63.lms.dto.request.LoginReqDto;
import com.anonymous63.lms.dto.request.RefreshReqDto;
import com.anonymous63.lms.dto.request.RegisterReqDto;
import com.anonymous63.lms.dto.response.JwtResponse;
import com.anonymous63.lms.entity.Role;
import com.anonymous63.lms.entity.User;
import com.anonymous63.lms.repository.RoleRepo;
import com.anonymous63.lms.repository.UserRepo;
import com.anonymous63.lms.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterReqDto request) {
        // check if email already exists
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        Role userRole = roleRepo.findByName("ROLE_MEMBER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(userRole));

        userRepo.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReqDto request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepo.findByEmail(request.getEmail()).orElseThrow();

        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshReqDto request) {
        var claims = jwtUtils.parseToken(request.getRefreshToken());
        String email = claims.getPayload().getSubject();

        User user = userRepo.findByEmail(email).orElseThrow();
        String newAccessToken = jwtUtils.generateAccessToken(user);

        return ResponseEntity.ok(new JwtResponse(newAccessToken, request.getRefreshToken()));
    }
}
