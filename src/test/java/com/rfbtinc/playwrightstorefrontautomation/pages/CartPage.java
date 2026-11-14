// src/test/java/com/rfbtinc/playwrightstorefrontautomation/pages/CartPage.java
package com.rfbtinc.playwrightstorefrontautomation.pages;

import com.microsoft.playwright.Page;
import com.rfbtinc.playwrightstorefrontautomation.model.ProductItem;
import org.assertj.core.api.Assertions;

/**
 * Page object for the cart or mini-cart destination.
 * Responsible for checking product presence and starting checkout.
 */
public class CartPage extends BasePage {
    private static final String CHECKOUT_BUTTON =
            "button:has-text('Checkout'), a:has-text('Checkout')";

    public CartPage(Page page) {
        super(page);
    }

    public CartPage(ProductPage pdp){
        super((Page) pdp);
    }

    /**
     * Confirms the selected product appears in cart content.
     * For MVP, SKU text is the fastest stable check.
     */
    public CartPage verifyProductInCart(ProductItem product) {
        Assertions.assertThat(page.content()).containsIgnoringCase(product.getSku());
        return this;
    }

    /**
     * Starts the checkout flow.
     * In this MVP, reaching checkout is treated as the end of the happy path.
     */
    public CheckoutPage proceedToCheckout() {
        clickWhenVisible(CHECKOUT_BUTTON);
        return new CheckoutPage(page);
    }
}
