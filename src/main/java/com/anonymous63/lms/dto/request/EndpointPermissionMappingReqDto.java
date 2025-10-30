package com.anonymous63.lms.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EndpointPermissionMappingReqDto {

    @Size(max = 200, message = "Endpoint must not exceed 200 characters")
    private String endpoint;

    @Size(max = 50, message = "Http method name must not exceed 50 characters")
    private String httpMethod;
    @Size(max = 50, message = "Required privilege name must not exceed 50 characters")
    private String requiredPrivilege;
}
