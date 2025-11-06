package com.anonymous63.lms;

import com.anonymous63.lms.entity.AbacPolicy;
import com.anonymous63.lms.repository.AbacPolicyRepo;
import com.anonymous63.lms.utils.AbacAttributeProvider;
import com.anonymous63.lms.utils.AbacPolicyEvaluator;
import com.anonymous63.lms.utils.ConditionExpressionBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AdvancedPolicyEvaluatorTest {
    @Mock
    AbacPolicyRepo policyRepository;
    AbacAttributeProvider contextProvider = new AbacAttributeProvider();
    AbacPolicyEvaluator evaluator;
    ConditionExpressionBuilder expressionBuilder;
    ObjectMapper objectMapper;

    AbacPolicy adminRead, ownerUpdate, lockedDenyUpdate;

    @BeforeEach
    void setup() {
        policyRepository = mock(AbacPolicyRepo.class);
        evaluator = new AbacPolicyEvaluator(policyRepository, contextProvider, expressionBuilder, objectMapper);

        adminRead = new AbacPolicy();
        adminRead.setResourceType("document");
        adminRead.setAction("READ");
        adminRead.setEffect("ALLOW");
        adminRead.setConditions("#subject['roles'].contains('ADMIN') && !#resource['archived']");

        ownerUpdate = new AbacPolicy();
        ownerUpdate.setResourceType("document");
        ownerUpdate.setAction("UPDATE");
        ownerUpdate.setEffect("ALLOW");
        ownerUpdate.setConditions("#subject['username'] == #resource['owner']");

        lockedDenyUpdate = new AbacPolicy();
        lockedDenyUpdate.setResourceType("document");
        lockedDenyUpdate.setAction("UPDATE");
        lockedDenyUpdate.setEffect("DENY");
        lockedDenyUpdate.setConditions("#resource['locked'] == true");
    }

    @Test
    void testAdminCanReadActiveDoc() {
        List<AbacPolicy> policies = List.of(adminRead);
        when(policyRepository.findByResourceTypeAndAction("document", "READ")).thenReturn(policies);

        var auth = new UsernamePasswordAuthenticationToken("alice", "pw", List.of(new SimpleGrantedAuthority("ADMIN")));
        Map<String, Object> doc = Map.of("resourceType", "document", "archived", false);

        assertTrue(evaluator.evaluatePolicy(auth, "document", "READ", doc));
    }

    @Test
    void testOwnerCanUpdateUnlockDoc() {
        List<AbacPolicy> policies = List.of(lockedDenyUpdate, ownerUpdate);
        when(policyRepository.findByResourceTypeAndAction("document", "UPDATE")).thenReturn(policies);

        var auth = new UsernamePasswordAuthenticationToken("bob", "pw", List.of(new SimpleGrantedAuthority("USER")));
        Map<String, Object> doc = Map.of("resourceType", "document", "owner", "bob", "locked", false);

        assertTrue(evaluator.evaluatePolicy(auth, "document", "UPDATE", doc));
    }

    @Test
    void testLockedDocCannotUpdateEvenOwner() {
        List<AbacPolicy> policies = List.of(lockedDenyUpdate, ownerUpdate);
        when(policyRepository.findByResourceTypeAndAction("document", "UPDATE")).thenReturn(policies);

        var auth = new UsernamePasswordAuthenticationToken("bob", "pw", List.of(new SimpleGrantedAuthority("USER")));
        Map<String, Object> doc = Map.of("resourceType", "document", "owner", "bob", "locked", true);

        // DENY overrides
        assertFalse(evaluator.evaluatePolicy(auth, "document", "UPDATE", doc));
    }
}
