package com.labca.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage {
    private static final By HEADER = By.cssSelector("[data-test='complete-header']");
    private static final By BACK_HOME = By.cssSelector("[data-test='back-to-products']");

    public final HeaderComponent header;

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
        this.header = new HeaderComponent(driver);
        visible(HEADER);
    }

    public String confirmationHeader() { return text(HEADER); }

    public InventoryPage backHome() {
        click(BACK_HOME);
        return new InventoryPage(driver).waitUntilLoaded();
    }
}
