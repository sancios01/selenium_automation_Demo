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
      | Header Name   | Header Value     |
      | Content-Type  | application/json |
      | Authorization | Bearer           |
      | Custom-Header | CustomValue      |
      # Empty row to indicate body parameters start here
      |               |                  |
      | key           | value            |
    Then the response status code should be 200
    And the response body should contain:
      | key1                     | value1      |
      | key2.nestedKey           | nestedValue |
      | key3[0]                  | item1       |
      | key3[1]                  | item2       |
      | key3[2]                  | item3       |
      | key4.subArray[0].subKey1 | subValue1   |
      | key4.subArray[1].subKey2 | subValue2   |
    And the response should have header "Custom-Response-Header" with value "ResponseCustomValue"

#  Scenario: Get Pet by ID
#    Given I set the base URI to "https://petstore.swagger.io/v2"
#    When I send a "GET" request to the endpoint "/store/inventory"
#    Then the response status code should be 200
#    And the response body should contain:
#      | sold      | 2      |
##      | string    | 723 |
##      | pending   | "null" |
##      | available | 257    |
##      | Pending   | null   |

  Scenario: Retrieve order details by orderId
    Given I set the base URI to "https://petstore.swagger.io/v2"
    And I send a "POST" request to the endpoint "/store/order" with headers and body:
      | Header Name  | Header Value             |
      | accept       | application/json         |
      | Content-Type | application/json         |
      |              |                          |
      | id           | 1                        |
      | petId        | 0                        |
      | quantity     | 0                        |
      | shipDate     | 2024-03-30T21:18:48.538Z |
      | status       | placed                   |
      | complete     | true                     |
    When I have the orderId 1 and retrieve the order details
    Then the response status code should be 200

  Scenario: Create, Update, and Get User Details
    Given I set the base URI to "https://petstore.swagger.io/v2"
    When V1 I send a "POST" request to the endpoint "/user"
      | id | username | firstName | lastName | email             | password | phone      | userStatus |
      | 1  | user1    | John      | Doe      | user1@example.com | pass123  | 1234567890 | 0          |
      | 2  | user2    | John      | Doe      | user1@example.com | pass123  | 1234567890 | 0          |
    Then the response status code for id "1" should be 200
    And the response status code for id "2" should be 200
    And the response body for "1" should contain:
      | code    | 200     |
      | type    | unknown |
      | message | 1       |
    And the response body for "2" should contain:
      | code    | 200     |
      | type    | unknown |
      | message | 2       |
    # Update User Details
    When V1 I send a "PUT" request to the endpoint '/user/'
      | id | username | firstName | lastName | email                     | password | phone      | userStatus |
      | 3  | user1    | John      | Doe      | updated_user1@example.com | pass123  | 1234567890 | 0          |
    Then the response status code for id "3" should be 200
    And the response body for "3" should contain:
      | code    | 200     |
      | type    | unknown |
      | message | 3       |

    When V1 I send a "GET" request to the endpoint "/user/"
      | id | username |
      | 4  | user1    |
    Then the response status code should be 200
    And the response body for "4" should contain:
      | id         | 1                         |
      | username   | user1                     |
      | firstName  | John                      |
      | lastName   | Doe                       |
      | email      | user1@example.com         |
      | password   | pass123                   |
      | phone      | 1234567890                |
      | userStatus | 0                         |
