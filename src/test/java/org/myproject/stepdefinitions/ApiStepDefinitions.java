package org.myproject.stepdefinitions;

import org.project.utils.ApiConfig;
import org.project.utils.ApiUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

public class ApiStepDefinitions {

    private ApiUtils apiUtils;

    @Given("I set the base URI to {string}")
    public void setBaseUri(String uri) {
        ApiConfig apiConfig = ApiConfig.getInstance();
        apiUtils = new ApiUtils(apiConfig);
        apiUtils.setBaseUri(uri);
    }

    @When("I send a GET request to the endpoint {string} with ID {int}")
    public void sendGetRequest(String endpoint, int petId) {
        apiUtils.sendGetRequest(endpoint.replace("{petId}", String.valueOf(petId)));
    }

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int statusCode) {
        Response response = apiUtils.getLastResponse();
        response.then().statusCode(statusCode);
    }

    @Then("the response body {string} field should be {string}")
    public void verifyResponseBodyField(String field, String value) {
        Response response = apiUtils.getLastResponse();
        response.then().body(field, equalTo(value));
    }

    @When("I send a GET request to the endpoint {string} with query parameter {string} as {string}")
    public void sendGetRequestWithQueryParam(String endpoint, String param, String value) {
        apiUtils.sendGetRequest(endpoint + "?" + param + "=" + value);
    }

    @Then("the response should contain pet details with {string} field as {string}")
    public void verifyPetDetailsByStatus(String field, String value) {
        Response response = apiUtils.getLastResponse();
        response.then().body(field, hasItem(value));
    }
}
