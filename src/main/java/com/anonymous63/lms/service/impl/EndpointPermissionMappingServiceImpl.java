package com.anonymous63.lms.service.impl;

import com.anonymous63.lms.dto.request.EndpointPermissionMappingReqDto;
import com.anonymous63.lms.dto.response.EndpointPermissionMappingResDto;
import com.anonymous63.lms.entity.EndpointPermissionMapping;
import com.anonymous63.lms.mapper.EndpointPermissionMappingMapper;
import com.anonymous63.lms.repository.EndpointPermissionMappingRepo;
import com.anonymous63.lms.service.EndpointPermissionMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EndpointPermissionMappingServiceImpl implements EndpointPermissionMappingService {

    private final EndpointPermissionMappingMapper mapper;
    private final EndpointPermissionMappingRepo endpointPermissionMappingRepo;

    @Override
    public EndpointPermissionMappingResDto createEndpointMapping(EndpointPermissionMappingReqDto dto) {
        EndpointPermissionMapping endpointPermissionMapping = EndpointPermissionMapping.builder()
                .endpoint(dto.getEndpoint())
                .httpMethod(dto.getHttpMethod())
                .requiredPrivilege(dto.getRequiredPrivilege())
                .build();
        endpointPermissionMapping = endpointPermissionMappingRepo.save(endpointPermissionMapping);
        return mapper.toDto(endpointPermissionMapping);
    }
}
