package com.project.csvreader;

import java.util.Scanner;

public class ChatbotAgent {

    public static void main(String[] args) {
        ChatbotService chatbot = new ChatbotService();
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.print("You: ");
            String userIn = in.nextLine().trim();

            if (userIn.equalsIgnoreCase("/exit")) {
                System.out.println("Bot: Goodbye!");
                break;
            }

            if (userIn.equalsIgnoreCase("/reload")) {
                chatbot.reloadKnowledgeBase();
                System.out.println("Bot: Knowledge base reloaded.");
                continue;
            }

            if (userIn.toLowerCase().startsWith("/repeat")) {
                String[] parts = userIn.split("\\s+", 3);
                if (parts.length < 3) {
                    System.out.println("Bot: Usage: /repeat n question");
                    continue;
                }
                try {
                    int n = Integer.parseInt(parts[1]);
                    String question = parts[2];
                    for (int i = 0; i < n; i++) {
                        System.out.println("Bot [" + (i + 1) + "]: " + chatbot.getResponse(question));
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Bot: Invalid number in /repeat command.");
                }
                continue;
            }

            String answer = chatbot.getResponse(userIn);
            System.out.println("Bot: " + answer);
        }

        in.close();
    }
}