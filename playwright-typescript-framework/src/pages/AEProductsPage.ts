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
