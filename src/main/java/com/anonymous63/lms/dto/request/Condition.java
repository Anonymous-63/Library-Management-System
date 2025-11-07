package com.anonymous63.lms.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Condition {
    private String category; // subject | resource | environment
    private String attribute; // e.g., roles, username, department, ownerId
    private String operator; // contains, ==, in, >
    private Object value;     // string, number, array, etc.
    private String connector; // "and" | "or" - used when joining conditions at same level (optional)
    private String group;     // optional grouping id to support parentheses
}
