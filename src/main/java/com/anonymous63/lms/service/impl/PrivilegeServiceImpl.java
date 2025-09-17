package com.anonymous63.lms.service.impl;

import com.anonymous63.lms.dto.request.PrivilegeReqDto;
import com.anonymous63.lms.dto.response.PrivilegeResDto;
import com.anonymous63.lms.entity.Privilege;
import com.anonymous63.lms.repository.PrivilegeRepo;
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
    private final PrivilegeRepo privilegeRepo;

    @Override
    public PrivilegeResDto createPrivilege(PrivilegeReqDto dto) {
        Privilege privilege = Privilege.builder()
                .name(dto.getName())
                .enabled(true)
                .build();
        privilege = privilegeRepo.save(privilege);
        return mapToDto(privilege);
    }

    @Override
    public PrivilegeResDto updatePrivilege(Long id, PrivilegeReqDto dto) {
        Privilege privilege = privilegeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Privilege not found"));
        privilege.setName(dto.getName());
        return mapToDto(privilegeRepo.save(privilege));
    }

    @Override
    public void deletePrivilege(Long id) {
        privilegeRepo.deleteById(id);
    }

    @Override
    public List<PrivilegeResDto> getAllPrivileges() {
        return privilegeRepo.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public Privilege getByName(String name) {
        return privilegeRepo.findByName(name)
                .orElseThrow(() -> new RuntimeException("Privilege not found"));
    }

    private PrivilegeResDto mapToDto(Privilege privilege) {
        return PrivilegeResDto.builder()
                .id(privilege.getId())
                .name(privilege.getName())
                .enabled(privilege.isEnabled())
                .build();
    }
}
