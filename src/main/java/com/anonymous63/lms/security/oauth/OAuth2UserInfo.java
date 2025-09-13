package com.anonymous63.lms.security.oauth;

import java.util.Map;

public interface OAuth2UserInfo {
    String getId();
    String getEmail();
    String getName();
    Map<String, Object> getAttributes();
}
