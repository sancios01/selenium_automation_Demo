package org.myproject.configuration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v95.log.Log;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverService;
import org.openqa.selenium.firefox.GeckoDriverService;

public class DriverFactory {
    private static WebDriver driver;
    private static DevTools chromeDevTools;

    public static WebDriver getDriver() {
        if (driver == null) {
            String browser = System.getProperty("browser", "chrome");
            switch (browser) {
                case "chrome":
                    driver = createChromeDriver();
                    break;
                case "firefox":
                    driver = createFirefoxDriver();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid browser name: " + browser);
            }
//            setupDevTools(driver);
        }
        return driver;
    }

    private static WebDriver createChromeDriver() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/webdriver/chromedriver_mac64/chromedriver");
        System.setProperty("webdriver.chrome.verboseLogging", "true");

        ChromeDriverService service = new ChromeDriverService.Builder()
                .withLogOutput(System.out)
                .build();


        return new ChromeDriver(service);
    }

    private static WebDriver createFirefoxDriver() {
        FirefoxDriverService service = new GeckoDriverService.Builder()
                .withLogOutput(System.out)
                .build();
        // Add any desired Firefox options here
        return new FirefoxDriver(service);
    }

    private static void setupDevTools(WebDriver driver) {
        chromeDevTools = ((HasDevTools) driver).getDevTools();
        chromeDevTools.createSession();

        // Enable DevTools Log domain and capture log entries
        chromeDevTools.send(Log.enable());
        chromeDevTools.addListener(Log.entryAdded(),
                logEntry -> {
                    System.out.println("log: "+logEntry.getText());
                    System.out.println("level: "+logEntry.getLevel());
                });
    }
}
