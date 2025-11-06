package com.anonymous63.lms.utils;

import com.anonymous63.lms.entity.AbacPolicy;
import com.anonymous63.lms.repository.AbacPolicyRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

@Component
@RequiredArgsConstructor
public class AbacPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private AbacPolicyEvaluator policyEvaluator;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (targetDomainObject instanceof java.util.Map<?, ?> m) {
            String resourceType = (String) m.get("resourceType");
            return policyEvaluator.evaluatePolicy(authentication, resourceType, permission.toString(), targetDomainObject);
        }
        return false;
    }

/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**

/* <<<<<<<<<<  afdf3e2d-ae42-4f49-96f2-0bf3cf373a73  >>>>>>>>>>> */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }

    public boolean hasPermission(Authentication authentication, Object targetDomainObject, String resourceType, Object permission) {
        return policyEvaluator.evaluatePolicy(authentication, resourceType, permission.toString(), targetDomainObject);
    }
}
