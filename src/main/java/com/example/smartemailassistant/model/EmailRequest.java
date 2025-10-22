package com.example.smartemailassistant.model;

public class EmailRequest {
    private String purpose;
    private String tone;
    private String recipient;
    private String subjectHint;

    public EmailRequest() {}

    public EmailRequest(String purpose, String tone, String recipient, String subjectHint) {
        this.purpose = purpose;
        this.tone = tone;
        this.recipient = recipient;
        this.subjectHint = subjectHint;
    }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getTone() { return tone; }
    public void setTone(String tone) { this.tone = tone; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public String getSubjectHint() { return subjectHint; }
    public void setSubjectHint(String subjectHint) { this.subjectHint = subjectHint; }

    public String getPrompt() { return purpose; }
}
