package com.anonymous63.lms.mapper;

import com.anonymous63.lms.dto.request.EndpointPermissionMappingReqDto;
import com.anonymous63.lms.dto.response.EndpointPermissionMappingResDto;
import com.anonymous63.lms.entity.EndpointPermissionMapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EndpointPermissionMappingMapper {

    EndpointPermissionMappingResDto toDto(EndpointPermissionMapping endpointPermissionMapping);

    EndpointPermissionMapping toEntity(EndpointPermissionMappingReqDto dto);
}
