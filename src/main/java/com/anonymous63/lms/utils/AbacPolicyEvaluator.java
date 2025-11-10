package com.anonymous63.lms.utils;

import com.anonymous63.lms.common.exception.PolicyEffect;
import com.anonymous63.lms.dto.request.Condition;
import com.anonymous63.lms.entity.AbacPolicy;
import com.anonymous63.lms.repository.AbacPolicyRepo;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class AbacPolicyEvaluator {

    private final AbacPolicyRepo abacPolicyRepo;
    private final AbacAttributeProvider attributeProvider;
    private final ConditionParser conditionParser;
    private final ConditionExpressionBuilder expressionBuilder;
    private final ExpressionParser spelParser = new SpelExpressionParser();

    public AbacPolicyEvaluator(AbacPolicyRepo abacPolicyRepo,
                               AbacAttributeProvider attributeProvider,
                               ConditionParser conditionParser,
                               ConditionExpressionBuilder expressionBuilder) {
        this.abacPolicyRepo = abacPolicyRepo;
        this.attributeProvider = attributeProvider;
        this.conditionParser = conditionParser;
        this.expressionBuilder = expressionBuilder;
    }

    /**
     * Evaluate policies for resourceType + action.
     * Behavior:
     * - Loads policies (cached)
     * - Evaluates in priority order (repo order)
     * - If any DENY policy matches -> returns false
     * - If any ALLOW policy matches -> returns true
     * - Otherwise default deny (false)
     */
    @Transactional(readOnly = true)
    public boolean evaluatePolicy(org.springframework.security.core.Authentication auth,
                                  String resourceType,
                                  String action,
                                  Object resource) {
        List<AbacPolicy> policies = abacPolicyRepo.findByResourceTypeAndAction(resourceType, action);
        if (policies == null || policies.isEmpty()) {
//            if (log.isTraceEnabled()) log.trace("No policies for: {}/{}", resourceType, action);
            return false; // default deny
        }

        Map<String, Object> subject = attributeProvider.subject(auth);
        Map<String, Object> resourceAttrs = attributeProvider.resource(resource);

        for (AbacPolicy policy : policies) {
            if (!policy.isEnabled()) continue;
            try {
                List<Condition> conditions = conditionParser.parse(policy.getConditions());
                String spel = expressionBuilder.buildExpression(conditions);

                /*if (log.isTraceEnabled()) {
                    log.trace("Evaluating policy {} (effect={}) => spel: {}", policy.getName(), policy.getEffect(), spel);
                }*/

                StandardEvaluationContext ctx = new StandardEvaluationContext();
                ctx.setVariable("subject", subject);
                ctx.setVariable("resource", resourceAttrs);

                Expression exp = spelParser.parseExpression(spel);
                Boolean matched = exp.getValue(ctx, Boolean.class);

                boolean isMatched = Boolean.TRUE.equals(matched);

                if (isMatched) {
                    PolicyEffect effect = PolicyEffect.fromString(policy.getEffect());
                    if (effect == PolicyEffect.DENY) {
//                        if (log.isDebugEnabled()) log.debug("Policy '{}' DENY matched. Access denied.", policy.getName());
                        return false; // DENY short-circuit
                    } else {
//                        if (log.isDebugEnabled()) log.debug("Policy '{}' ALLOW matched. Access granted.", policy.getName());
                        return true; // ALLOW
                    }
                }
            } catch (Exception e) {
//                log.error("Failed to evaluate policy '{}' for {}/{}: {}", policy.getName(), resourceType, action, e.getMessage(), e);
            }
        }

//        if (log.isTraceEnabled()) log.trace("No matching policy effect found. Deny by default.");
        return false; // default deny
    }
}
