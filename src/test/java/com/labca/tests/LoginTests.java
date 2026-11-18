package com.labca.tests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.labca.base.BaseTest;
import com.labca.pages.InventoryPage;
import com.labca.pages.LoginPage;
import com.labca.utils.Config;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@Tag("login")
@DisplayName("Login")
class LoginTests extends BaseTest {

    @Test
    @Tag("smoke")
    @DisplayName("TC01 - Valid user logs in and lands on Products page")
    void validLogin() {
        InventoryPage inventory = loginAsStandardUser();

        assertAll(
                () -> assertTrue(inventory.currentUrl().endsWith("/inventory.html")),
                () -> assertEquals("Products", inventory.pageTitle()),
                () -> assertEquals(6, inventory.itemCount()));
    }

    @Test
    @DisplayName("TC02 - Locked out user sees a lockout error")
    void lockedOutUser() {
        LoginPage login = loginPage().loginExpectingError(Config.get("locked.user"), Config.get("password"));

        assertEquals("Epic sadface: Sorry, this user has been locked out.", login.errorMessage());
    }

    @ParameterizedTest(name = "TC03 - user=''{0}'' password=''{1}'' -> {2}")
    @DisplayName("TC03 - Invalid or missing credentials show the correct error")
    @CsvSource(delimiter = '|', emptyValue = "", value = {
            "standard_user | wrong_pass   | Epic sadface: Username and password do not match any user in this service",
            "unknown_user  | secret_sauce | Epic sadface: Username and password do not match any user in this service",
            "''            | secret_sauce | Epic sadface: Username is required",
            "standard_user | ''           | Epic sadface: Password is required",
            "''            | ''           | Epic sadface: Username is required"
    })
    void invalidLogin(String user, String pass, String expectedError) {
        LoginPage login = loginPage().loginExpectingError(user, pass);

        assertAll(
                () -> assertEquals(expectedError, login.errorMessage()),
                () -> assertFalse(login.currentUrl().contains("inventory")));
    }

    @Test
    @DisplayName("TC04 - Error message can be dismissed")
    void dismissError() {
        LoginPage login = loginPage().loginExpectingError("bad", "bad").dismissError();

        assertFalse(login.isErrorDisplayed());
    }

    @Test
    @DisplayName("TC05 - Protected page redirects to login when not authenticated")
    void directAccessWithoutLoginIsBlocked() {
        driver.get(Config.get("base.url") + "inventory.html");
        LoginPage login = new LoginPage(driver);

        assertAll(
                () -> assertTrue(login.isLoaded()),
                () -> assertTrue(login.errorMessage().contains("You can only access '/inventory.html' when you are logged in")));
    }

    @Test
    @Tag("smoke")
    @DisplayName("TC06 - User can log out and cannot return with Back")
    void logout() {
        LoginPage login = loginAsStandardUser().header.logout();
        assertTrue(login.isLoaded());

        driver.navigate().back();
        assertTrue(new LoginPage(driver).isLoaded(), "Session should be cleared after logout");
    }
}
