$root = "D:\qa-automation-portfolio"

# Ensure directories exist
New-Item -ItemType Directory -Path "$root\playwright-typescript-framework\src\pages" -Force | Out-Null
New-Item -ItemType Directory -Path "$root\playwright-typescript-framework\tests\automationexercise" -Force | Out-Null
New-Item -ItemType Directory -Path "$root\selenium-java-framework\src\main\java\com\portfolio\qa\pages" -Force | Out-Null
New-Item -ItemType Directory -Path "$root\selenium-java-framework\src\test\java\com\portfolio\qa\tests\saucedemo" -Force | Out-Null
New-Item -ItemType Directory -Path "$root\selenium-java-framework\src\test\java\com\portfolio\qa\tests\automationexercise" -Force | Out-Null

# 1. Playwright: AEProductsPage.ts
@'
import { Page, Locator } from '@playwright/test';
import { BasePage } from './BasePage';

export class AEProductsPage extends BasePage {
  readonly searchInput: Locator;
  readonly searchBtn: Locator;
  readonly searchedProductsHeader: Locator;
  readonly productCards: Locator;
  readonly viewCartModalLink: Locator;

  constructor(page: Page) {
    super(page);
    this.searchInput = page.locator('#search_product');
    this.searchBtn = page.locator('#submit_search');
    this.searchedProductsHeader = page.getByRole('heading', { name: 'SEARCHED PRODUCTS' });
    this.productCards = page.locator('.features_items .col-sm-4');
    this.viewCartModalLink = page.locator('#cartModal a[href="/view_cart"]');
  }

  async open(): Promise<void> {
    await this.page.goto('https://automationexercise.com/products', { waitUntil: 'domcontentloaded' });
  }

  async searchForProduct(term: string): Promise<void> {
    await this.searchInput.fill(term);
    await this.searchBtn.click();
  }

  async addFirstProductToCart(): Promise<void> {
    await this.page.locator('.features_items .add-to-cart').first().click();
  }

  async goToCartFromModal(): Promise<void> {
    await this.viewCartModalLink.click();
  }
}
'@ | Set-Content -Path "$root\playwright-typescript-framework\src\pages\AEProductsPage.ts" -Encoding UTF8

# 2. Playwright: AECartPage.ts
@'
import { Page, Locator } from '@playwright/test';
import { BasePage } from './BasePage';

export class AECartPage extends BasePage {
  readonly cartTableRows: Locator;

  constructor(page: Page) {
    super(page);
    this.cartTableRows = page.locator('#cart_info_table tbody tr');
  }

  async open(): Promise<void> {
    await this.page.goto('https://automationexercise.com/view_cart', { waitUntil: 'domcontentloaded' });
  }

  async getCartItemCount(): Promise<number> {
    return await this.cartTableRows.count();
  }
}
'@ | Set-Content -Path "$root\playwright-typescript-framework\src\pages\AECartPage.ts" -Encoding UTF8

# 3. Playwright: products-cart.spec.ts
@'
import { test, expect } from '@playwright/test';
import { AEProductsPage } from '../../src/pages/AEProductsPage';
import { AECartPage } from '../../src/pages/AECartPage';

test.describe('Automation Exercise - UI Suite', () => {
  test('TC1: Search product and verify filtered catalog result cards', async ({ page }) => {
    const productsPage = new AEProductsPage(page);
    await productsPage.open();
    await productsPage.searchForProduct('Tshirt');
    await expect(productsPage.searchedProductsHeader).toBeVisible();
    const count = await productsPage.productCards.count();
    expect(count).toBeGreaterThan(0);
  });

  test('TC2: Add product to cart and verify cart item entry', async ({ page }) => {
    const productsPage = new AEProductsPage(page);
    const cartPage = new AECartPage(page);
    await productsPage.open();
    await productsPage.addFirstProductToCart();
    await productsPage.goToCartFromModal();
    await expect(page).toHaveURL(/.*view_cart/);
    const items = await cartPage.getCartItemCount();
    expect(items).toBeGreaterThanOrEqual(1);
  });
});
'@ | Set-Content -Path "$root\playwright-typescript-framework\tests\automationexercise\products-cart.spec.ts" -Encoding UTF8

# 4. Playwright: api-endpoints.spec.ts
@'
import { test, expect } from '@playwright/test';

const BASE_API = 'https://automationexercise.com/api';

test.describe('Automation Exercise - Official REST API Suite', () => {
  test('API 1: GET All Products List - returns 200 with catalog array', async ({ request }) => {
    const response = await request.get(`${BASE_API}/productsList`);
    expect(response.status()).toBe(200);
    const data = JSON.parse(await response.text());
    expect(data.responseCode).toBe(200);
    expect(data.products.length).toBeGreaterThan(0);
  });

  test('API 2: POST To All Products List - returns 405 Method Not Supported', async ({ request }) => {
    const response = await request.post(`${BASE_API}/productsList`);
    expect(response.status()).toBe(200);
    const data = JSON.parse(await response.text());
    expect(data.responseCode).toBe(405);
  });

  test('API 3: GET All Brands List - returns 200 with brands catalog', async ({ request }) => {
    const response = await request.get(`${BASE_API}/brandsList`);
    expect(response.status()).toBe(200);
    const data = JSON.parse(await response.text());
    expect(data.responseCode).toBe(200);
  });

  test('API 5: POST To Search Product - searches product and returns items', async ({ request }) => {
    const response = await request.post(`${BASE_API}/searchProduct`, {
      form: { search_product: 'top' },
    });
    expect(response.status()).toBe(200);
    const data = JSON.parse(await response.text());
    expect(data.responseCode).toBe(200);
  });

  test('API 10: POST To Verify Login with invalid details - returns 404', async ({ request }) => {
    const response = await request.post(`${BASE_API}/verifyLogin`, {
      form: { email: 'invalid_qa_user_9999@test.com', password: 'wrongpassword' },
    });
    expect(response.status()).toBe(200);
    const data = JSON.parse(await response.text());
    expect(data.responseCode).toBe(404);
  });
});
'@ | Set-Content -Path "$root\playwright-typescript-framework\tests\automationexercise\api-endpoints.spec.ts" -Encoding UTF8

# 5. Selenium: SauceInventoryPage.java
@'
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
'@ | Set-Content -Path "$root\selenium-java-framework\src\main\java\com\portfolio\qa\pages\SauceInventoryPage.java" -Encoding UTF8

# 6. Selenium: SauceCheckoutPage.java
@'
package com.portfolio.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SauceCheckoutPage extends BasePage {
    private final By checkoutBtn = By.id("checkout");
    private final By firstName = By.id("first-name");
    private final By lastName = By.id("last-name");
    private final By postalCode = By.id("postal-code");
    private final By continueBtn = By.id("continue");
    private final By finishBtn = By.id("finish");
    private final By completeHeader = By.className("complete-header");

    public SauceCheckoutPage(WebDriver driver) { super(driver); }
    public void proceedToCheckout() { click(checkoutBtn); }
    public void enterCustomerInfo(String first, String last, String zip) {
        writeText(firstName, first);
        writeText(lastName, last);
        writeText(postalCode, zip);
        click(continueBtn);
    }
    public void finishCheckout() { click(finishBtn); }
    public String getCompleteMessage() { return getText(completeHeader); }
}
'@ | Set-Content -Path "$root\selenium-java-framework\src\main\java\com\portfolio\qa\pages\SauceCheckoutPage.java" -Encoding UTF8

# 7. Selenium: AutomationExercisePage.java
@'
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
'@ | Set-Content -Path "$root\selenium-java-framework\src\main\java\com\portfolio\qa\pages\AutomationExercisePage.java" -Encoding UTF8

# 8. Selenium: SauceInventoryCheckoutTests.java
@'
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
'@ | Set-Content -Path "$root\selenium-java-framework\src\test\java\com\portfolio\qa\tests\saucedemo\SauceInventoryCheckoutTests.java" -Encoding UTF8

# 9. Selenium: AutomationExerciseTests.java
@'
package com.portfolio.qa.tests.automationexercise;

import com.portfolio.qa.pages.AutomationExercisePage;
import com.portfolio.qa.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AutomationExerciseTests extends BaseTest {
    @Test(description = "Search product and verify catalog result cards")
    public void testSearchProduct() {
        AutomationExercisePage ae = new AutomationExercisePage(getDriver());
        ae.openProducts().searchProduct("Tshirt");
        Assert.assertTrue(ae.isSearchedHeaderVisible());
        Assert.assertTrue(ae.getProductCount() > 0);
    }
}
'@ | Set-Content -Path "$root\selenium-java-framework\src\test\java\com\portfolio\qa\tests\automationexercise\AutomationExerciseTests.java" -Encoding UTF8

# 10. Selenium: testng.xml
@'
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd" >
<suite name="QA Automation Suite" verbose="1">
    <test name="SauceDemo Tests">
        <classes>
            <class name="com.portfolio.qa.tests.saucedemo.SauceLoginTests"/>
            <class name="com.portfolio.qa.tests.saucedemo.SauceInventoryCheckoutTests"/>
        </classes>
    </test>
    <test name="Automation Exercise Tests">
        <classes>
            <class name="com.portfolio.qa.tests.automationexercise.AutomationExerciseTests"/>
        </classes>
    </test>
</suite>
'@ | Set-Content -Path "$root\selenium-java-framework\testng.xml" -Encoding UTF8

Write-Host "All files created cleanly!" -ForegroundColor Green