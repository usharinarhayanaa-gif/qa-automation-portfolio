import { test, expect } from '@playwright/test';
import { LoginPage } from '../../src/pages/LoginPage';
import { InventoryPage } from '../../src/pages/InventoryPage';
import { CartCheckoutPage } from '../../src/pages/CartCheckoutPage';

test('E2E: Add item to cart, verify badge, and complete checkout', async ({ page }) => {
  const loginPage = new LoginPage(page);
  const inventory = new InventoryPage(page);
  const checkout = new CartCheckoutPage(page);

  await loginPage.open();
  await loginPage.login('standard_user', 'secret_sauce');

  await inventory.addItemToCart('Sauce Labs Backpack');
  await expect(inventory.cartBadge).toHaveText('1');

  await inventory.goToCart();
  await checkout.proceedToCheckout();
  await checkout.fillCustomerInfo('Jane', 'Doe', '560001');

  await expect(checkout.itemTotalLabel).toContainText('Item total: $29.99');
  await checkout.completeOrder();

  await expect(checkout.completeHeader).toHaveText('Thank you for your order!');
});
