import { test, expect } from '@playwright/test';

test.describe('Advanced Browser Interactions Suite', () => {
  test('TC1: Handle JavaScript Alert and Confirm dialogs', async ({ page }) => {
    await page.setContent(`
      <!DOCTYPE html>
      <html>
        <body>
          <button id="confirmBtn" onclick="runConfirm()">Click for JS Confirm</button>
          <p id="result"></p>
          <script>
            function runConfirm() {
              const res = confirm('I am a JS Confirm');
              document.getElementById('result').innerText = res ? 'You clicked: Ok' : 'You clicked: Cancel';
            }
          </script>
        </body>
      </html>
    `);

    page.once('dialog', async (dialog) => {
      expect(dialog.message()).toBe('I am a JS Confirm');
      await dialog.accept();
    });

    await page.locator('#confirmBtn').click();
    await expect(page.locator('#result')).toHaveText('You clicked: Ok');
  });

  test('TC2: Interact across iFrame boundaries', async ({ page }) => {
    await page.setContent(`
      <!DOCTYPE html>
      <html>
        <body>
          <iframe id="custom_frame" srcdoc="<html><body><div id='editor' contenteditable='true'>Initial text</div></body></html>"></iframe>
        </body>
      </html>
    `);

    const frame = page.frameLocator('#custom_frame');
    const editor = frame.locator('#editor');

    await expect(editor).toBeVisible();
    await editor.click();
    await editor.fill('Playwright cross-frame automation test passed.');
    await expect(editor).toHaveText('Playwright cross-frame automation test passed.');
  });

  test('TC3: Explicitly wait for dynamic element loading', async ({ page }) => {
    await page.setContent(`
      <!DOCTYPE html>
      <html>
        <body>
          <div id="start"><button onclick="startLoading()">Start</button></div>
          <div id="finish" style="display:none;"><h4>Hello World!</h4></div>
          <script>
            function startLoading() {
              setTimeout(() => {
                document.getElementById('finish').style.display = 'block';
              }, 1200);
            }
          </script>
        </body>
      </html>
    `);

    await page.locator('#start button').click();
    const finishHeader = page.locator('#finish h4');
    await expect(finishHeader).toBeVisible({ timeout: 10000 });
    await expect(finishHeader).toHaveText('Hello World!');
  });
});