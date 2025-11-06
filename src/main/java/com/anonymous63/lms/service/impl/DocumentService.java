package com.anonymous63.lms.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DocumentService {
    @PreAuthorize("hasPermission(#doc,'READ')")
    public String read(Map<String, Object> doc) { return "doc read: " + doc.get("id"); }

    @PreAuthorize("hasPermission(#doc, 'UPDATE')")
    public String update(Map<String, Object> doc) { return "doc updated: " + doc.get("id"); }
}