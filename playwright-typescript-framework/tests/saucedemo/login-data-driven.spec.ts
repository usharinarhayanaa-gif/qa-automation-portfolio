import { test, expect } from '@playwright/test';
import { LoginPage } from '../../src/pages/LoginPage';

const loginScenarios = [
  {
    username: 'standard_user',
    password: 'secret_sauce',
    shouldSucceed: true,
    expectedError: '',
  },
  {
    username: 'locked_out_user',
    password: 'secret_sauce',
    shouldSucceed: false,
    expectedError: 'Epic sadface: Sorry, this user has been locked out.',
  },
  {
    username: 'invalid_user',
    password: 'invalid_pass',
    shouldSucceed: false,
    expectedError: 'Epic sadface: Username and password do not match any user in this service',
  },
];

test.describe('SauceDemo Authentication Suite', () => {
  for (const scenario of loginScenarios) {
    test(`Verify login for user: ${scenario.username}`, async ({ page }) => {
      const loginPage = new LoginPage(page);
      await loginPage.open();
      await loginPage.login(scenario.username, scenario.password);

      if (scenario.shouldSucceed) {
        await expect(page).toHaveURL(/.*inventory\.html/);
        await expect(loginPage.appLogo).toBeVisible();
      } else {
        await expect(loginPage.errorMessage).toHaveText(scenario.expectedError);
      }
    });
  }
});
