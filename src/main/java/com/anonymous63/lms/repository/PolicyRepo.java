package com.anonymous63.lms.repository;

import com.anonymous63.lms.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyRepo extends JpaRepository<Policy, Long> {
    List<Policy> findByResourceAndEnabledTrue(String subject);
}
