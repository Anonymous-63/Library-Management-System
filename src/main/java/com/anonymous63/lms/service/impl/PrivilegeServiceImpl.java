package com.anonymous63.lms.service.impl;

import com.anonymous63.lms.dto.request.PrivilegeReqDto;
import com.anonymous63.lms.dto.response.PrivilegeResDto;
import com.anonymous63.lms.entity.Privilege;
import com.anonymous63.lms.mapper.PrivilegeMapper;
import com.anonymous63.lms.repository.PrivilegeRepo;
import com.anonymous63.lms.repository.UserRepo;
import com.anonymous63.lms.service.PrivilegeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PrivilegeServiceImpl implements PrivilegeService {
    private final PrivilegeMapper mapper;
    private final PrivilegeRepo privilegeRepo;
    private final UserRepo userRepo;

    @Override
    public PrivilegeResDto createPrivilege(PrivilegeReqDto dto) {
        Privilege privilege = Privilege.builder()
                .name(dto.getName())
                .enabled(true)
                .build();
        privilege = privilegeRepo.save(privilege);
        return mapper.toDto(privilege);
    }

    @Override
    public PrivilegeResDto updatePrivilege(Long id, PrivilegeReqDto dto) {
        Privilege privilege = privilegeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Privilege not found"));
        privilege.setName(dto.getName());
        return mapper.toDto(privilegeRepo.save(privilege));
    }

    @Override
    public void deletePrivilege(Long id) {
        privilegeRepo.deleteById(id);
    }

    @Override
    public List<PrivilegeResDto> getAllPrivileges() {
        return privilegeRepo.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Privilege getByName(String name) {
        return privilegeRepo.findByName(name)
                .orElseThrow(() -> new RuntimeException("Privilege not found"));
    }

    @Override
    public boolean userHasPrivilege(String email, String privilegeName) {
        return userRepo.findByEmail(email)
                .map(user -> user.getRoles().stream()
                        .flatMap(role -> role.getPrivileges().stream())
                        .anyMatch(privilege -> privilege.getName().equals(privilegeName)))
                .orElse(false);
    }
}
