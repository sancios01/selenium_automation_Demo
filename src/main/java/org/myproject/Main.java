package org.myproject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class Main {
    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "src/main/resources/webdriver/chromedriver_mac64/chromedriver");
        WebDriver driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.get("https://google.com");

        driver.quit();

        System.out.println("Hello world!");
    }
}