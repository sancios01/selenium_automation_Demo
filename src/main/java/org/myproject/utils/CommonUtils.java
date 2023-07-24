package org.myproject.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.myproject.configuration.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CommonUtils {
    private static final Logger LOGGER = LogManager.getLogger(DriverFactory.class);
    private static WebDriver driver;

    public static WebDriver getDriver() {
        if (driver == null) {
            String browser = "chrome"; // You can set browser dynamically or from configuration if needed.

            if (browser.equalsIgnoreCase("chrome")) {
                System.setProperty("webdriver.chrome.driver", "src/main/resources/webdriver/chromedriver_mac64/chromedriver");
//                WebDriver options = new ChromeDriver();
//                WebDriverManager.chromedriver().setup();

                ChromeOptions options = new ChromeOptions();
                // Add any desired Chrome options if needed
                driver = new ChromeDriver(options);
                LOGGER.info("ChromeDriver instance created.");
            } else if (browser.equalsIgnoreCase("firefox")) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                LOGGER.info("FirefoxDriver instance created.");
            }

            // Other driver setups if needed for different browsers

            // Set the driver logs level
            System.setProperty("webdriver.chrome.logfile", "logs/chromedriver.log");
            System.setProperty("webdriver.chrome.verboseLogging", "true");
        }
        return driver;
    }

}
