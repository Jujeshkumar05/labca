package com.labca.base;

import com.labca.pages.InventoryPage;
import com.labca.pages.LoginPage;
import com.labca.utils.Config;
import com.labca.utils.DriverFactory;
import com.labca.utils.ScreenshotOnFailure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

/**
 * Common setup: a fresh browser per test for full isolation.
 * The browser is closed by {@link ScreenshotOnFailure} after the result is known,
 * so a screenshot can be captured on failure first.
 */
@ExtendWith(ScreenshotOnFailure.class)
public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeEach
    void setUpDriver() {
        driver = DriverFactory.create();
        driver.get(Config.get("base.url"));
    }

    public WebDriver getDriver() {
        return driver;
    }

    protected LoginPage loginPage() {
        return new LoginPage(driver);
    }

    /** Logs in as the standard user and returns the inventory page. */
    protected InventoryPage loginAsStandardUser() {
        return loginPage().loginAs(Config.get("standard.user"), Config.get("password"));
    }
}
