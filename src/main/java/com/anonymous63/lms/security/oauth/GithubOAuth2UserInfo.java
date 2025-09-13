package com.anonymous63.lms.security.oauth;

import java.util.Map;

public class GithubOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attrs;

    public GithubOAuth2UserInfo(Map<String, Object> attrs) {
        this.attrs = attrs;
    }

    @Override
    public String getId() {
        return String.valueOf(attrs.get("id"));
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
