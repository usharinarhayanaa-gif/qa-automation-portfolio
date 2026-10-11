import { Page, Locator } from '@playwright/test';
import { BasePage } from './BasePage';

export class InventoryPage extends BasePage {
  readonly sortDropdown: Locator;
  readonly itemNames: Locator;
  readonly itemPrices: Locator;
  readonly cartBadge: Locator;
  readonly cartLink: Locator;

  constructor(page: Page) {
    super(page);
    this.sortDropdown = page.locator('.product_sort_container');
    this.itemNames = page.locator('.inventory_item_name');
    this.itemPrices = page.locator('.inventory_item_price');
    this.cartBadge = page.locator('.shopping_cart_badge');
    this.cartLink = page.locator('.shopping_cart_link');
  }

  async selectSortOption(optionValue: string): Promise<void> {
    await this.sortDropdown.selectOption(optionValue);
  }

  async getAllItemNames(): Promise<string[]> {
    return await this.itemNames.allInnerTexts();
  }

  async getAllItemPrices(): Promise<number[]> {
    const priceTexts = await this.itemPrices.allInnerTexts();
    return priceTexts.map(p => parseFloat(p.replace('$', '')));
  }

  async addItemToCart(itemName: string): Promise<void> {
    const item = this.page.locator('.inventory_item').filter({ hasText: itemName });
    await item.getByRole('button', { name: /add to cart/i }).click();
  }

  async goToCart(): Promise<void> {
    await this.cartLink.click();
  }
}
