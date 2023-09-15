package org.myproject.configuration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v95.log.Log;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.GeckoDriverService;

public class DriverFactory {
    private static DevTools chromeDevTools;
    private static WebDriver driver  ;

    public static WebDriver getDriver(String browser) {
        switch (browser) {
            case "chrome":
                return driver = createChromeDriver();
            case "chrome-V2":
                return driver = setupDevToolsChrome();
            case "firefox":
                return driver = createFirefoxDriver();
            default:
                throw new IllegalArgumentException("Invalid browser name: " + browser);
        }
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
        var service = new GeckoDriverService.Builder()
                .withLogOutput(System.out)
                .build();

        return new FirefoxDriver(service);
    }

    private static WebDriver setupDevToolsChrome() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/webdriver/chromedriver_mac64/chromedriver");
        var service = new ChromeDriverService.Builder()
                .build();
        var driver = new ChromeDriver(service);

        chromeDevTools = driver.getDevTools();
        chromeDevTools.createSession();
        chromeDevTools.send(Log.enable());
        chromeDevTools.addListener(Log.entryAdded(),
                logEntry -> {
                    System.out.println("Log: "+logEntry.getText());
                    System.out.println("Level: "+logEntry.getLevel());
                });
        return driver;
    }
    public static void quitDriver() {
        if (driver != null) {
            driver.close();
            driver.quit(); // Use quit() to close all windows and tabs
            driver = null;
        }
    }

}
