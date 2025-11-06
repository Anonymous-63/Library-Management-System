package com.anonymous63.lms.controller;

import com.anonymous63.lms.entity.AbacPolicy;
import com.anonymous63.lms.service.AbacPolicyService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/abac-policy")
public class AbacPolicyController {

    private final AbacPolicyService abacPolicyService;

    @PostMapping("/add")
    public ResponseEntity<?> login(@Valid @RequestBody JsonNode policyJson) {
        AbacPolicy policy = new AbacPolicy();
        policy.setName(policyJson.get("name").asText());
        policy.setResourceType(policyJson.get("resourceType").asText());
        policy.setAction(policyJson.get("action").asText());
        policy.setConditions(policyJson.get("conditions").toString()); // ✅ store full JSON as text

        abacPolicyService.createPolicy(policy);
        return ResponseEntity.ok("Policy saved successfully!");
    }
}
