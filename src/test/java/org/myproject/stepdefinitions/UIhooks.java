package org.myproject.stepdefinitions;

import io.cucumber.java.After;
import org.openqa.selenium.WebDriver;
import org.project.configuration.DriverFactory;

public class UIhooks {
    private static WebDriver driver;

//    @Before("@CustomHook")
//    public static void beforeScenario() {
////        driver = DriverFactory.getDriver("chrome");
//    }

    @After("@CustomHook")
    public static void afterScenario() {
        DriverFactory.quitDriver();
    }


}