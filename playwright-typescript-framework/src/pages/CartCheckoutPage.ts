import { Page, Locator } from '@playwright/test';
import { BasePage } from './BasePage';

export class CartCheckoutPage extends BasePage {
  readonly checkoutButton: Locator;
  readonly firstNameInput: Locator;
  readonly lastNameInput: Locator;
  readonly postalCodeInput: Locator;
  readonly continueButton: Locator;
  readonly finishButton: Locator;
  readonly completeHeader: Locator;
  readonly itemTotalLabel: Locator;

  constructor(page: Page) {
    super(page);
    this.checkoutButton = page.locator('#checkout');
    this.firstNameInput = page.locator('#first-name');
    this.lastNameInput = page.locator('#last-name');
    this.postalCodeInput = page.locator('#postal-code');
    this.continueButton = page.locator('#continue');
    this.finishButton = page.locator('#finish');
    this.completeHeader = page.locator('.complete-header');
    this.itemTotalLabel = page.locator('.summary_subtotal_label');
  }

  async proceedToCheckout(): Promise<void> {
    await this.checkoutButton.click();
  }

  async fillCustomerInfo(first: string, last: string, zip: string): Promise<void> {
    await this.firstNameInput.fill(first);
    await this.lastNameInput.fill(last);
    await this.postalCodeInput.fill(zip);
    await this.continueButton.click();
  }

  async completeOrder(): Promise<void> {
    await this.finishButton.click();
  }
}
