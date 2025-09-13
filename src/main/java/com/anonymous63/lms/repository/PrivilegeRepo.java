package com.anonymous63.lms.repository;

import com.anonymous63.lms.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrivilegeRepo extends JpaRepository<Privilege, Long> {
    Optional<Privilege> findByName(String name);
}
