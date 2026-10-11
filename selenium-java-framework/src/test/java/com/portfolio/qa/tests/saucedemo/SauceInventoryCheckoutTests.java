package com.portfolio.qa.tests.saucedemo;

import com.portfolio.qa.pages.SauceCheckoutPage;
import com.portfolio.qa.pages.SauceInventoryPage;
import com.portfolio.qa.pages.SauceLoginPage;
import com.portfolio.qa.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SauceInventoryCheckoutTests extends BaseTest {
    @BeforeMethod
    public void loginStandardUser() {
        new SauceLoginPage(getDriver()).open().login("standard_user", "secret_sauce");
    }

    @Test(description = "Verify Price Low to High Sorting")
    public void testPriceSortLowToHigh() {
        SauceInventoryPage inventory = new SauceInventoryPage(getDriver());
        inventory.selectSortOption("lohi");
        List<Double> actual = inventory.getAllPrices();
        List<Double> sorted = new ArrayList<>(actual);
        Collections.sort(sorted);
        Assert.assertEquals(actual, sorted);
    }

    @Test(description = "Verify E2E checkout completes")
    public void testEndToEndCheckout() {
        SauceInventoryPage inventory = new SauceInventoryPage(getDriver());
        inventory.addItemToCart("Sauce Labs Backpack");
        Assert.assertEquals(inventory.getCartBadgeCount(), "1");
        inventory.goToCart();

        SauceCheckoutPage checkout = new SauceCheckoutPage(getDriver());
        checkout.proceedToCheckout();
        checkout.enterCustomerInfo("Jane", "Doe", "560001");
        checkout.finishCheckout();
        Assert.assertEquals(checkout.getCompleteMessage(), "Thank you for your order!");
    }
}
