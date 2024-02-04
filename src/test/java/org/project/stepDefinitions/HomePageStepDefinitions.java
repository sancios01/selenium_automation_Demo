package org.project.stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.project.configuration.CredentialLoader;
import org.project.pages.RegistrationPage;

import java.util.Map;

public class HomePageStepDefinitions {
    private RegistrationPage registrationPage;

    private static final Logger LOGGER = LogManager.getLogger(HomePageStepDefinitions.class);
    private final CredentialLoader credentialLoader;

    public HomePageStepDefinitions() {
        this.credentialLoader = new CredentialLoader();
        this.registrationPage = new RegistrationPage();
    }

    @Given("I am on the Home page")
    public void navigateToRegistrationPage() {
        registrationPage.navigateToHomePage();
        LOGGER.info("Navigating to the registration page");
    }

    @When("I enter credential for the user {string}")
    public void i_have_valid_credentials_for_user(String userName) {
        // Use CredentialLoader to get user credentials by name
        Map<String, String> validUser = credentialLoader.getUserByName(userName);

        // Use the credentials as needed (e.g., store in instance variables)
        // For demonstration purposes, just printing them
        String username = validUser.get("username");
        String password = validUser.get("password");

        registrationPage.enterEmail(username);
        registrationPage.enterPassword(password);

    }
}