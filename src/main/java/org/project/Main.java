package org.project;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.log.Log;

public class Main {
    private static ChromeDriver driver; // Change to ChromeDriver
    private static DevTools chromeDevTools;

    public static void main(String[] args) {
        // Set up ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(); // Change to ChromeDriver

        // Initialize Chrome DevTools
        chromeDevTools = driver.getDevTools();
        chromeDevTools.createSession();

        // Enable logging in Chrome DevTools
        chromeDevTools.send(Log.enable());

        // Listen for log entry added events
        chromeDevTools.addListener(Log.entryAdded(),
                logEntry -> {
                    System.out.println("Log: " + logEntry.getText());
                    System.out.println("Level: " + logEntry.getLevel());
                });

        // Navigate to a web page
        driver.get("https://testersplayground.herokuapp.com/console-5d63b2b2-3822-4a01-8197-acd8aa7e1343.php");

        // Your Selenium code here

        // Close the WebDriver instance
        driver.quit();
    }
}
