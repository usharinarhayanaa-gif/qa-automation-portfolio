import { test, expect } from '@playwright/test';
import { LoginPage } from '../../src/pages/LoginPage';
import { InventoryPage } from '../../src/pages/InventoryPage';

test.describe('SauceDemo Inventory Sorting', () => {
  test.beforeEach(async ({ page }) => {
    const loginPage = new LoginPage(page);
    await loginPage.open();
    await loginPage.login('standard_user', 'secret_sauce');
  });

  test('Verify Price Low-to-High Sorting', async ({ page }) => {
    const inventory = new InventoryPage(page);
    await inventory.selectSortOption('lohi');
    const prices = await inventory.getAllItemPrices();
    const sortedPrices = [...prices].sort((a, b) => a - b);
    expect(prices).toEqual(sortedPrices);
  });

  test('Verify Name Z-to-A Sorting', async ({ page }) => {
    const inventory = new InventoryPage(page);
    await inventory.selectSortOption('za');
    const names = await inventory.getAllItemNames();
    const sortedNames = [...names].sort().reverse();
    expect(names).toEqual(sortedNames);
  });
});
