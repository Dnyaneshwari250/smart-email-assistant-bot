package com.example.smartemailassistant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    
    // Remove the root mapping to allow index.html to be served
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
    
    @GetMapping("/api/health")
    public String apiHealth() {
        return "API is healthy";
    }
}
