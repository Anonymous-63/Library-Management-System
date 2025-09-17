package com.anonymous63.lms.service;

import com.anonymous63.lms.dto.request.PrivilegeReqDto;
import com.anonymous63.lms.dto.response.PrivilegeResDto;
import com.anonymous63.lms.entity.Privilege;

import java.util.List;

public interface PrivilegeService {
    PrivilegeResDto createPrivilege(PrivilegeReqDto dto);
    PrivilegeResDto updatePrivilege(Long id, PrivilegeReqDto dto);
    void deletePrivilege(Long id);
    List<PrivilegeResDto> getAllPrivileges();
    Privilege getByName(String name); // useful for role assignment
}
