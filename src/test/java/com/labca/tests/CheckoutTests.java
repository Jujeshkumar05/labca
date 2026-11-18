package com.labca.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.labca.base.BaseTest;
import com.labca.pages.CartPage;
import com.labca.pages.CheckoutInfoPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@Tag("checkout")
@DisplayName("Checkout form validation")
class CheckoutTests extends BaseTest {

    private CheckoutInfoPage checkoutInfo;

    @BeforeEach
    void openCheckout() {
        checkoutInfo = loginAsStandardUser()
                .addToCart("Sauce Labs Backpack")
                .header.openCart()
                .checkout();
    }

    @ParameterizedTest(name = "TC19 - first=''{0}'' last=''{1}'' zip=''{2}'' -> {3}")
    @DisplayName("TC19 - Required customer fields are validated")
    @CsvSource(delimiter = '|', emptyValue = "", value = {
            "''   | ''    | ''    | Error: First Name is required",
            "John | ''    | ''    | Error: Last Name is required",
            "John | Smith | ''    | Error: Postal Code is required",
            "''   | Smith | 12345 | Error: First Name is required"
    })
    void requiredFields(String first, String last, String zip, String expectedError) {
        String error = checkoutInfo.fill(first, last, zip).continueExpectingError().errorMessage();
        assertEquals(expectedError, error);
    }

    @Test
    @DisplayName("TC20 - Cancel on checkout returns to cart with items intact")
    void cancelReturnsToCart() {
        CartPage cart = checkoutInfo.cancel();
        assertEquals(1, cart.itemNames().size());
    }
}
