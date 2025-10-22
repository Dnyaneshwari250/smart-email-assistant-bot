package com.example.smartemailassistant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/")
    public String home() {
        return "Smart Email Assistant is running!";
    }
    
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
