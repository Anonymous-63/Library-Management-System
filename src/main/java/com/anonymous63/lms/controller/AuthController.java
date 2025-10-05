package com.anonymous63.lms.controller;

import com.anonymous63.lms.dto.request.LoginReqDto;
import com.anonymous63.lms.dto.request.RefreshReqDto;
import com.anonymous63.lms.dto.request.RegisterReqDto;
import com.anonymous63.lms.dto.response.JwtResponse;
import com.anonymous63.lms.dto.response.UserResDto;
import com.anonymous63.lms.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResDto> register(@Valid @RequestBody RegisterReqDto request) {
        UserResDto response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginReqDto request) {
        // ✅ Login now checks credentials, account status, and enabled flag
        JwtResponse tokens = authService.login(request);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody RefreshReqDto request) {
        JwtResponse tokens = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(tokens);
    }
}
