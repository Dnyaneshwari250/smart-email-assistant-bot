package com.example.smartemailassistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.smartemailassistant.model.EmailRequest;
import com.example.smartemailassistant.model.EmailResponse;
import com.example.smartemailassistant.service.OllamaEmailService;

@Controller
public class EmailController {

    @Autowired
    private OllamaEmailService emailService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("emailRequest", new EmailRequest());
        // Initialize empty response
        EmailResponse emptyResponse = new EmailResponse();
        emptyResponse.setSuccess(false);
        model.addAttribute("emailResponse", emptyResponse);
        return "index";
    }

    @PostMapping("/generate")
    @ResponseBody
    public EmailResponse generateEmail(@RequestBody EmailRequest request) {
        return emailService.generateEmail(request);
    }
}