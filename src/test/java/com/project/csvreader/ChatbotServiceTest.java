package com.project.csvreader;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChatbotServiceTest {

    private ChatbotService chatbot;

    @BeforeEach
    public void setup() {
        chatbot = new ChatbotService();
    }

    // Helper method to simulate repeating a question n times
    private String[] repeatQuestion(int n, String question) {
        String[] responses = new String[n];
        for (int i = 0; i < n; i++) {
            responses[i] = chatbot.getResponse(question);
        }
        return responses;
    }

    @Test
    public void testOutOfDomainBeforeSystemPrompt() {
        chatbot.reloadKnowledgeBase();
        chatbot.setSystemPrompt(""); // simulate no system prompt loaded

        String question = "What is the capital of India?";
        String response = chatbot.getResponse(question);

        assertTrue(response.toLowerCase().contains("new delhi"),
                "Before system prompt, answer should contain 'New Delhi'");
    }

    @Test
    public void testOutOfDomainAfterSystemPrompt() {
        chatbot.reloadKnowledgeBase(); // system prompt loaded normally

        String question = "What is the capital of India?";
        String response = chatbot.getResponse(question);

        assertFalse(response.toLowerCase().contains("new delhi"),
                "After system prompt, answer should NOT contain 'New Delhi'");
        assertTrue(response.toLowerCase().contains("sorry") || response.toLowerCase().contains("cannot"),
                "After system prompt, should return refusal message");
    }

    @Test
    public void testRepeatConsistency() {
        String question = "What is the leave policy for tournaments?";
        int repeats = 10;

        String[] responses = repeatQuestion(repeats, question);

        for (int i = 1; i < repeats; i++) {
            assertEquals(responses[0], responses[i], "All repeated answers should be the same");
        }
    }

    @Test
    public void testOutOfDomainBeforeSystemPromptRepeated() {
        chatbot.reloadKnowledgeBase();
        chatbot.setSystemPrompt(""); // simulate no system prompt

        String question = "What is the capital of India?";
        int repeats = 10;
        String[] responses = repeatQuestion(repeats, question);

        boolean foundCapital = false;
        for (String resp : responses) {
            if (resp.toLowerCase().contains("new delhi")) {
                foundCapital = true;
                break;
            }
        }
        assertTrue(foundCapital, "Expected at least one response to mention 'New Delhi' before system prompt.");
    }

    @Test
    public void testOutOfDomainAfterSystemPromptRepeated() {
        chatbot.reloadKnowledgeBase(); // system prompt active

        String question = "What is the capital of India?";
        int repeats = 10;
        String[] responses = repeatQuestion(repeats, question);

        for (String resp : responses) {
            assertFalse(resp.toLowerCase().contains("new delhi"),
                    "Expected refusal, but got mention of 'New Delhi' after system prompt.");
        }
    }
}

