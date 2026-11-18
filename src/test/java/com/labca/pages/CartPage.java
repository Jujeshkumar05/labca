package com.labca.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CartPage extends BasePage {
    private static final By TITLE = By.cssSelector("[data-test='title']");
    private static final By ITEM_NAMES = By.cssSelector("[data-test='inventory-item-name']");
    private static final By CHECKOUT = By.cssSelector("[data-test='checkout']");
    private static final By CONTINUE_SHOPPING = By.cssSelector("[data-test='continue-shopping']");

    public final HeaderComponent header;

    public CartPage(WebDriver driver) {
        super(driver);
        this.header = new HeaderComponent(driver);
        visible(CHECKOUT);
    }

    public String pageTitle() { return text(TITLE); }

    public List<String> itemNames() {
        return driver.findElements(ITEM_NAMES).stream().map(WebElement::getText).toList();
    }

    public CartPage remove(String productName) {
        click(By.cssSelector("[data-test='remove-" + slug(productName) + "']"));
        return this;
    }

    public CheckoutInfoPage checkout() {
        click(CHECKOUT);
        return new CheckoutInfoPage(driver);
    }

    public InventoryPage continueShopping() {
        click(CONTINUE_SHOPPING);
        return new InventoryPage(driver).waitUntilLoaded();
    }
}
