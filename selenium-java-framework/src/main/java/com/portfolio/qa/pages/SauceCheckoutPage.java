package com.portfolio.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
        wait.until(ExpectedConditions.elementToBeClickable(checkoutBtn)).click();
        wait.until(ExpectedConditions.urlContains("checkout-step-one.html"));
    }

    private void fillReactInput(By locator, String value) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript(
            "const el = arguments[0];" +
            "const val = arguments[1];" +
            "const tracker = el._valueTracker;" +
            "if (tracker) { tracker.setValue(''); }" +
            "const nativeSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
            "nativeSetter.call(el, val);" +
            "el.dispatchEvent(new Event('input', { bubbles: true }));" +
            "el.dispatchEvent(new Event('change', { bubbles: true }));",
            el, value
        );
    }

    public void enterCustomerInfo(String first, String last, String zip) {
        fillReactInput(firstName, first);
        fillReactInput(lastName, last);
        fillReactInput(postalCode, zip);

        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(continueBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

        wait.until(ExpectedConditions.urlContains("checkout-step-two.html"));
    }

    public void finishCheckout() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(finishBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        wait.until(ExpectedConditions.urlContains("checkout-complete.html"));
    }

    public String getCompleteMessage() {
        return getText(completeHeader);
    }
}