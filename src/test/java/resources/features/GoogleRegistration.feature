@Regression
@QuitDriver
Feature: Google Registration

  Scenario: Unsuccessful registration with invalid information
    Given I am on the Google registration page
    And I enter my first name as "John"
    And I enter my last name as "Doe"
    And I enter my email as "johne90@mail.ru"
    And I enter my password as "password_+123"
    When I click on the next button
    Then I get next error:
        | error message | Email address is already in use |

  Scenario: Registration with missing email
    Given I am on the Home page
    And I enter credential for the user "user1@example.com"
    And I click on the submit bottom

#    And I am on the Google registration page
#    When I enter my first name as "John"
#    And I enter my last name as "Doe"
#    And I enter my password as "password123"
#    And I click on the next button
#    Then I should see an error message for the missing email
#
#  Scenario: Registration with weak password
#    Given I am on the Google registration page
#    When I enter my first name as "John"
#    And I enter my last name as "Doe"
#    And I enter my email as "johndoe@example.com"
#    And I enter my password as "weak"
#    And I click on the next button
#    Then I should see an error message for the weak password