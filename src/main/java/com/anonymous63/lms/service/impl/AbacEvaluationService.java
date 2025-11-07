package com.anonymous63.lms.service.impl;

import com.anonymous63.lms.common.exception.PolicyEffect;
import com.anonymous63.lms.dto.request.Condition;
import com.anonymous63.lms.entity.AbacPolicy;
import com.anonymous63.lms.repository.AbacPolicyRepo;
import com.anonymous63.lms.utils.AbacAttributeProvider;
import com.anonymous63.lms.utils.ConditionExpressionBuilder;
import com.anonymous63.lms.utils.ConditionParser;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AbacEvaluationService {

    private final AbacAttributeProvider attributeProvider;
    private final ConditionParser conditionParser;
    private final ConditionExpressionBuilder expressionBuilder;
    private final ExpressionParser spelParser = new SpelExpressionParser();

    public boolean evaluateCondition(String condition, Authentication auth, Object resource) {
        if (condition == null || condition.isBlank()) return true; // unconditional
        Map<String, Object> subject = attributeProvider.subject(auth);
        Map<String, Object> resourceAttrs = attributeProvider.resource(resource);

        try {
            List<Condition> conditions = conditionParser.parse(condition);
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
                return true;
            }
        } catch (Exception e) {
//                log.error("Failed to evaluate policy '{}' for {}/{}: {}", policy.getName(), resourceType, action, e.getMessage(), e);
        }
        return false;
    }

    public boolean isAllowed(AbacPolicy policy, Authentication auth, Object resource) {
        boolean matches = evaluateCondition(policy.getConditions(), auth, resource);
        return matches && "ALLOW".equalsIgnoreCase(policy.getEffect());
    }
}
