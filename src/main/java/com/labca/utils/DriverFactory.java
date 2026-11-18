package com.labca.utils;

import java.time.Duration;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/** Creates WebDriver instances. Selenium Manager resolves the driver binaries automatically. */
public final class DriverFactory {

    private DriverFactory() { }

    public static WebDriver create() {
        String browser = Config.get("browser", "chrome").toLowerCase();
        boolean headless = Boolean.parseBoolean(Config.get("headless", "true"));

        WebDriver driver = switch (browser) {
            case "firefox" -> {
                FirefoxOptions o = new FirefoxOptions();
                if (headless) o.addArguments("-headless");
                yield new FirefoxDriver(o);
            }
            case "edge" -> {
                EdgeOptions o = new EdgeOptions();
                if (headless) o.addArguments("--headless=new");
                o.addArguments("--window-size=1920,1080");
                yield new EdgeDriver(o);
            }
            default -> {
                ChromeOptions o = new ChromeOptions();
                if (headless) o.addArguments("--headless=new");
                o.addArguments("--window-size=1920,1080", "--disable-notifications", "--no-sandbox",
                        "--disable-dev-shm-usage", "--incognito");
                // Stop the password-manager breach popup from blocking the page
                o.setExperimentalOption("prefs", Map.of(
                        "credentials_enable_service", false,
                        "profile.password_manager_enabled", false,
                        "profile.password_manager_leak_detection", false));
                yield new ChromeDriver(o);
            }
        };
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Config.getInt("implicit.wait.seconds")));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        if (!headless) driver.manage().window().maximize();
        return driver;
    }
}
