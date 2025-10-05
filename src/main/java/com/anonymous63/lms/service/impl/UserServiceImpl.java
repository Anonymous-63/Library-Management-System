package com.anonymous63.lms.service.impl;

import com.anonymous63.lms.dto.request.UserReqDto;
import com.anonymous63.lms.dto.response.UserResDto;
import com.anonymous63.lms.entity.User;
import com.anonymous63.lms.enums.AccountStatus;
import com.anonymous63.lms.mapper.UserMapper;
import com.anonymous63.lms.repository.UserRepo;
import com.anonymous63.lms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper mapper;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResDto createUser(UserReqDto dto) {
        User user = mapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // always hash
        user.setStatus(AccountStatus.ACTIVE);
        user.setEnabled(true);
        return mapper.toDto(userRepo.save(user));
    }

    @Override
    public UserResDto updateUser(Long id, UserReqDto dto) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // update fields
        user.setName(dto.getName());
        user.setPhoneNo(dto.getPhoneNo());
        user.setAddress(dto.getAddress());
        user.setAvatarUrl(dto.getAvatarUrl());
        user.setDateOfBirth(dto.getDateOfBirth());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return mapper.toDto(userRepo.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepo.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepo.deleteById(id);
    }

    @Override
    public UserResDto getUserById(Long id) {
        return userRepo.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<UserResDto> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResDto enableUser(Long id, boolean enabled) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(enabled);
        return mapper.toDto(userRepo.save(user));
    }

    @Override
    public UserResDto updateUserStatus(Long id, String status) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(AccountStatus.valueOf(status));
        return mapper.toDto(userRepo.save(user));
    }
}
