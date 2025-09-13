package com.anonymous63.lms.security.oauth;

import com.anonymous63.lms.entity.Role;
import com.anonymous63.lms.entity.User;
import com.anonymous63.lms.repository.RoleRepo;
import com.anonymous63.lms.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>  {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google/github
        Map<String, Object> attributes = oauthUser.getAttributes();

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);
        String email = userInfo.getEmail();

        if (email == null || email.isBlank()) {
            // For providers that don't return email (or it's private), we must get it via separate endpoint
            // or ask the client to request email. For now throw:
            throw new OAuth2AuthenticationException(new OAuth2Error("email_not_found"), "Email not found from provider");
        }

        // Find existing user by provider id OR email
        Optional<User> userOpt = userRepo.findByEmail(email);

        User user;
        if (userOpt.isPresent()) {
            user = userOpt.get();
            // If existing user registered locally (provider==null), you may want to link or prevent duplicates.
            if (user.getProvider() == null || user.getProvider().isEmpty()) {
                // Optionally: link provider to existing account
                user.setProvider(registrationId);
                user.setProviderId(userInfo.getId());
                userRepo.save(user);
            } else if (!registrationId.equals(user.getProvider())) {
                // Different provider with same email — decide policy: reject, link, or allow
                // We'll link for now:
                user.setProvider(registrationId);
                user.setProviderId(userInfo.getId());
                userRepo.save(user);
            }
        } else {
            // create new user
            Role defaultRole = roleRepo.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Default role not found"));
            user = new User();
            user.setFirstName(userInfo.getName() != null ? userInfo.getName() : "");
            user.setLastName("");
            user.setEmail(email);
            user.setPassword(""); // no local password
            user.setProvider(registrationId);
            user.setProviderId(userInfo.getId());
            user.setEnabled(true);
            user.setRoles(Set.of(defaultRole));
            userRepo.save(user);
        }

        // Return a Spring OAuth2User for the rest of the pipeline (not used for token creation)
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
        return new DefaultOAuth2User(authorities, attributes, "sub"); // key may differ per provider
    }
}
