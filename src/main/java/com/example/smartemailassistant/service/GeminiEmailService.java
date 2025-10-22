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
        String emailBody = generateDetailedEmailBody(request);

        EmailResponse response = new EmailResponse();
        response.setGeneratedEmail(emailBody);
        response.setSubject(subject);

        System.out.println("✅ Email generated successfully!");
        return response;
    }

    private String generateSmartSubject(EmailRequest request) {
        String tone = request.getTone().toUpperCase();
        String base = request.getSubjectHint() != null ? request.getSubjectHint() : request.getTopic();

        switch (tone) {
            case "FORMAL":
                return "Formal Communication: " + base;
            case "PROFESSIONAL":
                return "Professional Update: " + base;
            case "CASUAL":
                return base;
            case "FRIENDLY":
                return "Friendly Update: " + base;
            default:
                return base;
        }
    }

    private String generateDetailedEmailBody(EmailRequest request) {
        String tone = request.getTone().toUpperCase();
        String recipient = request.getRecipient();
        String purpose = request.getPurpose() != null ? request.getPurpose() : request.getTopic();

        StringBuilder email = new StringBuilder();

        // Greeting
        email.append(getGreeting(tone)).append(" ").append(recipient).append(",\n\n");

        // Main content - multiple paragraphs for 75-100 words
        email.append(generateDetailedContent(purpose, tone)).append("\n\n");

        // Additional context or details
        email.append(generateAdditionalDetails(purpose, tone)).append("\n\n");

        // Call to action or next steps
        email.append(generateCallToAction(purpose, tone)).append("\n\n");

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

    private String generateDetailedContent(String purpose, String tone) {
        String[] formalOpenings = {
            "I am writing to bring to your attention regarding " + purpose + ". ",
            "This communication serves to formally address the matter of " + purpose + ". ",
            "I would like to take this opportunity to discuss " + purpose + " in detail. "
        };

        String[] professionalOpenings = {
            "I wanted to provide you with a comprehensive update regarding " + purpose + ". ",
            "Following our previous discussions, I would like to elaborate further on " + purpose + ". ",
            "I'm reaching out to provide detailed information about " + purpose + " and its implications. "
        };

        String[] casualOpenings = {
            "I wanted to touch base with you about " + purpose + " and share some thoughts. ",
            "Just wanted to give you a proper update about " + purpose + " and what's been happening. ",
            "I've been thinking about " + purpose + " and wanted to share some details with you. "
        };

        String[] friendlyOpenings = {
            "Hope you're doing well! I wanted to chat with you about " + purpose + " and catch up properly. ",
            "I've been meaning to connect with you regarding " + purpose + " and share some updates. ",
            "Wanted to take a moment to discuss " + purpose + " in more detail with you. "
        };

        String[] content;
        switch (tone) {
            case "FORMAL": content = formalOpenings; break;
            case "PROFESSIONAL": content = professionalOpenings; break;
            case "CASUAL": content = casualOpenings; break;
            case "FRIENDLY": content = friendlyOpenings; break;
            default: content = professionalOpenings;
        }

        return content[random.nextInt(content.length)];
    }

    private String generateAdditionalDetails(String purpose, String tone) {
        String[] formalDetails = {
            "This matter requires careful consideration and thorough evaluation to ensure we achieve the desired outcomes. " +
            "I have conducted preliminary research and analysis which indicates several important factors we should consider. " +
            "The implementation of this initiative will require coordinated efforts across multiple departments and stakeholders.",
            
            "After careful examination of the available data and current market conditions, I believe this approach offers significant advantages. " +
            "We should consider the long-term implications and potential challenges that may arise during execution. " +
            "Proper planning and resource allocation will be essential for successful implementation.",
            
            "The scope of this project encompasses multiple phases, each requiring specific expertise and dedicated resources. " +
            "We must establish clear milestones and performance indicators to monitor progress effectively. " +
            "Regular communication and status updates will help maintain alignment across all teams involved."
        };

        String[] professionalDetails = {
            "Based on my analysis, I've identified several key opportunities and potential challenges we should address. " +
            "The timeline for implementation appears feasible, though we may need to adjust based on resource availability. " +
            "I recommend we establish weekly check-ins to ensure we stay on track and address any emerging issues promptly.",
            
            "The feedback received so far has been generally positive, with some valuable suggestions for improvement. " +
            "We should consider conducting additional research to validate our assumptions and refine our approach. " +
            "Collaboration with other teams will be crucial for achieving our objectives efficiently.",
            
            "I've prepared a preliminary plan that outlines the major steps and estimated timeframes for completion. " +
            "We'll need to coordinate with the technical team to ensure proper implementation and testing. " +
            "Regular progress reviews will help us identify potential bottlenecks early in the process."
        };

        String[] casualDetails = {
            "I've been looking into this more and found some interesting insights that might help us move forward. " +
            "The team has been making good progress, though we've hit a few minor bumps along the way. " +
            "I think if we focus on the key priorities first, we can make solid headcount pretty quickly.",
            
            "From what I can see, things are shaping up nicely and we should be able to deliver good results. " +
            "There are a couple of areas where we might need some extra help or resources to get things done properly. " +
            "Let me know what you think about the approach and if you have any other ideas we should consider.",
            
            "I've put together some initial thoughts and would love to get your perspective on how to proceed. " +
            "The feedback so far has been really helpful in shaping our direction and priorities. " +
            "We're making steady progress and should have more concrete results to share soon."
        };

        String[] friendlyDetails = {
            "I'm really excited about the potential here and think we can achieve some great results together. " +
            "The team has been working hard and I'm impressed with the progress we've made so far. " +
            "I'd love to hear your thoughts and any ideas you might have to make this even better.",
            
            "This has been such an interesting project to work on and I appreciate all your support and input. " +
            "We're building something really special here and I'm confident we can deliver excellent results. " +
            "Let me know when you have a moment to chat more about this - I value your perspective greatly.",
            
            "I've been really enjoying collaborating with you on this and appreciate all your great ideas. " +
            "The progress we've made is really encouraging and I'm optimistic about what we can achieve. " +
            "Looking forward to continuing this work together and creating something we can all be proud of."
        };

        String[] content;
        switch (tone) {
            case "FORMAL": content = formalDetails; break;
            case "PROFESSIONAL": content = professionalDetails; break;
            case "CASUAL": content = casualDetails; break;
            case "FRIENDLY": content = friendlyDetails; break;
            default: content = professionalDetails;
        }

        return content[random.nextInt(content.length)];
    }

    private String generateCallToAction(String purpose, String tone) {
        String[] formalEndings = {
            "I would appreciate your feedback and insights on this matter at your earliest convenience. " +
            "Please let me know when you might be available to discuss this further in our upcoming meeting.",
            
            "I look forward to your response and guidance on how you would like to proceed with this initiative. " +
            "Your input would be invaluable in shaping our approach and ensuring successful implementation.",
            
            "I await your instructions regarding the next steps and am prepared to provide any additional information required. " +
            "Please do not hesitate to contact me should you require further clarification or supporting documentation."
        };

        String[] professionalEndings = {
            "Please let me know your thoughts and availability for a follow-up discussion next week. " +
            "I'm happy to provide additional details or adjust the approach based on your feedback.",
            
            "Looking forward to hearing your perspective and working together to move this forward. " +
            "Let me know what timing works best for you to connect and dive deeper into the details.",
            
            "Your input would be greatly appreciated as we finalize our plans and begin implementation. " +
            "I'm available to discuss this further whenever works best for your schedule."
        };

        String[] casualEndings = {
            "Let me know what you think when you get a chance - no rush! " +
            "Happy to chat more about this whenever works for you.",
            
            "Looking forward to hearing your thoughts and catching up properly soon. " +
            "Just shoot me a message when you have some time to connect.",
            
            "Let me know if you have any questions or want to bounce around some more ideas. " +
            "Talk soon and looking forward to continuing the conversation!"
        };

        String[] friendlyEndings = {
            "Can't wait to hear what you think and continue working on this together! " +
            "Let me know when you're free to chat more about this exciting project.",
            
            "Really looking forward to your thoughts and catching up properly soon. " +
            "Hope we can connect sometime next week to discuss this further!",
            
            "Excited to hear your perspective and keep moving forward with this great work. " +
            "Let me know when works best for you to connect and brainstorm some more!"
        };

        String[] content;
        switch (tone) {
            case "FORMAL": content = formalEndings; break;
            case "PROFESSIONAL": content = professionalEndings; break;
            case "CASUAL": content = casualEndings; break;
            case "FRIENDLY": content = friendlyEndings; break;
            default: content = professionalEndings;
        }

        return content[random.nextInt(content.length)];
    }

    private String getClosing(String tone) {
        switch (tone) {
            case "FORMAL": return "Sincerely";
            case "PROFESSIONAL": return "Best regards";
            case "CASUAL": return "Best";
            case "FRIENDLY": return "Warm regards";
            default: return "Best regards";
        }
    }
}
