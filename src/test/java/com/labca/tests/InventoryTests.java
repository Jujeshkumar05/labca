package com.labca.tests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.labca.base.BaseTest;
import com.labca.pages.InventoryPage;
import com.labca.pages.ProductDetailsPage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("inventory")
@DisplayName("Inventory / Products")
class InventoryTests extends BaseTest {

    private InventoryPage inventory;

    @BeforeEach
    void login() {
        inventory = loginAsStandardUser();
    }

    @Test
    @DisplayName("TC07 - Sort products by name A to Z")
    void sortByNameAscending() {
        List<String> names = inventory.sortBy("Name (A to Z)").itemNames();
        List<String> expected = new ArrayList<>(names);
        expected.sort(Comparator.naturalOrder());
        assertEquals(expected, names);
    }

    @Test
    @DisplayName("TC08 - Sort products by name Z to A")
    void sortByNameDescending() {
        List<String> names = inventory.sortBy("Name (Z to A)").itemNames();
        List<String> expected = new ArrayList<>(names);
        expected.sort(Comparator.reverseOrder());
        assertEquals(expected, names);
    }

    @Test
    @DisplayName("TC09 - Sort products by price low to high")
    void sortByPriceAscending() {
        List<Double> prices = inventory.sortBy("Price (low to high)").itemPrices();
        List<Double> expected = new ArrayList<>(prices);
        expected.sort(Comparator.naturalOrder());
        assertEquals(expected, prices);
    }

    @Test
    @DisplayName("TC10 - Sort products by price high to low")
    void sortByPriceDescending() {
        List<Double> prices = inventory.sortBy("Price (high to low)").itemPrices();
        List<Double> expected = new ArrayList<>(prices);
        expected.sort(Comparator.reverseOrder());
        assertEquals(expected, prices);
    }

    @Test
    @DisplayName("TC11 - Product details page matches the listing")
    void productDetails() {
        ProductDetailsPage details = inventory.openProduct("Sauce Labs Backpack");

        assertAll(
                () -> assertEquals("Sauce Labs Backpack", details.name()),
                () -> assertEquals("$29.99", details.price()),
                () -> assertFalse(details.description().isBlank()),
                () -> assertTrue(details.currentUrl().contains("inventory-item.html?id=")));

        assertEquals("Products", details.backToProducts().pageTitle());
    }

    @Test
    @DisplayName("TC12 - Add to cart from product details updates badge")
    void addFromDetailsPage() {
        ProductDetailsPage details = inventory.openProduct("Sauce Labs Bike Light").addToCart();
        assertEquals(1, details.header.cartCount());
    }
}
