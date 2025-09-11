package com.anonymous63.lms.service.impl;

import com.anonymous63.lms.dto.request.UserReqDto;
import com.anonymous63.lms.dto.response.UserResDto;
import com.anonymous63.lms.entity.User;
import com.anonymous63.lms.mapper.UserMapper;
import com.anonymous63.lms.repository.UserRepo;
import com.anonymous63.lms.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper mapper;
    private final UserRepo userRepo;

    @Transactional
    @Override
    public UserResDto save(UserReqDto reqDto) {
        User user = mapper.toEntity(reqDto);
        User saveUser = userRepo.save(user);
        return mapper.toUserResDto(saveUser);
    }
}
