package com.portfolio.qa.tests.saucedemo;

import com.portfolio.qa.pages.SauceCheckoutPage;
import com.portfolio.qa.pages.SauceInventoryPage;
import com.portfolio.qa.pages.SauceLoginPage;
import com.portfolio.qa.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class SauceInventoryCheckoutTests extends BaseTest {
    private SauceInventoryPage inventory;
    private SauceCheckoutPage checkout;

    @BeforeMethod
    public void setupInventory() {
        SauceLoginPage loginPage = new SauceLoginPage(getDriver());
        loginPage.open();
        loginPage.login("standard_user", "secret_sauce");
        inventory = new SauceInventoryPage(getDriver());
        checkout = new SauceCheckoutPage(getDriver());
    }

    @Test(description = "Verify product sorting by price Low to High")
    public void testPriceSortLowToHigh() {
        inventory.selectSortOption("lohi");
        List<Double> prices = inventory.getAllPrices();
        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(prices.get(i) <= prices.get(i + 1), "Prices not sorted ascending!");
        }
    }

    @Test(description = "Verify complete E2E checkout workflow")
    public void testEndToEndCheckout() {
        inventory.addItemToCart("Sauce Labs Backpack");
        inventory.goToCart();
        checkout.proceedToCheckout();
        checkout.enterCustomerInfo("Jane", "Doe", "12345");
        checkout.finishCheckout();
        Assert.assertEquals(checkout.getCompleteMessage(), "Thank you for your order!");
    }
}