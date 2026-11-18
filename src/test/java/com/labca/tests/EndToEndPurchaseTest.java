package com.labca.tests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.labca.base.BaseTest;
import com.labca.pages.CartPage;
import com.labca.pages.CheckoutCompletePage;
import com.labca.pages.CheckoutOverviewPage;
import com.labca.pages.InventoryPage;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** Full user journey: login -> browse -> cart -> checkout -> confirmation -> logout. */
@Tag("e2e")
@Tag("smoke")
@DisplayName("End-to-end purchase journey")
class EndToEndPurchaseTest extends BaseTest {

    private static final double TAX_RATE = 0.08;
    private static final double DELTA = 0.01;

    @Test
    @DisplayName("TC21 - Customer buys two products and completes the order")
    void completePurchase() {
        // 1. Login
        InventoryPage inventory = loginAsStandardUser();

        // 2. Browse and add products
        inventory.sortBy("Price (low to high)")
                .addToCart("Sauce Labs Onesie")
                .addToCart("Sauce Labs Fleece Jacket");
        assertEquals(2, inventory.header.cartCount());

        // 3. Review cart
        CartPage cart = inventory.header.openCart();
        assertEquals(List.of("Sauce Labs Onesie", "Sauce Labs Fleece Jacket"), cart.itemNames());

        // 4. Enter customer information
        CheckoutOverviewPage overview = cart.checkout()
                .fill("John", "Smith", "560001")
                .continueToOverview();

        // 5. Verify order summary and totals
        double itemSum = overview.sumOfItemPrices();
        double expectedTax = Math.round(itemSum * TAX_RATE * 100.0) / 100.0;
        assertAll("order totals",
                () -> assertEquals(List.of("Sauce Labs Onesie", "Sauce Labs Fleece Jacket"), overview.itemNames()),
                () -> assertEquals(itemSum, overview.subtotal(), DELTA),
                () -> assertEquals(expectedTax, overview.tax(), DELTA),
                () -> assertEquals(overview.subtotal() + overview.tax(), overview.total(), DELTA));

        // 6. Place order
        CheckoutCompletePage complete = overview.finish();
        assertAll("confirmation",
                () -> assertEquals("Thank you for your order!", complete.confirmationHeader()),
                () -> assertTrue(complete.currentUrl().endsWith("/checkout-complete.html")),
                () -> assertEquals(0, complete.header.cartCount()));

        // 7. Return home and log out
        InventoryPage home = complete.backHome();
        assertEquals("Products", home.pageTitle());
        assertTrue(home.header.logout().isLoaded());
    }
}
