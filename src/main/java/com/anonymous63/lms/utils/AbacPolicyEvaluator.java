package com.anonymous63.lms.utils;

import com.anonymous63.lms.entity.AbacPolicy;
import com.anonymous63.lms.repository.AbacPolicyRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class AbacPolicyEvaluator {

    private final AbacPolicyRepo abacPolicyRepo;
    private final AbacAttributeProvider abacAttributeProvider;
    private final ExpressionParser expressionParser = new SpelExpressionParser();

    public AbacPolicyEvaluator(AbacPolicyRepo repository, AbacAttributeProvider contextProvider) {
        this.abacPolicyRepo = repository;
        this.abacAttributeProvider = contextProvider;
    }

    public boolean evaluatePolicy(Authentication auth, String resourceType, String action, Object resource) {
        List<AbacPolicy> policies = abacPolicyRepo.findByResourceTypeAndAction(resourceType, action);

        Map<String, Object> subjectAttrs = abacAttributeProvider.subject(auth);
        Map<String, Object> resourceAttrs = abacAttributeProvider.resource(resource);

        for (AbacPolicy policy : policies) {
            try {
                StandardEvaluationContext context = new StandardEvaluationContext();
                context.setVariable("subject", subjectAttrs);
                context.setVariable("resource", resourceAttrs);

                Expression expression = expressionParser.parseExpression(policy.getConditions());

                Boolean result = expression.getValue(context, Boolean.class);
                if (Boolean.TRUE.equals(result)) {
                    return "ALLOW".equalsIgnoreCase(policy.getEffect());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
