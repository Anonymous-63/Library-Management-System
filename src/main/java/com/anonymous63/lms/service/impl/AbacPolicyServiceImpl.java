package com.anonymous63.lms.service.impl;

import com.anonymous63.lms.entity.AbacPolicy;
import com.anonymous63.lms.repository.AbacPolicyRepo;
import com.anonymous63.lms.service.AbacPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AbacPolicyServiceImpl implements AbacPolicyService {

    private final AbacPolicyRepo abacPolicyRepo;

    @Override
    public AbacPolicy createPolicy(AbacPolicy policy) {
        return abacPolicyRepo.save(policy);
    }

    @Override
    public List<AbacPolicy> getAllPolicies() {
        return abacPolicyRepo.findAll();
    }

    @Override
    public Optional<AbacPolicy> getPolicyById(Long policyId) {
        return abacPolicyRepo.findById(policyId);
    }

    @Override
    public void deletePolicy(Long policyId) {
        abacPolicyRepo.deleteById(policyId);
    }
}
