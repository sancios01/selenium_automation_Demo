package org.project.stepDefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.project.pages.RegistrationPage;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RegistrationStepDefinitions {
    private RegistrationPage registrationPage;

    private static final Logger LOGGER = LogManager.getLogger(RegistrationStepDefinitions.class);

    public RegistrationStepDefinitions() {
        this.registrationPage = new RegistrationPage();
    }

    @Given("I am on the Google registration page")
    public void navigateToRegistrationPage() {
        registrationPage.navigateToRegistrationPage();
        LOGGER.info("Navigating to the registration page");
    }

    @When("I enter my first name as {string}")
    public void enterFirstName(String firstName) {
        registrationPage.enterFirstName(firstName);
    }

    @When("I enter my last name as {string}")
    public void enterLastName(String lastName) {
        registrationPage.enterLastName(lastName);
    }

    @When("I enter my email as {string}")
    public void enterEmail(String email) {
        registrationPage.enterEmail(email);
    }

    @When("I enter my password as {string}")
    public void enterPassword(String password) {
        registrationPage.enterPassword(password);
    }

    @When("I enter my Confirm Passwd as {string}")
    public void enterConfirmPasswd(String password) {
        registrationPage.enterConfirmPasswd(password);
    }

    @When("I click on the next button")
    public void clickNextButton() {
        registrationPage.clickNextButton();
    }

    @Then("I should see the next step of the registration process")
    public void verifyNextStep() {
        assertTrue("Next step of registration not displayed", registrationPage.isNextStepDisplayed());
    }

    @Then("I should see an error message for the missing email")
    public void verifyMissingEmailErrorMessage() {
        assertTrue(registrationPage.isMissingEmailErrorMessageDisplayed());
    }

    @Then("I should see an error message for the weak password")
    public void verifyWeakPasswordErrorMessage() {
        assertTrue(registrationPage.isWeakPasswordErrorMessageDisplayed());
    }

    @Then("I get next error:")
    public void errorMs(DataTable testData){
        Map<String, String> data = testData.asMap(String.class, String.class);
        data.get("error message");

        assertEquals(registrationPage.getWebLogs(), data.get("error message"));
    }
}