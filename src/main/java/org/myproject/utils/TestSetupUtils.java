package org.myproject.utils;

import org.myproject.configuration.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;

public class TestSetupUtils {
    private static WebDriver driver;

    @Before("@CustomHook")
    public static void beforeScenario() {
        driver = DriverFactory.getDriver("chrome");
    }

    @After("@CustomHook")
    public static void afterScenario() {
        if (driver != null) {
            driver.quit();
        }
    }

}