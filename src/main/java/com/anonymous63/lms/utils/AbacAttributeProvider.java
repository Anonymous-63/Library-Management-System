package com.anonymous63.lms.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AbacAttributeProvider {
    public Map<String, Object> subject(Authentication authentication) {
        Map<String, Object> attrs = new HashMap<>();
        if (authentication == null) return attrs;

        attrs.put("username", Optional.ofNullable(authentication.getName()).orElse(""));

        // Roles -> set of strings
        Set<String> roles = new HashSet<>();
        try {
            for (GrantedAuthority a : authentication.getAuthorities()) {
                if (a != null && a.getAuthority() != null) roles.add(a.getAuthority());
            }
        } catch (Exception ignored) {}

        attrs.put("roles", roles);
        // Add convenience scalar: primaryRole (first) if needed
        attrs.put("primaryRole", roles.stream().findFirst().orElse(null));

        // Add additional claims if your Authentication has JWT claims
        // If using JwtAuthenticationToken:
        // if (authentication instanceof JwtAuthenticationToken jwtAuth) {
        //     Map<String,Object> claims = jwtAuth.getToken().getClaims();
        //     attrs.putAll(claims); // be careful with collisions
        // }

        return attrs;
    }

    public Map<String, Object> resource(Object resource) {
        if (resource instanceof Map<?, ?> m) {
            // shallow copy
            Map<String, Object> copy = new HashMap<>();
            ((Map<?, ?>) resource).forEach((k, v) -> {
                if (k instanceof String) copy.put((String) k, v);
            });
            return copy;
        }
        return new HashMap<>();
    }
}
