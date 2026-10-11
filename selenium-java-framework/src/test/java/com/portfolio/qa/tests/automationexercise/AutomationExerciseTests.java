package com.portfolio.qa.tests.automationexercise;

import com.portfolio.qa.pages.AutomationExercisePage;
import com.portfolio.qa.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AutomationExerciseTests extends BaseTest {
    @Test(description = "Search product and verify catalog result cards")
    public void testSearchProduct() {
        AutomationExercisePage ae = new AutomationExercisePage(getDriver());
        ae.openProducts().searchProduct("Tshirt");
        Assert.assertTrue(ae.isSearchedHeaderVisible());
        Assert.assertTrue(ae.getProductCount() > 0);
    }
}
