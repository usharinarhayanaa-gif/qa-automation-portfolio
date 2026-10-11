package com.portfolio.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SauceLoginPage extends BasePage {
    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");
    private final By appLogo = By.className("app_logo");

    public SauceLoginPage(WebDriver driver) {
        super(driver);
    }

    public SauceLoginPage open() {
        driver.get("https://www.saucedemo.com/");
        return this;
    }

    public void login(String username, String password) {
        writeText(usernameInput, username);
        writeText(passwordInput, password);
        click(loginButton);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isInventoryLoaded() {
        return isDisplayed(appLogo);
    }
}
