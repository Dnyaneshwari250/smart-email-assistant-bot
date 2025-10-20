package com.example.smartemailassistant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Smart Email Assistant is running!");
        response.put("service", "Google Gemini API");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> home() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to Smart Email Assistant!");
        response.put("endpoint", "Use POST /generate to create emails");
        response.put("example", "curl -X POST /generate -H \"Content-Type: application/json\" -d '{\"prompt\":\"meeting request\",\"tone\":\"FORMAL\",\"recipient\":\"manager\",\"subjectHint\":\"Meeting\"}'");
        return ResponseEntity.ok(response);
    }
}