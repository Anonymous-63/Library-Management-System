package com.anonymous63.lms.utils;


import com.anonymous63.lms.dto.request.Condition;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConditionParser {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Condition> parse(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid conditions JSON", e);
        }
    }
}
