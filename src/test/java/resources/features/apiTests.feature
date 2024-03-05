@StopWireMock
Feature: WireMock Testing

  Scenario: Mocking a GET request
    Given WireMock server is running with mappings from "src/test/java/resources/stubs/example.json"
    When I send a "GET" request to the endpoint "/api/resource"
    Then the response status code should be 200
    And the response body should contain "Hello, World!"

  Scenario: Mocking a POST request with headers and body
    Given WireMock server is running with mappings from "src/test/java/resources/stubs/examplePost.json"
    When I send a "POST" request to the endpoint "/api/resource" with headers and body:
      | Header Name    | Header Value       |
      | Content-Type   | application/json   |
      | Authorization  | Bearer             |
      | Custom-Header  | CustomValue        |
      # Empty row to indicate body parameters start here
      |                |                    |
      | key            | value              |
    Then the response status code should be 200
    And the response body should contain "Response body"
    And the response should have header "Custom-Response-Header" with value "ResponseCustomValue"

#  Scenario: Get Pet by ID
#    Given I set the base URI to "https://petstore.swagger.io/v2"
#    When I send a GET request to the endpoint "/pet/{petId}" with ID 1
#    Then the response status code should be 200
#    And the response body "name" field should be "doggie"
#    And the response body "status" field should be "available"
#
#  Scenario: Search Pets by Status
#    Given I set the base URI to "https://petstore.swagger.io/v2"
#    When I send a GET request to the endpoint "/pet/findByStatus" with query parameter "status" as "available"
#    Then the response status code should be 200
#    And the response should contain pet details with "status" field as "available"
