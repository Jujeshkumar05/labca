# LabCA – End-to-End Web Automation (Selenium WebDriver + JUnit 5)

End-to-end UI test suite for the public demo shop [saucedemo.com](https://www.saucedemo.com/),
built with **Selenium WebDriver 4**, **JUnit 5** and the **Page Object Model**.

## Tech stack
| Tool | Version |
|------|---------|
| Java | 17+ |
| Maven | 3.9+ |
| Selenium WebDriver | 4.27 (Selenium Manager downloads drivers automatically) |
| JUnit Jupiter | 5.11 |

## Project structure
```
src/test/java/com/labca
├── base/BaseTest.java              # Driver lifecycle, common login helper
├── utils/
│   ├── Config.java                 # config.properties + -D overrides
│   ├── DriverFactory.java          # Chrome / Firefox / Edge, headless option
│   └── ScreenshotOnFailure.java    # JUnit 5 TestWatcher -> target/screenshots
├── pages/                          # Page objects
│   ├── LoginPage, InventoryPage, ProductDetailsPage, HeaderComponent
│   └── CartPage, CheckoutInfoPage, CheckoutOverviewPage, CheckoutCompletePage
└── tests/
    ├── LoginTests.java             # TC01–TC06
    ├── InventoryTests.java         # TC07–TC12
    ├── CartTests.java              # TC13–TC18
    ├── CheckoutTests.java          # TC19–TC20
    └── EndToEndPurchaseTest.java   # TC21 full journey
```

## Test cases
| ID | Area | Scenario | Expected result |
|----|------|----------|-----------------|
| TC01 | Login | Valid user logs in | Lands on Products page with 6 items |
| TC02 | Login | Locked-out user | "Sorry, this user has been locked out." |
| TC03 | Login | Wrong password / unknown user / empty fields (5 data rows) | Correct validation message, stays on login |
| TC04 | Login | Dismiss error | Error banner disappears |
| TC05 | Login | Open `/inventory.html` without login | Redirected to login with access error |
| TC06 | Login | Logout, then browser Back | Login page shown, session cleared |
| TC07 | Inventory | Sort Name A→Z | Names ascending |
| TC08 | Inventory | Sort Name Z→A | Names descending |
| TC09 | Inventory | Sort Price low→high | Prices ascending |
| TC10 | Inventory | Sort Price high→low | Prices descending |
| TC11 | Inventory | Open product details | Name, price, description match; Back returns |
| TC12 | Inventory | Add to cart from details page | Cart badge = 1 |
| TC13 | Cart | Add one item | Badge = 1, button changes to Remove |
| TC14 | Cart | Add three items | Badge = 3, all listed in cart |
| TC15 | Cart | Remove from products page | Badge cleared |
| TC16 | Cart | Remove from cart page | Only remaining item listed |
| TC17 | Cart | Continue Shopping | Cart contents persist |
| TC18 | Cart | Reset App State | Cart empty |
| TC19 | Checkout | Missing first name / last name / postal code (4 data rows) | Correct required-field error |
| TC20 | Checkout | Cancel checkout | Back to cart, items intact |
| TC21 | E2E | Login → sort → add 2 items → cart → checkout → verify subtotal, 8% tax, total → finish → logout | "Thank you for your order!", cart empty |

## Running
```bash
mvn test                                   # all tests, headless Chrome
mvn test -Dheadless=false                  # watch the browser
mvn test -Dbrowser=firefox                 # or edge
mvn test -Dgroups=smoke                    # only @Tag("smoke")
mvn test -Dtest=EndToEndPurchaseTest       # single class
```
Reports: `target/surefire-reports/`. Screenshots of failed tests: `target/screenshots/`.
