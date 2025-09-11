package com.anonymous63.lms.mapper;

import com.anonymous63.lms.dto.request.UserReqDto;
import com.anonymous63.lms.dto.response.UserResDto;
import com.anonymous63.lms.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResDto toUserResDto(User user);

    User toEntity(UserReqDto reqDto);
}
