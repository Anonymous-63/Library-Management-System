package com.anonymous63.lms.repository;

import com.anonymous63.lms.entity.AbacPolicy;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbacPolicyRepo extends JpaRepository<AbacPolicy, Long> {
    List<AbacPolicy> findByResourceTypeAndAction(String resourceType, String action);
}
