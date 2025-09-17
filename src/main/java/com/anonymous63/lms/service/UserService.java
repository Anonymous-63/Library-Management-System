package com.anonymous63.lms.service;

import com.anonymous63.lms.dto.request.UserReqDto;
import com.anonymous63.lms.dto.response.UserResDto;

import java.util.List;

public interface UserService {
    UserResDto createUser(UserReqDto dto);
    UserResDto updateUser(Long id, UserReqDto dto);
    void deleteUser(Long id);
    UserResDto getUserById(Long id);
    List<UserResDto> getAllUsers();
    UserResDto enableUser(Long id, boolean enabled);
    UserResDto updateUserStatus(Long id, String status);
}
