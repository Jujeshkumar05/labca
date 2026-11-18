package com.labca.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailsPage extends BasePage {
    private static final By NAME = By.cssSelector("[data-test='inventory-item-name']");
    private static final By PRICE = By.cssSelector("[data-test='inventory-item-price']");
    private static final By DESCRIPTION = By.cssSelector("[data-test='inventory-item-desc']");
    private static final By ADD = By.cssSelector("[data-test='add-to-cart']");
    private static final By BACK = By.cssSelector("[data-test='back-to-products']");

    public final HeaderComponent header;

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
        this.header = new HeaderComponent(driver);
        visible(BACK);
    }

    public String name() { return text(NAME); }

    public String price() { return text(PRICE); }

    public String description() { return text(DESCRIPTION); }

    public ProductDetailsPage addToCart() {
        click(ADD);
        return this;
    }

    public InventoryPage backToProducts() {
        click(BACK);
        return new InventoryPage(driver).waitUntilLoaded();
    }
}
