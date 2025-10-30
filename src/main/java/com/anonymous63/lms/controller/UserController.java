package com.anonymous63.lms.controller;

import com.anonymous63.lms.dto.request.UserReqDto;
import com.anonymous63.lms.dto.response.UserResDto;
import com.anonymous63.lms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
//@PreAuthorize("hasAuthority('MANAGE_USERS')")
@PreAuthorize("@permissionEvaluator.hasPermission(authentication, null, null)")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // ✅ Create User
    @PostMapping
    public ResponseEntity<UserResDto> createUser(@RequestBody UserReqDto dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    // ✅ Update UserÏ
    @PutMapping("/{id}")
    public ResponseEntity<UserResDto> updateUser(
            @PathVariable Long id,
            @RequestBody UserReqDto dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    // ✅ Delete User
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Get User by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // ✅ Get All Users
    @GetMapping
    public ResponseEntity<List<UserResDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // ✅ Enable / Disable User
    @PatchMapping("/{id}/enable")
    public ResponseEntity<UserResDto> enableUser(
            @PathVariable Long id,
            @RequestParam boolean enabled) {
        return ResponseEntity.ok(userService.enableUser(id, enabled));
    }

    // ✅ Update User Status
    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResDto> updateUserStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(userService.updateUserStatus(id, status));
    }
}
