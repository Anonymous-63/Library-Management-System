package com.anonymous63.lms.service.impl;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DocumentService {

    public String read(Map<String, Object> doc) { return "doc read: " + doc.get("owner"); }


    public String update(Map<String, Object> doc) { return "doc updated: " + doc.get("id"); }
}