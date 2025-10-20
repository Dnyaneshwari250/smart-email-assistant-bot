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
    public String getPrompt() {
        return prompt != null ? prompt.trim() : "";
    }
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getTone() {
        return tone != null ? tone : "FORMAL";
    }
    public void setTone(String tone) {
        this.tone = tone;
    }

    public String getRecipient() {
        return recipient != null ? recipient.trim() : "";
    }
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubjectHint() {
        return subjectHint != null ? subjectHint.trim() : "";
    }
    public void setSubjectHint(String subjectHint) {
        this.subjectHint = subjectHint;
    }

    @Override
    public String toString() {
        return "EmailRequest{" +
                "prompt='" + prompt + '\'' +
                ", tone='" + tone + '\'' +
                ", recipient='" + recipient + '\'' +
                ", subjectHint='" + subjectHint + '\'' +
                '}';
    }
}