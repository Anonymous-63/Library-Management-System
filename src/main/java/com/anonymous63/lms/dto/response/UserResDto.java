package com.anonymous63.lms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNo;
    private String avatarUrl;
    private String address;
    private LocalDate dateOfBirth;
    private Set<String> roles;    // role names only
    private String status;        // AccountStatus
    private boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastLogin;
}
