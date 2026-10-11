package com.portfolio.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AutomationExercisePage extends BasePage {
    private final By searchInput = By.id("search_product");
    private final By searchBtn = By.id("submit_search");
    private final By searchedHeader = By.xpath("//h2[contains(text(), 'Searched Products')]");
    private final By productCards = By.cssSelector(".features_items .col-sm-4");

    public AutomationExercisePage(WebDriver driver) { super(driver); }

    public AutomationExercisePage openProducts() {
        driver.get("https://automationexercise.com/products");
        return this;
    }

    public void searchProduct(String term) {
        writeText(searchInput, term);
        click(searchBtn);
    }

    public boolean isSearchedHeaderVisible() { return isDisplayed(searchedHeader); }
    public int getProductCount() { return driver.findElements(productCards).size(); }
}
