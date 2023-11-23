package org.project.utils;

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
        var giv = given()
                 .header("api_key", "DEFAULT_API_KEY")
                 .when();

        if (apiConfig.getApiKey() != null){
            giv = giv.header("api_key", apiConfig.getApiKey());
        }

        return giv.get(endpoint);
    }

    public Response getLastResponse() {
        return this.lastResponse;
    }
}
