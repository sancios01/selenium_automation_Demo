Feature: Google Registration

  Scenario: Successful registration with valid information
    Given I am on the Google registration page
    When I enter my first name as "John"
    And I enter my last name as "Doe"
    And I enter my email as "johne90@mail.ru"
    And I enter my password as "password_+123"
#    And I enter my Confirm Passwd as "password_+123"
    And I click on the next button


#    Then I should see the next step of the registration process
#
#  Scenario: Registration with missing email
#    Given I am on the Google registration page
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