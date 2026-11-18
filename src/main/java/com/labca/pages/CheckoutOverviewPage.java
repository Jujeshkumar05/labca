package com.labca.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/** Checkout step two: order summary with totals. */
public class CheckoutOverviewPage extends BasePage {
    private static final By ITEM_NAMES = By.cssSelector("[data-test='inventory-item-name']");
    private static final By ITEM_PRICES = By.cssSelector("[data-test='inventory-item-price']");
    private static final By SUBTOTAL = By.cssSelector("[data-test='subtotal-label']");
    private static final By TAX = By.cssSelector("[data-test='tax-label']");
    private static final By TOTAL = By.cssSelector("[data-test='total-label']");
    private static final By FINISH = By.cssSelector("[data-test='finish']");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
        visible(FINISH);
    }

    public List<String> itemNames() {
        return driver.findElements(ITEM_NAMES).stream().map(WebElement::getText).toList();
    }

    public double sumOfItemPrices() {
        return driver.findElements(ITEM_PRICES).stream()
                .mapToDouble(e -> Double.parseDouble(e.getText().replace("$", "")))
                .sum();
    }

    private double amount(By locator) {
        String t = text(locator);
        return Double.parseDouble(t.substring(t.indexOf('$') + 1));
    }

    public double subtotal() { return amount(SUBTOTAL); }

    public double tax() { return amount(TAX); }

    public double total() { return amount(TOTAL); }

    public CheckoutCompletePage finish() {
        click(FINISH);
        return new CheckoutCompletePage(driver);
    }
}
