package org.project.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
        var giv = given().log().all()
                .header("api_key", "DEFAULT_API_KEY")
                .when();

        if (apiConfig.getApiKey() != null) {
            giv = giv.header("api_key", apiConfig.getApiKey());
        }

        lastResponse = giv.get(endpoint); // Update lastResponse with the response received
        return lastResponse;
    }

    public Response sendPostRequestWithBody(String endpoint, Map<String, String> body) {
         var giv = given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .post(endpoint);

        lastResponse = giv;
        return lastResponse;
    }

    public Response sendPutRequestWithBody(String endpoint, Map<String, String> body) {
        var giv = given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .put(endpoint);
        lastResponse = giv;
        return lastResponse;
    }

    public Response sendPostRequest(String endpoint) {
        var giv = given()
                .header("api_key", "DEFAULT_API_KEY")
                .when();

        if (apiConfig.getApiKey() != null) {
            giv = giv.header("api_key", apiConfig.getApiKey());
        }

        lastResponse = giv.post(endpoint); // Update lastResponse with the response received
        return lastResponse;
    }

    public Response sendPostRequestWithHeadersAndBody(String endpoint, Map<String, String> headers, Map<String, String> requestBody) {
        var mergedHeaders = new HashMap<>(getDefaultHeaders()); // Copy default headers

        if (headers != null) {
            mergedHeaders.putAll(headers); // Override default headers with provided headers
        }

        var giv = given().log().all()
                .headers(headers)
                .body(requestBody)
                .when();

        if (apiConfig.getApiKey() != null) {
            giv = giv.header("api_key", apiConfig.getApiKey());
        }
        lastResponse = giv.post(endpoint); // Update lastResponse with the response received
        return lastResponse;
    }

    public Response getPostRequestWithHeadersAndBody(String endpoint, Map<String, String> headers, Map<String, String> requestBody) {


        var giv = given()
                .headers(headers)
                .body(requestBody)
                .when();

        if (apiConfig.getApiKey() != null) {
            giv = giv.header("api_key", apiConfig.getApiKey());
        }
        lastResponse = giv.get(endpoint); // Update lastResponse with the response received
        return lastResponse;
    }

    public Response sendGetRequestWithHeaders(String endpoint, Map<String, String> headers) {
        var giv = given()
                .headers(headers)
                .when();

        if (apiConfig.getApiKey() != null) {
            giv = giv.header("api_key", apiConfig.getApiKey());
        }
        lastResponse = giv.get(endpoint); // Update lastResponse with the response received
        return lastResponse;
    }

    public Response sendPostRequestWithHeaders(String endpoint, Map<String, String> headers) {
        var giv = given()
                .headers(headers)
                .when();

        if (apiConfig.getApiKey() != null) {
            giv = giv.header("api_key", apiConfig.getApiKey());
        }
        lastResponse = giv.post(endpoint); // Update lastResponse with the response received
        return lastResponse;
    }

    public static Response getLastResponse() {
        return lastResponse;
    }

    public static Map<String, String> getResponseHeaders() {
        if (lastResponse == null) {
            throw new IllegalStateException("No response received. Please make a request first.");
        }
        return lastResponse.getHeaders().asList().stream()
                .collect(Collectors.toMap(Header::getName, Header::getValue));
    }

    private Map<String, String> getDefaultHeaders() {
        Map<String, String> defaultHeaders = new HashMap<>();
        defaultHeaders.put("Content-Type", "application/json");
        defaultHeaders.put("Authorization", "Bearer token");
        defaultHeaders.put("Accept", "application/json");
        // Add more default headers as needed
        return defaultHeaders;
    }
}
