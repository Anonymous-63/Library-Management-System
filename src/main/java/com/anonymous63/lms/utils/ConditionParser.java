package com.anonymous63.lms.utils;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ConditionParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Map<String, Object>> parse(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON conditions: " + jsonString, e);
        }
    }
}
