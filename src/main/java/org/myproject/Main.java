package org.myproject;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v95.log.Log;


public class Main {
    private static ChromeDriver driver;
    private static DevTools chromeDevTools;

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/webdriver/chromedriver_mac64/chromedriver");
        System.setProperty("webdriver.chrome.verboseLogging", "true");

        driver = new ChromeDriver();
        chromeDevTools = driver.getDevTools();
        chromeDevTools.createSession();

        chromeDevTools.send(Log.enable());
        chromeDevTools.addListener(Log.entryAdded(),
                logEntry -> {
                    System.out.println("log: "+logEntry.getText());
                    System.out.println("level: "+logEntry.getLevel());
                });
        driver.get("https://testersplayground.herokuapp.com/console-5d63b2b2-3822-4a01-8197-acd8aa7e1343.php");
    }
}