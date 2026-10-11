package com.portfolio.qa.tests.theinternet;

import com.portfolio.qa.tests.base.BaseTest;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TheInternetTests extends BaseTest {

    @Test(description = "Handle JavaScript Alert / Confirm dialogs")
    public void testJavaScriptAlert() {
        getDriver().get("https://the-internet.herokuapp.com/javascript_alerts");
        getDriver().findElement(By.xpath("//button[text()='Click for JS Confirm']")).click();
        
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        Assert.assertEquals(alert.getText(), "I am a JS Confirm");
        alert.accept();

        String resultText = getDriver().findElement(By.id("result")).getText();
        Assert.assertEquals(resultText, "You clicked: Ok");
    }

    @Test(description = "Switch to iFrame and type inside rich text editor")
    public void testIFrameEditor() {
        getDriver().get("https://the-internet.herokuapp.com/iframe");
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("mce_0_ifr")));
        WebElement editor = getDriver().findElement(By.id("tinymce"));
        
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].innerText = 'Selenium TestNG iFrame automated text.';", editor);
        
        Assert.assertEquals(editor.getText().trim(), "Selenium TestNG iFrame automated text.");
        getDriver().switchTo().defaultContent();
    }

    @Test(description = "Wait for dynamic element loading")
    public void testDynamicLoading() {
        String html = "data:text/html;charset=utf-8," +
                "<html><body>" +
                "<div id='start'><button onclick='setTimeout(() => { document.getElementById(\"finish\").style.display = \"block\"; }, 1000);'>Start</button></div>" +
                "<div id='finish' style='display:none;'><h4>Hello World!</h4></div>" +
                "</body></html>";
        getDriver().get(html);
        getDriver().findElement(By.cssSelector("#start button")).click();

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        WebElement finish = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#finish h4")));
        Assert.assertEquals(finish.getText().trim(), "Hello World!");
    }
}