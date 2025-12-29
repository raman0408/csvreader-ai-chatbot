# Domain-Specific AI Chatbot

## Overview
This is a domain-specific AI chatbot that only answers questions from a fixed knowledge base. It refuses to answer out-of-scope questions, ensuring reliability and preventing hallucinations.

## Features
- Answers questions strictly from the knowledge base
- Refuses unrelated queries politely
- Commands:
  - `/reload` - Reload knowledge base from CSV
  - `/repeat n question` - Ask the same question multiple times
  - `/exit` - Quit the chatbot

## Tech Stack
- Java 21
- Maven for project management
- Ollama Qwen3:1.7b as LLM
- Apache Commons CSV & Text for CSV parsing and text similarity

## How to Run
1. Clone the repo: `git clone <your-repo-url>`
2. Build project: `mvn clean compile`
3. Run chatbot: `mvn exec:java -Dexec.mainClass="com.project.csvreader.ChatbotAgent"`

## Notes
- Ensure Ollama server is running locally at `http://localhost:11434`
- Update `config.properties` for model or environment changes