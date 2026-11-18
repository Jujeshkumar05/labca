package com.labca.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR = By.cssSelector("[data-test='error']");
    private static final By ERROR_CLOSE = By.cssSelector("[data-test='error-button']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage enterUsername(String username) {
        type(USERNAME, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(PASSWORD, password);
        return this;
    }

    public void submit() {
        click(LOGIN_BUTTON);
    }

    public InventoryPage loginAs(String username, String password) {
        enterUsername(username).enterPassword(password).submit();
        return new InventoryPage(driver).waitUntilLoaded();
    }

    public LoginPage loginExpectingError(String username, String password) {
        enterUsername(username).enterPassword(password).submit();
        visible(ERROR);
        return this;
    }

    public String errorMessage() {
        return text(ERROR);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(ERROR);
    }

    public LoginPage dismissError() {
        click(ERROR_CLOSE);
        return this;
    }

    public boolean isLoaded() {
        return isDisplayed(LOGIN_BUTTON);
    }
}
