// src/test/java/com/rfbtinc/playwrightstorefrontautomation/pages/BasePage.java
package com.rfbtinc.playwrightstorefrontautomation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * Parent class for all page objects.
 * Stores the shared Page instance and common helper actions.
 */
public abstract class BasePage {
    protected final Page page;

    protected BasePage(Page page) {
        this.page = page;
    }

    /**
     * Waits until the target element is visible before clicking.
     * This reduces duplicated wait/click code across page classes.
     */
    protected void clickWhenVisible(String selector) {
        page.locator(selector).first().waitFor(
                new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        page.locator(selector).first().click();
    }
}
