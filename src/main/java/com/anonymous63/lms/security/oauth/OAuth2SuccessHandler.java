package com.anonymous63.lms.security.oauth;

import com.anonymous63.lms.entity.Role;
import com.anonymous63.lms.entity.User;
import com.anonymous63.lms.repository.RoleRepo;
import com.anonymous63.lms.repository.UserRepo;
import com.anonymous63.lms.security.jwt.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
//    private final RefreshTokenService refreshTokenService; // optional DB-backed; can be null if stateless

    private final String frontendRedirectUri = "https://localhost:8080/"; // change

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = oauthToken.getAuthorizedClientRegistrationId();
        Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();

        // get email (Google: "email", GitHub: "login")
        String email = (String) attributes.getOrDefault("email", attributes.get("login"));

        if (email == null) {
            throw new RuntimeException("OAuth2 provider did not return an email or login identifier");
        }

        // 🔹 auto-create user if not present
        User user = userRepo.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setProvider(registrationId);
            newUser.setProviderId((String) attributes.get("sub")); // google "sub", github "id"
            newUser.setEnabled(true);

            Role roleUser = roleRepo.findByName("ROLE_MEMBER")
                    .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
            newUser.getRoles().add(roleUser);

            return userRepo.save(newUser);
        });

        // Issue tokens
        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        // set refresh token in secure cookie
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // redirect frontend with access token
        String redirectUrl = UriComponentsBuilder.fromUriString(frontendRedirectUri)
//                .queryParam("access_token", accessToken)
                .build().toUriString();

        response.sendRedirect(redirectUrl);
    }
}
