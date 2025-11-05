package com.anonymous63.lms.controller;

import com.anonymous63.lms.dto.request.PrivilegeReqDto;
import com.anonymous63.lms.dto.response.PrivilegeResDto;
import com.anonymous63.lms.service.PrivilegeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/privileges")
public class PrivilegeController {
    private final PrivilegeService privilegeService;

    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, null, null)")
    @PostMapping
    public PrivilegeResDto create(@RequestBody PrivilegeReqDto dto) {
        return privilegeService.createPrivilege(dto);
    }

    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, null, null)")
    @PutMapping("/{id}")
    public PrivilegeResDto update(@PathVariable Long id, @RequestBody PrivilegeReqDto dto) {
        return privilegeService.updatePrivilege(id, dto);
    }

    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, null, null)")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        privilegeService.deletePrivilege(id);
    }

    @PreAuthorize("@permissionEvaluator.hasPermission(authentication, null, null)")
    @GetMapping
    public List<PrivilegeResDto> getAll() {
        return privilegeService.getAllPrivileges();
    }
}
