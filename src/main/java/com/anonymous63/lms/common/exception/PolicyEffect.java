package com.anonymous63.lms.common.exception;


public enum PolicyEffect {
    ALLOW,
    DENY;

    public static PolicyEffect fromString(String s) {
        if (s == null) return ALLOW;
        try {
            return PolicyEffect.valueOf(s.toUpperCase());
        } catch (Exception e) {
            return ALLOW;
        }
    }
}
