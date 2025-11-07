package com.anonymous63.lms.controller;

import com.anonymous63.lms.entity.AbacPolicy;
import com.anonymous63.lms.service.AbacPolicyService;
import com.anonymous63.lms.service.impl.AbacUserPolicyService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/abac-policies")
public class AbacPolicyController {

    private final AbacPolicyService abacPolicyService;
    private final AbacUserPolicyService abacUserPolicyService;

    @GetMapping
    public List<AbacPolicy> getAll() {
        return abacPolicyService.getAllPolicies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AbacPolicy> getById(@PathVariable Long id) {
        return abacPolicyService.getPolicyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public AbacPolicy create(@RequestBody AbacPolicy policy) {
        return abacPolicyService.createPolicy(policy);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AbacPolicy> update(@PathVariable Long id, @RequestBody AbacPolicy policy) {
        return abacPolicyService.getPolicyById(id)
                .map(existing -> {
                    existing.setName(policy.getName());
                    existing.setDescription(policy.getDescription());
                    existing.setResourceType(policy.getResourceType());
                    existing.setAction(policy.getAction());
                    existing.setEffect(policy.getEffect());
                    existing.setConditions(policy.getConditions());
                    return ResponseEntity.ok(abacPolicyService.createPolicy(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        abacPolicyService.deletePolicy(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mine")
    public ResponseEntity<Map<String, Map<String, Boolean>>> getMyPolicies() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Map<String, Boolean>> userPolicies = abacUserPolicyService.getUserPolicies(auth);
        return ResponseEntity.ok(userPolicies);
    }

}
