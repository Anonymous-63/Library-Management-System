package com.anonymous63.lms.service;

import com.anonymous63.lms.dto.request.EndpointPermissionMappingReqDto;
import com.anonymous63.lms.dto.response.EndpointPermissionMappingResDto;

public interface EndpointPermissionMappingService {
    EndpointPermissionMappingResDto createEndpointMapping(EndpointPermissionMappingReqDto dto);
}
