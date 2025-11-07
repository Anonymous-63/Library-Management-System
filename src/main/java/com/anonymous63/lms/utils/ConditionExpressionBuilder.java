package com.anonymous63.lms.utils;

import com.anonymous63.lms.common.exception.ConditionOperator;
import com.anonymous63.lms.dto.request.Condition;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ConditionExpressionBuilder {
    public String buildExpression(List<Condition> conditions) {
        if (conditions == null || conditions.isEmpty()) return "true";

        // Group by 'group' if grouping is used; otherwise single group
        Map<String, List<Condition>> groups = new LinkedHashMap<>();
        String defaultGroupKey = "_default_";
        for (Condition c : conditions) {
            String g = c.getGroup() == null ? defaultGroupKey : c.getGroup();
            groups.computeIfAbsent(g, k -> new ArrayList<>()).add(c);
        }

        // build each group into an expression (join inside group by connector)
        List<String> groupExprs = new ArrayList<>();
        for (List<Condition> group : groups.values()) {
            String groupExpr = buildGroupExpression(group);
            groupExprs.add("(" + groupExpr + ")");
        }

        // join groups with AND (if you want OR between groups, change here or include connector at group level)
        return String.join(" and ", groupExprs);
    }

    private String buildGroupExpression(List<Condition> group) {
        List<String> exprParts = new ArrayList<>();
        for (Condition c : group) {
            String cat = Optional.ofNullable(c.getCategory()).orElse("subject");
            String attr = c.getAttribute();
            ConditionOperator op = ConditionOperator.fromString(c.getOperator());
            String valueExpr = literalValue(c.getValue());
            String leftExpr = String.format("#%s['%s']", cat, attr);

            String part = switch (op) {
                case CONTAINS -> String.format("(%s != null and %s.contains(%s))", leftExpr, leftExpr, valueExpr);
                case NOT_CONTAINS -> String.format("(%s == null or !%s.contains(%s))", leftExpr, leftExpr, valueExpr);
                case IN -> String.format("(%s != null and %s.contains(%s))", leftExpr, leftExpr, valueExpr);
                case NOT_IN -> String.format("(%s == null or !%s.contains(%s))", leftExpr, leftExpr, valueExpr);
                case EQ, NE, GT, LT, GTE, LTE -> String.format("(%s %s %s)", leftExpr, op.symbol(), valueExpr);
                default -> throw new IllegalStateException("Unhandled operator: " + op);
            };

            exprParts.add(part);
        }

        // join by connector; default to "and"
        String joined = exprParts.get(0);
        for (int i = 1; i < group.size(); i++) {
            String connector = Optional.ofNullable(group.get(i).getConnector()).orElse("and").toLowerCase();
            joined = "(" + joined + ") " + connector + " (" + exprParts.get(i) + ")";
        }
        return joined;
    }

    private String literalValue(Object value) {
        if (value == null) return "null";
        if (value instanceof Number) return String.valueOf(value);
        if (value instanceof Boolean) return String.valueOf(value);
        if (value instanceof Collection<?>) {
            // produce a list literal: {'a','b'}
            Collection<?> col = (Collection<?>) value;
            String joined = col.stream().map(this::literalValue).collect(Collectors.joining(","));
            return "{" + joined + "}";
        }
        // default treat as string
        String escaped = value.toString().replace("'", "\\'");
        return "'" + escaped + "'";
    }
}
