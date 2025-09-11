package com.anonymous63.lms.repository;

import com.anonymous63.lms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
