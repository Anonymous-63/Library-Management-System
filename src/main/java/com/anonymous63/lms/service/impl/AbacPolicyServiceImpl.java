package com.anonymous63.lms.service.impl;

import com.anonymous63.lms.entity.AbacPolicy;
import com.anonymous63.lms.repository.AbacPolicyRepo;
import com.anonymous63.lms.service.AbacPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AbacPolicyServiceImpl implements AbacPolicyService {

    private final AbacPolicyRepo abacPolicyRepo;

    @Override
    public AbacPolicy createPolicy(AbacPolicy policy) {
        return abacPolicyRepo.save(policy);
    }
}
