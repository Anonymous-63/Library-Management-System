package com.anonymous63.lms.controller;

import com.anonymous63.lms.dto.request.PrivilegeReqDto;
import com.anonymous63.lms.dto.response.PrivilegeResDto;
import com.anonymous63.lms.service.PrivilegeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/privileges")
@RequiredArgsConstructor
public class PrivilegeController {
    private final PrivilegeService privilegeService;

    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    @PostMapping
    public PrivilegeResDto create(@RequestBody PrivilegeReqDto dto) {
        return privilegeService.createPrivilege(dto);
    }

    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    @PutMapping("/{id}")
    public PrivilegeResDto update(@PathVariable Long id, @RequestBody PrivilegeReqDto dto) {
        return privilegeService.updatePrivilege(id, dto);
    }

    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        privilegeService.deletePrivilege(id);
    }

    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    @GetMapping
    public List<PrivilegeResDto> getAll() {
        return privilegeService.getAllPrivileges();
    }
}
