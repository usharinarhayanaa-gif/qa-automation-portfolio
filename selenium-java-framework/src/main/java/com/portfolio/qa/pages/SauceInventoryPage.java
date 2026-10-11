package com.portfolio.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import java.util.List;
import java.util.stream.Collectors;

public class SauceInventoryPage extends BasePage {
    private final By sortSelect = By.className("product_sort_container");
    private final By itemPrices = By.className("inventory_item_price");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");

    public SauceInventoryPage(WebDriver driver) { super(driver); }

    public void selectSortOption(String value) {
        Select select = new Select(waitForVisibility(sortSelect));
        select.selectByValue(value);
    }

    public List<Double> getAllPrices() {
        return driver.findElements(itemPrices).stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "").trim()))
                .collect(Collectors.toList());
    }

    public void addItemToCart(String productName) {
        click(By.xpath("//div[text()='" + productName + "']/ancestor::div[@class='inventory_item']//button"));
    }

    public String getCartBadgeCount() { return getText(cartBadge); }
    public void goToCart() { click(cartLink); }
}
