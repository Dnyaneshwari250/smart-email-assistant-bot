package com.example.smartemailassistant.model;

public class EmailRequest {
    private String topic;
    private String tone;
    private String recipient;
    private String subject;
    private String purpose;
    private String subjectHint;

    public EmailRequest() {}

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getSubjectHint() {
        return subjectHint;
    }

    public void setSubjectHint(String subjectHint) {
        this.subjectHint = subjectHint;
    }
}
