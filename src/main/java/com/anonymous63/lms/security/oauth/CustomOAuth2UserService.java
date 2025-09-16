package com.anonymous63.lms.security.oauth;

import com.anonymous63.lms.entity.Role;
import com.anonymous63.lms.entity.User;
import com.anonymous63.lms.repository.RoleRepo;
import com.anonymous63.lms.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oauthUser.getAttributes());

        String email = userInfo.getEmail();
        if ((email == null || email.isBlank()) && "github".equals(registrationId)) {
            email = fetchGithubPrimaryEmail(userRequest.getAccessToken().getTokenValue());
        }
        if (email == null || email.isBlank()) {
            throw new OAuth2AuthenticationException("Email not found from provider: " + registrationId);
        }

        String finalEmail = email;
        User user = userRepo.findByEmail(email).orElseGet(() -> createNewUser(userInfo, registrationId, finalEmail));

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toSet());

        return new CustomOAuth2User(
                user,
                authorities,
                userInfo.getAttributes(),
                userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName()
        );
    }

    private User createNewUser(OAuth2UserInfo userInfo, String provider, String email) {
        Role defaultRole = roleRepo.findByName("ROLE_MEMBER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User newUser = User.builder()
                .name(userInfo.getName())
                .email(email)
                .provider(provider)
                .providerId(userInfo.getId())
                .active(true)
                .password(UUID.randomUUID().toString())
                .roles(Set.of(defaultRole))
                .build();
        return userRepo.save(newUser);
    }

    private String fetchGithubPrimaryEmail(String accessToken) {
        String url = "https://api.github.com/user/emails";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Map<String, Object>>> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
                });
        assert response.getBody() != null;
        return response.getBody().stream()
                .filter(e -> Boolean.TRUE.equals(e.get("primary")) && Boolean.TRUE.equals(e.get("verified")))
                .map(e -> (String) e.get("email"))
                .findFirst()
                .orElse(null);
    }
}

