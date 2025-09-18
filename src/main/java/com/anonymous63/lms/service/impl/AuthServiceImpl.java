package com.anonymous63.lms.service.impl;

import com.anonymous63.lms.common.exception.BusinessException;
import com.anonymous63.lms.common.exception.ResourceNotFoundException;
import com.anonymous63.lms.dto.request.LoginReqDto;
import com.anonymous63.lms.dto.request.RegisterReqDto;
import com.anonymous63.lms.dto.response.JwtResponse;
import com.anonymous63.lms.dto.response.UserResDto;
import com.anonymous63.lms.entity.Role;
import com.anonymous63.lms.entity.User;
import com.anonymous63.lms.enums.AccountStatus;
import com.anonymous63.lms.mapper.UserMapper;
import com.anonymous63.lms.repository.RoleRepo;
import com.anonymous63.lms.repository.UserRepo;
import com.anonymous63.lms.security.jwt.JwtUtils;
import com.anonymous63.lms.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final UserMapper mapper;

    @Override
    public UserResDto register(RegisterReqDto request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            try {
                throw new BadRequestException("Email already registered");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }


        Role defaultRole = roleRepo.findByName("ROLE_MEMBER").orElseThrow(() -> new ResourceNotFoundException("Default role not found"));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNo(request.getPhoneNo());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAvatarUrl(request.getAvatarUrl());
        user.setAddress(request.getAddress());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setRoles(Set.of(defaultRole));
        User saved = userRepo.save(user);
        return mapper.toDto(saved);
    }

    @Override
    public JwtResponse login(LoginReqDto request) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepo.findByEmail(request.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 🔹 Check if account is enabled
        if (!user.isEnabled()) {
            throw new BusinessException("Account is disabled");
        }

// 🔹 Check account status
        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException("Account not active: " + user.getStatus());
        }

        user.setLastLogin(Instant.now());
        userRepo.save(user);

        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        return new JwtResponse(accessToken, refreshToken);
    }

    @Override
    public JwtResponse refreshToken(String refreshToken) {
        var claims = jwtUtils.parseToken(refreshToken);
        String email = claims.getPayload().getSubject();

        User user = userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 🔹 Check if account is enabled
        if (!user.isEnabled()) {
            throw new BusinessException("Account is disabled");
        }

// 🔹 Check account status
        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException("Account not active: " + user.getStatus());
        }

        String newAccessToken = jwtUtils.generateAccessToken(user);

        return new JwtResponse(newAccessToken, refreshToken);
    }
}
