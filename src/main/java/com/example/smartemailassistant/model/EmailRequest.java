package com.example.smartemailassistant.model;

public class EmailRequest {
    private String prompt;
    private String tone;
    private String recipient;
    private String subjectHint;

    // Default constructor
    public EmailRequest() {}

    // Getters and Setters
    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }

    public String getTone() { return tone; }
    public void setTone(String tone) { this.tone = tone; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public String getSubjectHint() { return subjectHint; }
    public void setSubjectHint(String subjectHint) { this.subjectHint = subjectHint; }
}