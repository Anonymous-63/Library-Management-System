package com.anonymous63.lms.security.oauth;

import com.anonymous63.lms.entity.Role;
import com.anonymous63.lms.entity.User;
import com.anonymous63.lms.repository.RoleRepo;
import com.anonymous63.lms.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    private final OidcUserService delegate = new OidcUserService();

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = delegate.loadUser(userRequest);

        Map<String, Object> attributes = oidcUser.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);

        String email = userInfo.getEmail();
        if (email == null || email.isBlank()) {
            throw new OAuth2AuthenticationException("Email not found from OIDC provider: " + registrationId);
        }

        User user = userRepo.findByEmail(email)
                .orElseGet(() -> createNewUser(userInfo, registrationId, email));

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toSet());

        // ✅ Wrap as CustomOidcUser (which Spring accepts as OidcUser)
        return new CustomOidcUser(user, authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }

    private User createNewUser(OAuth2UserInfo userInfo, String provider, String email) {
        Role defaultRole = roleRepo.findByName("ROLE_MEMBER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User newUser = User.builder()
                .name(userInfo.getName())
                .email(email)
                .provider(provider)
                .providerId(userInfo.getId())
                .enabled(true)
                .password(UUID.randomUUID().toString())
                .roles(Set.of(defaultRole))
                .build();
        return userRepo.save(newUser);
    }

}
