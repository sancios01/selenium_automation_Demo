package org.project.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/resources/features",
        glue = {"org.project.stepDefinitions","org.project.utils"},
        tags = "@Regression",
        plugin = {"pretty",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "html:target/CucumberTestReport.html",
                "json:target/CucumberTestReport.json"},
        stepNotifications = true
)
public class CucumberRunner {
    // This class is left empty as it is just a runner class
}