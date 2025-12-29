package com.project.csvreader;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private static final Properties props = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                props.load(input);
            } else {
                throw new RuntimeException("config.properties not found in classpath");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static String getEnvBaseUrl() {
        String env = get("llm.environment").toLowerCase();
        if ("local".equals(env)) {
            return get("ollama.local.baseUrl");
        } else if ("company".equals(env)) {
            return get("ollama.company.baseUrl");
        } else {
            throw new RuntimeException("Invalid llm.environment: " + env);
        }
    }
    public String getModelUrl() {
        return props.getProperty("model.url");
    }

    public static String getModelName() {
        return get("ollama.modelName");
    }

    public static double getTemperature() {
        return Double.parseDouble(get("temperature"));
    }

    public static int getTimeoutSeconds() {
        return Integer.parseInt(get("timeout.seconds"));
    }
}
