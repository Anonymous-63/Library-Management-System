package com.anonymous63.lms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EndpointPermissionMappingResDto {
    private Long id;
    private String endpoint;
    private String httpMethod;
    private String requiredPrivilege;}
