package com.anonymous63.lms.utils;

import com.anonymous63.lms.repository.EndpointPermissionMappingRepo;
import com.anonymous63.lms.service.PrivilegeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serializable;

@Component("permissionEvaluator")
public class DynamicPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private EndpointPermissionMappingRepo mappingRepo;

    @Autowired
    private PrivilegeService privilegeService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String path = request.getRequestURI();
        String method = request.getMethod();

        String requiredPrivilege = mappingRepo.findPrivilegeByEndpointAndMethod(path, method);
        if (requiredPrivilege == null) return false;

        return privilegeService.userHasPrivilege(authentication.getName(), requiredPrivilege);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication, targetType, permission);
    }
}
