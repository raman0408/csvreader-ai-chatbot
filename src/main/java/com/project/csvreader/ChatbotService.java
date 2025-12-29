package com.project.csvreader;

import dev.langchain4j.model.ollama.OllamaLanguageModel;
import dev.langchain4j.model.output.Response;

import java.time.Duration;
import java.util.List;

public class ChatbotService {
    private final OllamaLanguageModel model;
    private String systemPrompt;

    public ChatbotService() {
        this.model = OllamaLanguageModel.builder()
                .baseUrl(ConfigLoader.getEnvBaseUrl())
                .modelName(ConfigLoader.getModelName())
                .temperature(ConfigLoader.getTemperature())
                .timeout(Duration.ofSeconds(ConfigLoader.getTimeoutSeconds()))
                .logRequests(true)
                .logResponses(true)
                .build();

        reloadKnowledgeBase(); // Initial load
    }

    /**
     * Public setter to allow tests to override the system prompt
     */
    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }

    /**
     * Reloads the knowledge base and rebuilds the system prompt.
     */
    public void reloadKnowledgeBase() {
        KnowledgeBaseReader.KBData kbData = KnowledgeBaseReader.getKnowledgeBase();
        this.systemPrompt = buildSystemPrompt(kbData.systemPrompt, kbData.knowledgeBase);
    }

    /**
     * Get chatbot response for a given user input.
     * Removes <think> tags and leading "A:" if present.
     */
    public String getResponse(String userInput) {
        String fullPrompt = systemPrompt + "\nUser: " + userInput + "\nAssistant: ";
        Response<String> response = model.generate(fullPrompt);

        return response.content()
                .replaceAll("(?s)<think>.*?</think>", "")
                .replaceAll("(?i)^\\s*A:\\s*", "")
                .trim();
    }

    private String buildSystemPrompt(String basePrompt, List<String> knowledgeBase) {
        StringBuilder prompt = new StringBuilder(basePrompt).append("\n");
        for (String qa : knowledgeBase) {
            prompt.append(qa).append("\n");
        }
        return prompt.toString();
    }
}