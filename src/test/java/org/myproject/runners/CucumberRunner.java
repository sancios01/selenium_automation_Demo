package org.myproject.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:src/test/resources/features",
        glue = {"org.myproject.stepdefinitions","org.project.utils","org.myproject.runners"},
        tags = "@Regression",
        plugin = {"pretty",
                "html:target/CucumberTestReport.html",
                "json:target/CucumberTestReport.json"},
        monochrome = true

)
public class CucumberRunner {
    // This class is left empty as it is just a runner class
}