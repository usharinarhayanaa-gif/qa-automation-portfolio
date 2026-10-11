package com.portfolio.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SauceCheckoutPage extends BasePage {
    private final By checkoutBtn = By.id("checkout");
    private final By firstName = By.id("first-name");
    private final By lastName = By.id("last-name");
    private final By postalCode = By.id("postal-code");
    private final By continueBtn = By.id("continue");
    private final By finishBtn = By.id("finish");
    private final By completeHeader = By.className("complete-header");

    public SauceCheckoutPage(WebDriver driver) { super(driver); }

    public void proceedToCheckout() {
        wait.until(ExpectedConditions.urlContains("cart.html"));
        click(checkoutBtn);
    }

    public void enterCustomerInfo(String first, String last, String zip) {
        writeText(firstName, first);
        writeText(lastName, last);
        writeText(postalCode, zip);
        click(continueBtn);
        wait.until(ExpectedConditions.urlContains("checkout-step-two.html"));
    }

    public void finishCheckout() {
        click(finishBtn);
        wait.until(ExpectedConditions.urlContains("checkout-complete.html"));
    }

    public String getCompleteMessage() {
        return getText(completeHeader);
    }
}