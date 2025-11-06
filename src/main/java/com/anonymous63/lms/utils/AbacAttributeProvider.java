package com.anonymous63.lms.utils;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class AbacAttributeProvider {
    public Map<String, Object> subject(Authentication authentication) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("username", authentication.getName());
        // Add roles as a set
        Set<String> roles = new HashSet<>();
        authentication.getAuthorities().forEach(a -> roles.add(a.getAuthority()));
        attrs.put("roles", roles);

        // Example: attrs.put("department", ...); attrs.put("clearance", ...);
        return attrs;
    }

    public Map<String, Object> resource(Object resource) {
        // For demo, resource is a Map<String,Object>
        if (resource instanceof Map<?, ?> m) {
            return new HashMap<>((Map<String, Object>) m);
        }
        return new HashMap<>();
    }
}
