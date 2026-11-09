package com.rfbtinc.playwrightstorefrontautomation.pages;

import com.microsoft.playwright.Page;
import com.rfbtinc.playwrightstorefrontautomation.model.ProductItem;
import com.rfbtinc.playwrightstorefrontautomation.utils.CloudflareDetector;

import javax.print.DocFlavor.STRING;

import org.assertj.core.api.Assertions;

/**
 * PDP page object.
 */
public class ProductPage extends BasePage {
    private static final String COOKIE_ACCEPT_BUTTON =
            "button:has-text('Accept'), button#onetrust-accept-btn-handler";
    private static final String PRODUCT_TITLE = "h1";
    private static final String PRODUCT_MODEL = ".product-model";
    private static final String ADD_TO_CART_BUTTON =
            "button:has-text('Add to cart'), button[data-eventname='add_to_cart']";
    private static final String GO_TO_CART_BUTTON = "#viewCartButton";

    public ProductPage(Page page) {
        super(page);
    }

    /**
     * Opens a PDP using a relative path resolved against the configured locale domain.
     */
    public ProductPage open(ProductItem product) {
        page.navigate(product.getPath());
        //CloudflareDetector.failIfBlocked(page);
        acceptCookiesIfPresent();
        return this;
    }

    public ProductPage verifyLoaded(ProductItem product) {
        System.out.println("Verifying product page loaded for: " + product.getName() + " | URL: " + page.url());

        Assertions.assertThat(page.locator(PRODUCT_TITLE).first().textContent()).isNotNull();
        Assertions.assertThat(page.locator(PRODUCT_MODEL).textContent()).containsIgnoringCase(product.getSku());
        Assertions.assertThat(page.content()).contains(product.getExpectedPrice());
        return this;
    }

    public ProductPage addToCart() {
        clickWhenVisible(ADD_TO_CART_BUTTON);
        return this;
    }

    public CartPage goToCart() {
        page.waitForTimeout(1500);
        clickWhenVisible(GO_TO_CART_BUTTON);
        return new CartPage(page);
    }

    private void acceptCookiesIfPresent() {
        try {
            if (page.locator(COOKIE_ACCEPT_BUTTON).first().isVisible()) {
                page.locator(COOKIE_ACCEPT_BUTTON).first().click();
            }
        } catch (Exception ignored) {
        }
    }
}
