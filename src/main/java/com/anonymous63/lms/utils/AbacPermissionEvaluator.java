package com.anonymous63.lms.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AbacPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private AbacPolicyEvaluator policyEvaluator;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (!(targetDomainObject instanceof Map<?, ?> m)) return false;
        Object rt = m.get("resourceType");
        String resourceType = rt == null ? null : rt.toString();
        return resourceType != null && policyEvaluator.evaluatePolicy(authentication, resourceType, permission.toString(), targetDomainObject);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        // Optionally implement id-based resolving; return false by default to be explicit
        return false;
    }
}
