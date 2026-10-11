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
