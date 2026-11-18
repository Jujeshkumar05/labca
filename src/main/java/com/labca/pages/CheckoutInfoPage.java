package com.labca.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Checkout step one: customer information form. */
public class CheckoutInfoPage extends BasePage {
    private static final By FIRST_NAME = By.cssSelector("[data-test='firstName']");
    private static final By LAST_NAME = By.cssSelector("[data-test='lastName']");
    private static final By POSTAL_CODE = By.cssSelector("[data-test='postalCode']");
    private static final By CONTINUE = By.cssSelector("[data-test='continue']");
    private static final By CANCEL = By.cssSelector("[data-test='cancel']");
    private static final By ERROR = By.cssSelector("[data-test='error']");

    public CheckoutInfoPage(WebDriver driver) {
        super(driver);
        visible(FIRST_NAME);
    }

    public CheckoutInfoPage fill(String first, String last, String zip) {
        type(FIRST_NAME, first);
        type(LAST_NAME, last);
        type(POSTAL_CODE, zip);
        return this;
    }

    public CheckoutOverviewPage continueToOverview() {
        click(CONTINUE);
        return new CheckoutOverviewPage(driver);
    }

    public CheckoutInfoPage continueExpectingError() {
        click(CONTINUE);
        visible(ERROR);
        return this;
    }

    public String errorMessage() { return text(ERROR); }

    public CartPage cancel() {
        click(CANCEL);
        return new CartPage(driver);
    }
}
