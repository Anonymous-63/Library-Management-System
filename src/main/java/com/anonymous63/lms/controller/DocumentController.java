package com.anonymous63.lms.controller;

import com.anonymous63.lms.service.impl.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/documents")
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    // Test read permission
    @PostMapping("/read")
    @PreAuthorize("hasPermission(#doc,'ADD')")
    public String readDoc(@RequestBody Map<String, Object> doc) {
        // Authentication is auto-injected for logged in users
        try {
            return documentService.read(doc);
        } catch (AccessDeniedException e) {
            return "Access Denied: " + e.getMessage();
        }
    }

    // Test update permission
    @PostMapping("/update")
    @PreAuthorize("hasPermission(#doc, 'UPDATE')")
    public String updateDoc(@RequestBody Map<String, Object> doc) {
        try {
            return documentService.update(doc);
        } catch (AccessDeniedException e) {
            return "Access Denied: " + e.getMessage();
        }
    }
}
