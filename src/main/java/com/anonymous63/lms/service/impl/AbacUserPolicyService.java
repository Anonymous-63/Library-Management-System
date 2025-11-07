package com.anonymous63.lms.service.impl;

import com.anonymous63.lms.entity.AbacPolicy;
import com.anonymous63.lms.repository.AbacPolicyRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AbacUserPolicyService {
    private final AbacPolicyRepo abacPolicyRepo;
    private final AbacEvaluationService abacEvaluationService;

    public Map<String, Map<String, Boolean>> getUserPolicies(Authentication auth) {
        Map<String, Map<String, Boolean>> result = new LinkedHashMap<>();
        List<AbacPolicy> policies = abacPolicyRepo.findAll();

        for (AbacPolicy policy : policies) {
            boolean allowed = abacEvaluationService.isAllowed(policy, auth, null); // global eval (no resource)
            result
                    .computeIfAbsent(policy.getResourceType(), k -> new LinkedHashMap<>())
                    .put(policy.getAction(), allowed);
        }

        return result;
    }
}
