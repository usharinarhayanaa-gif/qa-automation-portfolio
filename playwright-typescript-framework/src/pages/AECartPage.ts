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
