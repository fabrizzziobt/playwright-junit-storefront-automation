package com.rfbtinc.playwrightstorefrontautomation.tests;

import com.rfbtinc.playwrightstorefrontautomation.base.BaseTest;
import com.rfbtinc.playwrightstorefrontautomation.model.ProductItem;
import com.rfbtinc.playwrightstorefrontautomation.pages.CartPage;
import com.rfbtinc.playwrightstorefrontautomation.pages.CheckoutPage;
import com.rfbtinc.playwrightstorefrontautomation.pages.ProductPage;
import com.rfbtinc.playwrightstorefrontautomation.utils.ProductDataLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

/**
 * MVP storefront order-start scenario.
 */
class PlaceOrderMvpTest extends BaseTest {

    static Stream<ProductItem> productProvider() {
        List<ProductItem> products = ProductDataLoader.loadProducts();
        return products.stream();
    }

    @ParameterizedTest
    @MethodSource("productProvider")
    @DisplayName("MVP: user can add a predefined product to cart and start checkout")
    void shouldAddPredefinedProductAndStartCheckout(ProductItem product) {


        ProductPage productPage = new ProductPage(page)
                .open(product)
                .verifyLoaded(product)
                .addToCart();

        CartPage cartPage = productPage
                .goToCart()
                .verifyProductInCart(product);

        CheckoutPage checkout = cartPage
                .proceedToCheckout()
                .waitForCheckoutPage()
                .enterContactEmail("g10xsharkninjatesting@gmail.com")
                .continueAsGuest()
                .continueToShippingStep();
    }
}
