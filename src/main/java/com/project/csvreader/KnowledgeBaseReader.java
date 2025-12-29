package com.project.csvreader;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class KnowledgeBaseReader {

    public static class KBData {
        public final String systemPrompt;
        public final List<String> knowledgeBase;

        public KBData(String systemPrompt, List<String> knowledgeBase) {
            this.systemPrompt = systemPrompt;
            this.knowledgeBase = knowledgeBase;
        }
    }

    @SuppressWarnings("deprecation")
    public static KBData getKnowledgeBase() {
        List<String> knowledgeBase = new ArrayList<>();
        String systemPrompt = "";

        try {
            Reader in = new FileReader("sports_corp_knowledge_base.csv");
            CSVFormat format = CSVFormat.Builder
                    .create(CSVFormat.DEFAULT)
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .build();

            Iterable<CSVRecord> records = format.parse(in);
            for (CSVRecord record : records) {
                String category = record.get("CATEGORY");
                String subCategory = record.get("SUB_CATEGORY");
                String question = record.get("QUESTION");
                String answer = record.get("ANSWER");

                // Check for system prompt entry
                if ("META".equalsIgnoreCase(category.trim()) && "SYSTEM_PROMPT".equalsIgnoreCase(subCategory.trim())) {
                    systemPrompt = answer.trim();
                } else {
                    knowledgeBase.add("Q: " + question + "\nA: " + answer);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new KBData(systemPrompt, knowledgeBase);
    }
}

