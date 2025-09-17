package com.anonymous63.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleReqDto {
    @NotBlank(message = "Role name is required")
    private String name;

    private Set<Long> privilegeIds; // IDs of privileges assigned to this role
}
