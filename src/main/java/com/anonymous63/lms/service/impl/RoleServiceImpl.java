package com.anonymous63.lms.service.impl;

import com.anonymous63.lms.dto.request.RoleReqDto;
import com.anonymous63.lms.dto.response.RoleResDto;
import com.anonymous63.lms.entity.Privilege;
import com.anonymous63.lms.entity.Role;
import com.anonymous63.lms.mapper.RoleMapper;
import com.anonymous63.lms.repository.PrivilegeRepo;
import com.anonymous63.lms.repository.RoleRepo;
import com.anonymous63.lms.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;
    private final PrivilegeRepo privilegeRepo;
    private final RoleMapper mapper;

    @Override
    public RoleResDto createRole(RoleReqDto dto) {
        Role role = new Role();
        role.setName(dto.getName());
        if (dto.getPrivilegeIds() != null) {
            Set<Privilege> privileges = new HashSet<>(privilegeRepo.findAllById(dto.getPrivilegeIds()));
            role.setPrivileges(privileges);
        }
        role = roleRepo.save(role);
        return mapper.toDto(role);
    }

    @Override
    public RoleResDto updateRole(Long roleId, RoleReqDto dto) {
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        role.setName(dto.getName());
        if (dto.getPrivilegeIds() != null) {
            Set<Privilege> privileges = new HashSet<>(privilegeRepo.findAllById(dto.getPrivilegeIds()));
            role.setPrivileges(privileges);
        }
        return mapper.toDto(roleRepo.save(role));
    }

    @Override
    public void deleteRole(Long roleId) {
        roleRepo.deleteById(roleId);
    }

    @Override
    public List<RoleResDto> getAllRoles() {
        return roleRepo.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }
}
