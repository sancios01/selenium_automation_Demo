package org.project.stepDefinitions.api;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.project.api_testing.WireMockUtil;
import org.project.utils.ApiConfig;
import org.project.utils.ApiUtils;
import org.project.utils.ScenarioContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ApiStepDefinitions {

    private final ApiUtils apiUtils;
    private final ScenarioContext scenarioContext;

    public ApiStepDefinitions(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
        this.apiUtils = new ApiUtils(ApiConfig.getInstance());
    }

    @Given("WireMock server is running with mappings from {string}")
    public void wireMockServerIsRunning(String jsonFilePath) {
        WireMockUtil.startServer(8080);
        try {
            WireMockUtil.loadMappingsFromJson(jsonFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Given("I set the base URI to {string}")
    public void setBaseUri(String uri) {
        apiUtils.setBaseUri(uri);
    }

    @When("I send a {string} request to the endpoint {string}")
    public void sendHttpRequest(String method, String endpoint) {
        Response response;
        switch (method.toUpperCase()) {
            case "GET":
                response = apiUtils.sendGetRequest(endpoint);
                break;
            case "POST":
                response = apiUtils.sendPostRequest(endpoint);
                break;
            // Add other HTTP methods as needed
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
        scenarioContext.setContext("response", response); // Set the response in the scenario context
    }


    @When("I send a {string} request to the endpoint {string} with query parameter {string} as {string}")
    public void sendHttpRequestWithQueryParam(String method, String endpoint, String param, String value) {
        String requestUrl = endpoint + "?" + param + "=" + value;
        sendHttpRequest(method, requestUrl);
    }

    @When("I send a {string} request to the endpoint {string} with headers and body:")
    public void sendHttpRequestWithHeadersAndBody(String method, String endpoint, DataTable dataTable) {
        Map<String, String> headers = new HashMap<>();
        Map<String, String> body = new HashMap<>();
        boolean bodyStarted = false;

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String key = row.get("Header Name");
            String value = row.get("Header Value");

            if (key != null && value != null && !key.isEmpty()) {
                if (!bodyStarted) {
                    headers.put(key, value);
                } else {
                    body.put(key, value);
                }
            } else {
                // If we encounter an empty cell, assume body parameters start from here
                bodyStarted = true;
            }
        }

        // Send request based on method
        switch (method.toUpperCase()) {
            case "GET":
                apiUtils.getPostRequestWithHeadersAndBody(endpoint, headers, body);
                break;
            case "POST":
                apiUtils.sendPostRequestWithHeadersAndBody(endpoint, headers, body);
                break;
            // Add other HTTP methods as needed
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
    }





    @When("I send a POST request to {string} with headers:")
    public void sendPostRequestWithHeaders(String endpoint, DataTable headersTable) {
        Map<String, String> headers = headersTable.asMap(String.class, String.class);
        Response response = apiUtils.sendPostRequestWithHeaders(endpoint, headers);
        scenarioContext.setContext("response", response); // Set the response in the scenario context
    }

    @Then("the response status code should be {int}")
    public void verifyResponseStatusCode(int expectedStatusCode) {
        Response response = apiUtils.getLastResponse();
        assertEquals(expectedStatusCode, response.getStatusCode());
    }

    @Then("the response body should contain {string}")
    public void verifyResponseBody(String expectedContent) {
        Response response = apiUtils.getLastResponse();
        assertTrue(response.getBody().asString().contains(expectedContent));
    }

    @Then("the response headers should contain:")
    public void verifyResponseHeaders(DataTable expectedHeadersTable) {
        Map<String, String> expectedHeaders = expectedHeadersTable.asMap(String.class, String.class);
        Map<String, String> actualHeaders = apiUtils.getResponseHeaders();
        for (Map.Entry<String, String> entry : expectedHeaders.entrySet()) {
            assertTrue(actualHeaders.containsKey(entry.getKey()));
            assertEquals(entry.getValue(), actualHeaders.get(entry.getKey()));
        }
    }

    @Then("the response should have header {string} with value {string}")
    public void verifyResponseHeader(String headerName, String expectedValue) {
        Response response = apiUtils.getLastResponse();
        assertEquals(expectedValue, response.getHeader(headerName));
    }

//    @Then("the request body is:")
//    public void verifyRequestBody(DataTable table) {
//        // Convert the DataTable to a Map
//        Map<String, String> requestBodyMap = table.asMap(String.class, String.class);
//
//        // Convert the Map to a JSON object
//        JSONObject expectedJson = new JSONObject(requestBodyMap);
//
//        // Retrieve the actual JSON response from the scenario context
//        Object responseObject = scenarioContext.getContext("response");
//        if (responseObject instanceof Response) {
//            Response response = (Response) responseObject;
//
//            // Parse the actual JSON response from the API
//            JSONObject actualJson = new JSONObject(response.getBody().asString());
//
//            // Compare expected and actual JSON objects
//            assertEquals(expectedJson.toString(), actualJson.toString());
//        } else {
//            throw new IllegalStateException("Response object is not an instance of Response class");
//        }
//    }


    @Then("the response body {string} field should be {string}")
    public void verifyResponseBodyField(String field, String value) {
        Response response = apiUtils.getLastResponse();
        response.then().body(field, equalTo(value));
    }

    @Then("the response should contain pet details with {string} field as {string}")
    public void verifyPetDetailsByStatus(String field, String value) {
        Response response = apiUtils.getLastResponse();
        response.then().body(field, hasItem(value));
    }
}
