package com.labca.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class InventoryPage extends BasePage {
    private static final By TITLE = By.cssSelector("[data-test='title']");
    private static final By ITEMS = By.cssSelector("[data-test='inventory-item']");
    private static final By ITEM_NAMES = By.cssSelector("[data-test='inventory-item-name']");
    private static final By ITEM_PRICES = By.cssSelector("[data-test='inventory-item-price']");
    private static final By SORT = By.cssSelector("[data-test='product-sort-container']");

    public final HeaderComponent header;

    public InventoryPage(WebDriver driver) {
        super(driver);
        this.header = new HeaderComponent(driver);
    }

    public InventoryPage waitUntilLoaded() {
        visible(TITLE);
        return this;
    }

    public String pageTitle() {
        return text(TITLE);
    }

    public int itemCount() {
        return driver.findElements(ITEMS).size();
    }

    public List<String> itemNames() {
        return driver.findElements(ITEM_NAMES).stream().map(WebElement::getText).toList();
    }

    public List<Double> itemPrices() {
        return driver.findElements(ITEM_PRICES).stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .toList();
    }

    public InventoryPage sortBy(String visibleText) {
        new Select(visible(SORT)).selectByVisibleText(visibleText);
        return this;
    }

    public InventoryPage addToCart(String productName) {
        click(By.cssSelector("[data-test='add-to-cart-" + slug(productName) + "']"));
        return this;
    }

    public InventoryPage removeFromCart(String productName) {
        click(By.cssSelector("[data-test='remove-" + slug(productName) + "']"));
        return this;
    }

    public boolean isRemoveButtonShown(String productName) {
        return isDisplayed(By.cssSelector("[data-test='remove-" + slug(productName) + "']"));
    }

    public ProductDetailsPage openProduct(String productName) {
        click(By.xpath("//div[@data-test='inventory-item-name' and normalize-space()='" + productName + "']"));
        return new ProductDetailsPage(driver);
    }
}
