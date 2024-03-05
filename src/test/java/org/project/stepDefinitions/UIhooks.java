package org.project.stepDefinitions;

import io.cucumber.java.After;
import org.openqa.selenium.WebDriver;
import org.project.api_testing.WireMockUtil;
import org.project.configuration.DriverFactory;

public class UIhooks {
    private static WebDriver driver;

//    @Before("@CustomHook")
//    public static void beforeScenario() {
////        driver = DriverFactory.getDriver("chrome");
//    }

    @After("@QuitDriver")
    public static void afterScenario() {
        DriverFactory.quitDriver();
    }

    @After("@StopWireMock")
    public static void closeWiremock() {
        WireMockUtil.stopServer();
    }

}