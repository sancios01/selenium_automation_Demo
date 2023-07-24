package org.myproject.pages;

import org.myproject.utils.CommonUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegistrationPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By firstNameInput = By.id("firstName");
    private By lastNameInput = By.id("lastName");
    private By emailInput = By.id("email");
    private By passwordInput = By.id("password");
    private By confirmPasswd = By.name("ConfirmPasswd");
    private By nextButton = By.id("submit");

    public RegistrationPage() {
        this.driver = CommonUtils.getDriver();
        this.wait =  new WebDriverWait(driver, Duration.ofSeconds(15)); // Adjust the wait time as needed
    }

    public void navigateToRegistrationPage() {
        driver.get("https://thinking-tester-contact-list.herokuapp.com/addUser");
    }

    public void enterText(By locator, String text) {
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    public void clickButton(By locator) {
        driver.findElement(locator).click();
//        Actions act = new Actions(driver);
//        WebElement ele = driver.findElement(locator);
//        act.doubleClick(ele).perform();
    }

    public void enterFirstName(String firstName) {
        enterText(firstNameInput, firstName);
    }

    public void enterLastName(String lastName) {

        enterText(lastNameInput, lastName);
    }

    public void enterEmail(String email) {

        enterText(emailInput, email);
    }

    public void enterPassword(String password) {

        enterText(passwordInput, password);
    }

    public void enterConfirmPasswd(String password) {
        enterText(confirmPasswd, password);
    }

    public void clickNextButton() {
        clickButton(nextButton);
    }
    public boolean isMissingEmailErrorMessageDisplayed() {
        return driver.findElement(By.xpath("//span[contains(text(), 'missing email')]")).isDisplayed();
    }

    public boolean isWeakPasswordErrorMessageDisplayed() {
        return driver.findElement(By.xpath("//span[contains(text(), 'weak password')]")).isDisplayed();
    }

    public boolean isNextStepDisplayed() {
//        WebElement firstResult = new WebDriverWait(driver, Duration.ofSeconds(15))
//                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Confirmă numărul de telefon']")));
//        return firstResult.isDisplayed();

        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Confirmă numărul de telefon']")));
        return element.isDisplayed();
    }
}