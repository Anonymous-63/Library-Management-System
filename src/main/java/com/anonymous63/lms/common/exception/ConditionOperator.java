package com.anonymous63.lms.common.exception;

public enum ConditionOperator {
    EQ("=="),
    NE("!="),
    GT(">"),
    LT("<"),
    GTE(">="),
    LTE("<="),
    CONTAINS("contains"),
    NOT_CONTAINS("!contains"),
    IN("in"),
    NOT_IN("!in");

    private final String symbol;

    ConditionOperator(String symbol) {
        this.symbol = symbol;
    }

    public String symbol() {
        return symbol;
    }

    public static ConditionOperator fromString(String s) {
        if (s == null) throw new IllegalArgumentException("operator is null");
        String normalized = s.trim().toLowerCase();
        return switch (normalized) {
            case "=", "==", "eq" -> EQ;
            case "!=", "ne" -> NE;
            case ">" -> GT;
            case "<" -> LT;
            case ">=" -> GTE;
            case "<=" -> LTE;
            case "contains" -> CONTAINS;
            case "!contains" -> NOT_CONTAINS;
            case "in" -> IN;
            case "!in" -> NOT_IN;
            default -> throw new IllegalArgumentException("Unknown operator: " + s);
        };
    }
}