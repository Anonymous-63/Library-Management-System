package com.anonymous63.lms.security.oauth;

import com.anonymous63.lms.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.util.Collection;

public class CustomOidcUser extends DefaultOidcUser {
    private final User user;

    public CustomOidcUser(User user,
                          Collection<? extends GrantedAuthority> authorities,
                          OidcIdToken idToken,
                          OidcUserInfo userInfo) {
        super(authorities, idToken, userInfo);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
