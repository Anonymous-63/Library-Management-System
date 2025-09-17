package com.anonymous63.lms.service;

import com.anonymous63.lms.dto.request.RoleReqDto;
import com.anonymous63.lms.dto.response.RoleResDto;

import java.util.List;

public interface RoleService {
    RoleResDto createRole(RoleReqDto dto);
    RoleResDto updateRole(Long roleId, RoleReqDto dto);
    void deleteRole(Long roleId);
    List<RoleResDto> getAllRoles();
}
