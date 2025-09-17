package com.anonymous63.lms.mapper;

import com.anonymous63.lms.dto.request.PrivilegeReqDto;
import com.anonymous63.lms.dto.response.PrivilegeResDto;
import com.anonymous63.lms.entity.Privilege;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrivilegeMapper {

    PrivilegeResDto toDto(Privilege privilege);

    Privilege toEntity(PrivilegeReqDto dto);
}
