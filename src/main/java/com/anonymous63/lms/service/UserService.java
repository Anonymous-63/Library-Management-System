package com.anonymous63.lms.service;

import com.anonymous63.lms.dto.request.UserReqDto;
import com.anonymous63.lms.dto.response.UserResDto;

public interface UserService {
    UserResDto save(UserReqDto reqDto);
}
