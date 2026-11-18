package com.labca.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** Header shared by all logged-in pages: cart badge, cart link and burger menu. */
public class HeaderComponent extends BasePage {
    private static final By CART_LINK = By.cssSelector("[data-test='shopping-cart-link']");
    private static final By CART_BADGE = By.cssSelector("[data-test='shopping-cart-badge']");
    private static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By LOGOUT_LINK = By.id("logout_sidebar_link");
    private static final By RESET_LINK = By.id("reset_sidebar_link");
    private static final By CLOSE_MENU = By.id("react-burger-cross-btn");

    public HeaderComponent(WebDriver driver) {
        super(driver);
    }

    public int cartCount() {
        return driver.findElements(CART_BADGE).isEmpty() ? 0 : Integer.parseInt(text(CART_BADGE));
    }

    public CartPage openCart() {
        click(CART_LINK);
        return new CartPage(driver);
    }

    public LoginPage logout() {
        click(MENU_BUTTON);
        click(LOGOUT_LINK);
        return new LoginPage(driver);
    }

    public void resetAppState() {
        click(MENU_BUTTON);
        click(RESET_LINK);
        click(CLOSE_MENU);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOGOUT_LINK));
    }
}
