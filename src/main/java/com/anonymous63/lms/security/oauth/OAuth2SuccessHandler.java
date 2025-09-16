package com.anonymous63.lms.security.oauth;

import com.anonymous63.lms.entity.User;
import com.anonymous63.lms.repository.UserRepo;
import com.anonymous63.lms.security.jwt.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Object principal = authentication.getPrincipal();

        User user;
        if (principal instanceof CustomOAuth2User customUser) {
            user = customUser.getUser();
        } else if (principal instanceof CustomOidcUser customOidcUser) {
            user = customOidcUser.getUser();
        } else {
            throw new RuntimeException("Unexpected principal type: " + principal.getClass());
        }

        // Issue tokens
        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // Decide response type
        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            // Return JSON for Postman / API clients
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Map<String, Object> body = new HashMap<>();
            body.put("access_token", accessToken);
            body.put("token_type", "Bearer");
            body.put("expires_in", 3600);
            body.put("refresh_token", "HttpOnly Cookie");

            new ObjectMapper().writeValue(response.getWriter(), body);
        } else {
            // Redirect for browser clients
            String frontendRedirectUri = "http://localhost:3000/";
            String redirectUrl = UriComponentsBuilder.fromUriString(frontendRedirectUri)
                    .queryParam("access_token", accessToken)
                    .build().toUriString();

            response.sendRedirect(redirectUrl);
        }
    }
}

