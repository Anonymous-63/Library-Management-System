package com.anonymous63.lms.service;

import com.anonymous63.lms.entity.AbacPolicy;

import java.util.List;
import java.util.Optional;

public interface AbacPolicyService {

    AbacPolicy createPolicy(AbacPolicy policy);

    List<AbacPolicy> getAllPolicies();

    Optional<AbacPolicy> getPolicyById(Long policyId);

    void deletePolicy(Long policyId);
}
