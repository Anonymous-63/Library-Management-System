package com.anonymous63.lms.utils;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ConditionExpressionBuilder {
    public String buildExpression(List<Map<String, Object>> conditions) {
        return conditions.stream()
                .map(c -> {
                    String category = (String) c.get("category");
                    String attribute = (String) c.get("attribute");
                    String operator = (String) c.get("operator");
                    Object value = c.get("value");

                    String exprValue = (value instanceof String)
                            ? "'" + value + "'"
                            : String.valueOf(value);

                    String op = switch (operator) {
                        case "=" -> "==";
                        case "!=" -> "!=";
                        case ">" -> ">";
                        case "<" -> "<";
                        default -> operator;
                    };

                    return String.format("#%s['%s'] %s %s", category, attribute, op, exprValue);
                })
                .collect(Collectors.joining(" and "));
    }
}
