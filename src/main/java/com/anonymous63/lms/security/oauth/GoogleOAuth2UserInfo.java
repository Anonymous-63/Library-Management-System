package com.anonymous63.lms.security.oauth;

import java.util.Map;

public class GoogleOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attrs;

    public GoogleOAuth2UserInfo(Map<String, Object> attrs) {
        this.attrs = attrs;
    }

    @Override
    public String getId() {
        return (String) attrs.get("sub");
    }

    @Override
    public String getEmail() {
        return (String) attrs.get("email");
    }

    @Override
    public String getName() {
        return (String) attrs.get("name");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attrs;
    }
}
