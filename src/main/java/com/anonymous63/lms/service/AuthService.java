package com.anonymous63.lms.service;

import com.anonymous63.lms.dto.request.LoginReqDto;
import com.anonymous63.lms.dto.request.RegisterReqDto;
import com.anonymous63.lms.dto.response.JwtResponse;
import com.anonymous63.lms.dto.response.UserResDto;

public interface AuthService {

    UserResDto register(RegisterReqDto request);

    JwtResponse login(LoginReqDto request);

    JwtResponse refreshToken(String refreshToken);
}

