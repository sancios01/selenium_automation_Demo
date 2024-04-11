package org.project.stepDefinitions.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.project.api_testing.WireMockUtil;
import org.project.utils.ApiConfig;
import org.project.utils.ApiUtils;
import org.project.utils.ScenarioContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.*;

public class ApiStepDefinitions {

    private final ApiUtils apiUtils;
    private final ScenarioContext scenarioContext;

    private static final Logger LOGGER = LogManager.getLogger(ApiStepDefinitions.class);

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


    @When("V1 I send a {string} request to the endpoint {string}")
    public void sendv1HttpRequest(String method, String endpoint, DataTable dataTable) {
        // Iterăm prin fiecare rând al tabelului de date
        dataTable.asMaps(String.class, String.class).forEach(row -> {
            Map<String, String> body = new HashMap<>();

            // Iterăm prin fiecare pereche cheie-valoare din rând și adăugăm la corpul cererii
            row.forEach((key, value) -> body.put(key, value));

            // Trimitem cererea bazată pe metoda HTTP specificată
            Response response;
            switch (method.toUpperCase()) {
                case "GET":
                    response = apiUtils.sendGetRequest(endpoint + body.get("username"));
                    break;
                case "POST":
                    response = apiUtils.sendPostRequestWithBody(endpoint, body);
                    break;
                case "PUT":
                    response = apiUtils.sendPutRequestWithBody(endpoint + body.get("username"), body);
                    break;
                default:
                    throw new IllegalArgumentException("Metoda HTTP nesuportată: " + method);
            }

            // Logăm mesajul cererii și răspunsul
            LOGGER.info("Cerere trimisă: {} {} cu corpul {}", method, endpoint, body);
            LOGGER.info("Corpul răspunsului: {}", prettyPrintUsingGson(response.getBody().asString()));

            // Adăugăm răspunsul în contextul scenariului folosind cheia unică din rând
            scenarioContext.setContext(row.get("id"), response);
        });
    }

    @Then("the response body for {string} should contain:")
    public void verifyResponseBodyforid(String requestId, DataTable dataTable) {
        // Convert DataTable to a single Map
        Map<String, String> expectedData = dataTable.asMap(String.class, String.class);

        // Retrieve the actual JSON response body
        Response response = (Response) scenarioContext.getContext(requestId);
        String responseBody = response.getBody().asString();

        // Parse the response body using JsonPath
        JsonPath jsonPath = new JsonPath(responseBody);

        // Iterate over each entry in the expected data Map
        expectedData.forEach((key, expectedValue) -> {
            // Use JsonPath to extract the actual value
            String actualValue = jsonPath.getString(key);

            // Perform comparison between expected and actual values
            assertThat(expectedValue.equals(actualValue)).isTrue();
            LOGGER.info("expectedValue for {}:{} actualValue {}",key,expectedValue,actualValue);
        });
    }


    @Given("I have the orderId {int} and retrieve the order details")
    public void setAndRetrieveOrderDetails(int orderId) {
        LOGGER.info("Sending GET request to retrieve order details for orderId: {}", orderId);

        Response response = apiUtils.sendGetRequest("/store/order/" + orderId);
        scenarioContext.setContext("orderId", orderId);
        scenarioContext.setContext("response", response);

        LOGGER.info("Received response: {}", response.getBody().asString());
    }

    @When("I send a {string} request to the endpoint {string} with headers and body:")
    public void sendHttpRequestWithHeadersAndBody(String method, String endpoint, DataTable dataTable) {
        Response response;
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
                response = apiUtils.getPostRequestWithHeadersAndBody(endpoint, headers, body);
                break;
            case "POST":
                response = apiUtils.sendPostRequestWithHeadersAndBody(endpoint, headers, body);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        // Log request and response details
        LOGGER.info("Sending {} request to {} with headers {} and body {}", method, endpoint, headers, body);
        LOGGER.info("Received response: {}", response.getBody().asString());

        // Set the response in the scenario context if needed
        scenarioContext.setContext("response", response);
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

    @Then("the response status code for id {string} should be {int}")
    public void verifyResponseStatusCode(String requestId, int expectedStatusCode) {
        // Retrieve the response based on the unique key (in this case, the "id")
        Response response = (Response) scenarioContext.getContext(requestId);

        // Verify the status code of the retrieved response
        assertEquals(expectedStatusCode, response.getStatusCode());
    }

    @Then("the response body should contain {string}")
    public void verifyResponseBody(String expectedContent) {
        Response response = apiUtils.getLastResponse();
        assertTrue(response.getBody().asString().contains(expectedContent));
    }

    @Then("the response body should contain:")
    public void verifyResponseBody(DataTable dataTable) {
        // Convert DataTable to a single Map
        Map<String, String> expectedData = dataTable.asMap(String.class, String.class);

        // Retrieve the actual JSON response body
        Response response = (Response) scenarioContext.getContext("response");
        String responseBody = response.getBody().asString();

        // Parse the response body using JsonPath
        JsonPath jsonPath = new JsonPath(responseBody);

        // Iterate over each entry in the expected data Map
        expectedData.forEach((key, expectedValue) -> {
            // Use JsonPath to extract the actual value
            String actualValue = jsonPath.getString(key);

            // Perform comparison between expected and actual values
            assertThat(expectedValue.equals(actualValue)).isTrue();
        });
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

    public String prettyPrintUsingGson(String uglyJson) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = JsonParser.parseString(uglyJson);
        String prettyJsonString = gson.toJson(jsonElement);
        return prettyJsonString;
    }
}
