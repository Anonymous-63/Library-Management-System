package com.anonymous63.lms.mapper;

import com.anonymous63.lms.dto.request.UserReqDto;
import com.anonymous63.lms.dto.response.UserResDto;
import com.anonymous63.lms.entity.Role;
import com.anonymous63.lms.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserReqDto dto);

    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    @Mapping(target = "status", expression = "java(user.getStatus().name())")
    UserResDto toDto(User user);

    default Set<String> mapRoles(Set<Role> roles) {
        return roles.stream().map(Role::getName).collect(Collectors.toSet());
    }
}
