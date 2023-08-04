Feature: API Testing for Swagger Petstore

  Scenario: Get Pet by ID
    Given I set the base URI to "https://petstore.swagger.io/v2"
    When I send a GET request to the endpoint "/pet/{petId}" with ID 1
    Then the response status code should be 200
    And the response body "name" field should be "doggie"
    And the response body "status" field should be "available"

  Scenario: Search Pets by Status
    Given I set the base URI to "https://petstore.swagger.io/v2"
    When I send a GET request to the endpoint "/pet/findByStatus" with query parameter "status" as "available"
    Then the response status code should be 200
    And the response should contain pet details with "status" field as "available"
