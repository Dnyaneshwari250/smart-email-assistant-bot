package com.example.smartemailassistant.model;

public class EmailRequest {
    private String prompt;
    private String tone;
    private String recipient;
    private String subjectHint;

    // Constructors
    public EmailRequest() {}

    public EmailRequest(String prompt, String tone, String recipient, String subjectHint) {
        this.prompt = prompt;
        this.tone = tone;
        this.recipient = recipient;
        this.subjectHint = subjectHint;
    }

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