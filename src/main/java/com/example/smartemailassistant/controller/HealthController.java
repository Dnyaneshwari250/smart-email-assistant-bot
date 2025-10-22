package com.example.smartemailassistant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("status", "UP");
        healthStatus.put("service", "Smart Email Assistant");
        healthStatus.put("version", "1.0.0");
        healthStatus.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(healthStatus);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> appInfo() {
        Map<String, Object> appInfo = new HashMap<>();
        appInfo.put("name", "Smart Email Assistant");
        appInfo.put("description", "AI-powered email generation assistant");
        appInfo.put("version", "1.0.0");
        appInfo.put("author", "Your Name");
        appInfo.put("endpoints", new String[]{
            "/api/health",
            "/api/info", 
            "/api/generate-email"
        });
        
        return ResponseEntity.ok(appInfo);
    }
}
