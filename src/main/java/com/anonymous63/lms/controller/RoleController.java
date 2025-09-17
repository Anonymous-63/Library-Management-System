package com.anonymous63.lms.controller;

import com.anonymous63.lms.dto.request.RoleReqDto;
import com.anonymous63.lms.dto.response.RoleResDto;
import com.anonymous63.lms.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    @PostMapping
    public ResponseEntity<RoleResDto> createRole(@RequestBody RoleReqDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(dto));
    }

    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    @PutMapping("/{roleId}")
    public ResponseEntity<RoleResDto> updateRole(
            @PathVariable Long roleId, @RequestBody RoleReqDto dto) {
        return ResponseEntity.ok(roleService.updateRole(roleId, dto));
    }

    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    @GetMapping
    public ResponseEntity<List<RoleResDto>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }
}
