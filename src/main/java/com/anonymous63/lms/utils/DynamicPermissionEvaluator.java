package com.anonymous63.lms.utils;

import com.anonymous63.lms.entity.Policy;
import com.anonymous63.lms.entity.User;
import com.anonymous63.lms.repository.PolicyRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@Component("permissionEvaluator")
public class DynamicPermissionEvaluator implements PermissionEvaluator {

    private PolicyRepo policyRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (!(authentication.getPrincipal() instanceof User)) return false;

        User user = (User) authentication.getPrincipal();
        String action = permission.toString().toUpperCase();
        String resource = targetDomainObject.getClass().getSimpleName().toUpperCase();

        Map<String, String> resourceAttributes = extractAttributes(targetDomainObject);

        return evaluate(user, resource, action, resourceAttributes);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        // Optional: implement if you want permission by id and type
        return false;
    }

    private Map<String, String> extractAttributes(Object obj) {
        Map<String, String> attributes = new HashMap<>();
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value != null) {
                    attributes.put(field.getName(), value.toString());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return attributes;
    }

    private boolean evaluate(User user, String resource, String action, Map<String, String> resourceAttributes) {
        List<Policy> policies = policyRepo.findByResourceAndEnabledTrue(resource);

        for (Policy policy : policies) {
            try {
                JsonNode condition = objectMapper.readTree(policy.getConditionJson());

                // Role match
                JsonNode rolesNode = condition.path("roles");
                boolean roleMatch = rolesNode.isArray() && user.getRoles().stream()
                        .anyMatch(r -> StreamSupport.stream(rolesNode.spliterator(), false)
                                .anyMatch(role -> role.asText().equalsIgnoreCase(r.getName())));
                if (!roleMatch) continue;

                // Action match
                JsonNode actionsNode = condition.path("actions");
                boolean actionMatch = actionsNode.isArray() &&
                        StreamSupport.stream(actionsNode.spliterator(), false)
                                .anyMatch(a -> a.asText().equalsIgnoreCase(action));
                if (!actionMatch) continue;

                // Attribute match
                JsonNode attrNode = condition.path("attributes");
                boolean attrMatch = true;
                if (attrNode.isObject()) {
                    Iterator<String> fieldNames = attrNode.fieldNames();
                    while (fieldNames.hasNext()) {
                        String key = fieldNames.next();
                        if (!resourceAttributes.getOrDefault(key, "").equals(attrNode.get(key).asText())) {
                            attrMatch = false;
                            break;
                        }
                    }
                }

                if (attrMatch) return true;

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
