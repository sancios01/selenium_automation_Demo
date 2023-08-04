package org.myproject.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class ApiUtils {
    private static Response lastResponse;
    private final ApiConfig apiConfig;

    // Constructor for dependency injection
    public ApiUtils(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    public void setBaseUri(String baseUri) {
        RestAssured.baseURI = baseUri;
    }

    public Response sendGetRequest(String endpoint) {
        // Check if the API key is set in the ApiConfig instance, otherwise use the default key
        String apiKey = apiConfig.getApiKey() != null ? apiConfig.getApiKey() : getDefaultApiKey();

        lastResponse = given()
                .header("api_key", apiKey)
                .when().get(endpoint);
        return lastResponse;
    }

    public Response getLastResponse() {
        return lastResponse;
    }

    private String getDefaultApiKey() {
        // Return the default API key here
        return "DEFAULT_API_KEY";
    }
}
