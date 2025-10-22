package com.example.smartemailassistant.service;

import org.springframework.stereotype.Service;
import com.example.smartemailassistant.model.EmailRequest;
import com.example.smartemailassistant.model.EmailResponse;
import java.util.Random;

@Service
public class GeminiEmailService {

    private final Random random = new Random();

    public EmailResponse generateEmail(EmailRequest request) {
        System.out.println("🎯 Generating AI-powered email...");

        String subject = generateSmartSubject(request);
        String emailBody = generateSmartEmailBody(request);

        EmailResponse response = new EmailResponse();
        response.setGeneratedEmail(emailBody);
        response.setSubject(subject);

        System.out.println("✅ Email generated successfully!");
        return response;
    }

    private String generateSmartSubject(EmailRequest request) {
        String tone = request.getTone().toUpperCase();
        String base = request.getSubjectHint();

        switch (tone) {
            case "FORMAL":
                return "Formal Communication: " + base;
            case "PROFESSIONAL":
                return "Update: " + base;
            case "CASUAL":
                return base;
            case "FRIENDLY":
                return "Quick Update: " + base;
            default:
                return base;
        }
    }

    private String generateSmartEmailBody(EmailRequest request) {
        String tone = request.getTone().toUpperCase();
        String recipient = request.getRecipient();
        String purpose = request.getPurpose();

        StringBuilder email = new StringBuilder();

        // Greeting
        email.append(getGreeting(tone)).append(" ").append(recipient).append(",\n\n");

        // Professional body based on purpose and tone
        email.append(generateProfessionalContent(purpose, tone)).append("\n\n");

        // Closing
        email.append(getClosing(tone)).append(",\n[Your Name]");

        return email.toString();
    }

    private String getGreeting(String tone) {
        switch (tone) {
            case "FORMAL": return "Dear";
            case "PROFESSIONAL": return "Hello";
            case "CASUAL": return "Hi";
            case "FRIENDLY": return "Hi there";
            default: return "Hello";
        }
    }

    private String generateProfessionalContent(String purpose, String tone) {
        String[] formalPhrases = {
            "I am writing to bring to your attention regarding " + purpose + ". ",
            "This communication serves to address the matter of " + purpose + ". ",
            "I would like to formally discuss " + purpose + ". "
        };

        String[] professionalPhrases = {
            "I wanted to follow up on " + purpose + ". ",
            "Regarding " + purpose + ", I think we should discuss this further. ",
            "I'm reaching out about " + purpose + ". "
        };

        String[] casualPhrases = {
            "Just wanted to touch base about " + purpose + ". ",
            "Quick update: " + purpose + ". ",
            "Following up on " + purpose + ". "
        };

        String[] friendlyPhrases = {
            "Hope you're doing well! I wanted to chat about " + purpose + ". ",
            "Just circling back on " + purpose + ". ",
            "Wanted to quickly connect about " + purpose + ". "
        };

        String[] content;
        switch (tone) {
            case "FORMAL": content = formalPhrases; break;
            case "PROFESSIONAL": content = professionalPhrases; break;
            case "CASUAL": content = casualPhrases; break;
            case "FRIENDLY": content = friendlyPhrases; break;
            default: content = professionalPhrases;
        }

        String mainContent = content[random.nextInt(content.length)];

        // Add contextually appropriate ending
        String[] endings = {
            "I look forward to your response.",
            "Please let me know your thoughts.",
            "Your input would be greatly appreciated.",
            "Looking forward to hearing from you.",
            "Let me know what you think.",
            "Talk soon!"
        };

        String ending = endings[random.nextInt(endings.length)];
        
        // Make ending more formal for formal tone
        if ("FORMAL".equals(tone)) {
            ending = "I look forward to your prompt response.";
        }

        return mainContent + ending;
    }

    private String getClosing(String tone) {
        switch (tone) {
            case "FORMAL": return "Sincerely";
            case "PROFESSIONAL": return "Best regards";
            case "CASUAL": return "Best";
            case "FRIENDLY": return "Cheers";
            default: return "Best regards";
        }
    }
}
