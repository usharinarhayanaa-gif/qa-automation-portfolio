package com.portfolio.qa.tests.saucedemo;

import com.portfolio.qa.pages.SauceLoginPage;
import com.portfolio.qa.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SauceLoginTests extends BaseTest {

    @DataProvider(name = "loginScenarios")
    public Object[][] getLoginData() {
        return new Object[][] {
            {"standard_user", "secret_sauce", true, ""},
            {"locked_out_user", "secret_sauce", false, "Epic sadface: Sorry, this user has been locked out."},
            {"invalid_user", "invalid_pass", false, "Epic sadface: Username and password do not match any user in this service"}
        };
    }

    @Test(dataProvider = "loginScenarios", description = "Verify authentication flows on SauceDemo")
    public void testSauceDemoLogin(String username, String password, boolean shouldSucceed, String expectedErrorMessage) {
        SauceLoginPage loginPage = new SauceLoginPage(getDriver());
        loginPage.open();
        loginPage.login(username, password);

        if (shouldSucceed) {
            Assert.assertTrue(getDriver().getCurrentUrl().contains("inventory.html"), "URL should redirect to inventory page.");
            Assert.assertTrue(loginPage.isInventoryLoaded(), "Inventory header logo should be visible.");
        } else {
            Assert.assertEquals(loginPage.getErrorMessage(), expectedErrorMessage, "Error message must match expected alert.");
        }
    }
}
