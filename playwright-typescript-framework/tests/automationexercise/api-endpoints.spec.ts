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
