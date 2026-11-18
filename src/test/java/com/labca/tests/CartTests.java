package com.labca.tests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.labca.base.BaseTest;
import com.labca.pages.CartPage;
import com.labca.pages.InventoryPage;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("cart")
@DisplayName("Shopping cart")
class CartTests extends BaseTest {

    private static final String BACKPACK = "Sauce Labs Backpack";
    private static final String BIKE_LIGHT = "Sauce Labs Bike Light";
    private static final String ONESIE = "Sauce Labs Onesie";

    private InventoryPage inventory;

    @BeforeEach
    void login() {
        inventory = loginAsStandardUser();
    }

    @Test
    @Tag("smoke")
    @DisplayName("TC13 - Adding an item updates badge and button state")
    void addSingleItem() {
        inventory.addToCart(BACKPACK);

        assertAll(
                () -> assertEquals(1, inventory.header.cartCount()),
                () -> assertTrue(inventory.isRemoveButtonShown(BACKPACK)));
    }

    @Test
    @DisplayName("TC14 - Multiple items appear in the cart")
    void addMultipleItems() {
        inventory.addToCart(BACKPACK).addToCart(BIKE_LIGHT).addToCart(ONESIE);
        assertEquals(3, inventory.header.cartCount());

        CartPage cart = inventory.header.openCart();
        assertAll(
                () -> assertEquals("Your Cart", cart.pageTitle()),
                () -> assertEquals(List.of(BACKPACK, BIKE_LIGHT, ONESIE), cart.itemNames()));
    }

    @Test
    @DisplayName("TC15 - Removing an item from the products page clears the badge")
    void removeFromInventory() {
        inventory.addToCart(BACKPACK).removeFromCart(BACKPACK);

        assertAll(
                () -> assertEquals(0, inventory.header.cartCount()),
                () -> assertFalse(inventory.isRemoveButtonShown(BACKPACK)));
    }

    @Test
    @DisplayName("TC16 - Removing an item from the cart page")
    void removeFromCartPage() {
        inventory.addToCart(BACKPACK).addToCart(BIKE_LIGHT);
        CartPage cart = inventory.header.openCart().remove(BACKPACK);

        assertAll(
                () -> assertEquals(List.of(BIKE_LIGHT), cart.itemNames()),
                () -> assertEquals(1, cart.header.cartCount()));
    }

    @Test
    @DisplayName("TC17 - Cart contents persist after Continue Shopping")
    void cartPersistsAcrossNavigation() {
        inventory.addToCart(ONESIE);
        InventoryPage back = inventory.header.openCart().continueShopping();

        assertAll(
                () -> assertEquals(1, back.header.cartCount()),
                () -> assertTrue(back.isRemoveButtonShown(ONESIE)));
    }

    @Test
    @DisplayName("TC18 - Reset App State empties the cart")
    void resetAppStateClearsCart() {
        inventory.addToCart(BACKPACK).addToCart(BIKE_LIGHT);
        inventory.header.resetAppState();

        assertEquals(0, inventory.header.cartCount());
        assertTrue(inventory.header.openCart().itemNames().isEmpty());
    }
}
