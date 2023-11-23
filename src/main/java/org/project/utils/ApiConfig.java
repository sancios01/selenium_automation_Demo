package org.project.utils;

public class ApiConfig {
    private String apiKey;
    private static final String DEFAULT_API_KEY = "DEFAULT_API_KEY";

    private static final ApiConfig instance = new ApiConfig();

    // Private constructor to prevent instantiation from outside the class
    private ApiConfig() {}

    // Getter method for API key
    public String getApiKey() {
        return apiKey;
    }

    // Setter method for API key
    public void setApiKey(String key) {
        apiKey = key;
    }

    // Static method to get the singleton instance
    public static ApiConfig getInstance() {
        return instance;
    }

    // Return the default API key
    public String getDefaultApiKey() {
        return DEFAULT_API_KEY;
    }
}
