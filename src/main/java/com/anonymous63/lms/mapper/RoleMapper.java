package com.anonymous63.lms.mapper;

import com.anonymous63.lms.dto.request.RoleReqDto;
import com.anonymous63.lms.dto.response.RoleResDto;
import com.anonymous63.lms.entity.Privilege;
import com.anonymous63.lms.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "privileges", expression = "java(mapPrivileges(role.getPrivileges()))")
    RoleResDto toDto(Role role);

    @Mapping(target = "privileges", ignore = true)
        // privileges assigned manually in service
    Role toEntity(RoleReqDto dto);

    default Set<String> mapPrivileges(Set<Privilege> privileges) {
        return privileges.stream().map(Privilege::getName).collect(Collectors.toSet());
    }
}
