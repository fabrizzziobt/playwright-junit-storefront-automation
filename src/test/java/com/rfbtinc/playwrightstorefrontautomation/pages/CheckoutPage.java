package com.rfbtinc.playwrightstorefrontautomation.pages;

import org.assertj.core.api.Assertions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CheckoutPage extends BasePage {

    // Headings / page markers
    private final Locator checkoutHeading;
    private final Locator shippingSection;
    private final Locator paymentSection;
    private final Locator reviewOrderSection;

    // Guest / contact
    private final Locator emailInput;
    private final Locator continueAsGuestButton;
    private final Locator continueToShippingButton;

    // Shipping address
    /*private final Locator firstNameInput;
    private final Locator lastNameInput;
    private final Locator addressLine1Input;
    private final Locator addressLine2Input;
    private final Locator cityInput;
    private final Locator stateInput;
    private final Locator postalCodeInput;
    private final Locator phoneInput;
    private final Locator countrySelect;

    // Shipping actions
    private final Locator shippingMethodSection;
    private final Locator continueToPaymentButton;

    // Billing / payment
    private final Locator billingSameAsShippingCheckbox;
    private final Locator cardNumberInput;
    private final Locator expiryDateInput;
    private final Locator cvvInput;
    private final Locator nameOnCardInput;
    private final Locator placeOrderButton;
    */
    
    // Constructor  
    public CheckoutPage(Page page) {
        super(page);

        // Page / section markers
        this.checkoutHeading = page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName(java.util.regex.Pattern.compile("checkout", java.util.regex.Pattern.CASE_INSENSITIVE)));
        this.shippingSection = page.getByText(java.util.regex.Pattern.compile("shipping", java.util.regex.Pattern.CASE_INSENSITIVE)).first();
        this.paymentSection = page.getByText(java.util.regex.Pattern.compile("payment", java.util.regex.Pattern.CASE_INSENSITIVE)).first();
        this.reviewOrderSection = page.getByText(java.util.regex.Pattern.compile("review order|order review", java.util.regex.Pattern.CASE_INSENSITIVE)).first();

        // Guest / contact
        this.emailInput = page.getByLabel(java.util.regex.Pattern.compile("email", java.util.regex.Pattern.CASE_INSENSITIVE));
        this.continueAsGuestButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName(java.util.regex.Pattern.compile("guest|continue as guest", java.util.regex.Pattern.CASE_INSENSITIVE)));
        this.continueToShippingButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName(java.util.regex.Pattern.compile("continue|continue to shipping|next", java.util.regex.Pattern.CASE_INSENSITIVE)));

        // Shipping address
        /*this.firstNameInput = page.getByLabel(java.util.regex.Pattern.compile("first name", java.util.regex.Pattern.CASE_INSENSITIVE));
        this.lastNameInput = page.getByLabel(java.util.regex.Pattern.compile("last name", java.util.regex.Pattern.CASE_INSENSITIVE));
        this.addressLine1Input = page.getByLabel(java.util.regex.Pattern.compile("address line 1|street address|address", java.util.regex.Pattern.CASE_INSENSITIVE));
        this.addressLine2Input = page.getByLabel(java.util.regex.Pattern.compile("address line 2|apartment|suite", java.util.regex.Pattern.CASE_INSENSITIVE));
        this.cityInput = page.getByLabel(java.util.regex.Pattern.compile("city", java.util.regex.Pattern.CASE_INSENSITIVE));
        this.stateInput = page.getByLabel(java.util.regex.Pattern.compile("state|province|region", java.util.regex.Pattern.CASE_INSENSITIVE));
        this.postalCodeInput = page.getByLabel(java.util.regex.Pattern.compile("zip|postal code", java.util.regex.Pattern.CASE_INSENSITIVE));
        this.phoneInput = page.getByLabel(java.util.regex.Pattern.compile("phone", java.util.regex.Pattern.CASE_INSENSITIVE));
        this.countrySelect = page.getByLabel(java.util.regex.Pattern.compile("country", java.util.regex.Pattern.CASE_INSENSITIVE));

        // Shipping step
        this.shippingMethodSection = page.getByText(java.util.regex.Pattern.compile("shipping method|delivery method", java.util.regex.Pattern.CASE_INSENSITIVE)).first();
        this.continueToPaymentButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName(java.util.regex.Pattern.compile("continue to payment|payment|next", java.util.regex.Pattern.CASE_INSENSITIVE)));

        // Payment / review
        this.billingSameAsShippingCheckbox = page.getByRole(AriaRole.CHECKBOX,
                new Page.GetByRoleOptions().setName(java.util.regex.Pattern.compile("billing.*same.*shipping|same as shipping", java.util.regex.Pattern.CASE_INSENSITIVE)));
        this.cardNumberInput = page.getByLabel(java.util.regex.Pattern.compile("card number", java.util.regex.Pattern.CASE_INSENSITIVE));
        this.expiryDateInput = page.getByLabel(java.util.regex.Pattern.compile("expiration|expiry", java.util.regex.Pattern.CASE_INSENSITIVE));
        this.cvvInput = page.getByLabel(java.util.regex.Pattern.compile("cvv|security code|cvc", java.util.regex.Pattern.CASE_INSENSITIVE));
        this.nameOnCardInput = page.getByLabel(java.util.regex.Pattern.compile("name on card|cardholder", java.util.regex.Pattern.CASE_INSENSITIVE));
        this.placeOrderButton = page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName(java.util.regex.Pattern.compile("place order|submit order|complete order", java.util.regex.Pattern.CASE_INSENSITIVE)));
                */
    }  

    public CheckoutPage waitForCheckoutPage() {
        checkoutHeading.waitFor();
        Assertions.assertThat(isLoaded()).as("Checkout page should be loaded").isTrue();    
        return this;
    }

    public boolean isLoaded() {
        return checkoutHeading.isVisible();
    }

    public CheckoutPage continueAsGuestIfPresent() {
        if (continueAsGuestButton.count() > 0 && continueAsGuestButton.first().isVisible()) {
            continueAsGuestButton.first().click();
        }
        else {
            System.out.println("Continue as Guest button not present");
        }
        return this;
    }

    public CheckoutPage enterContactEmail(String email) {
        if (emailInput.count() > 0 && emailInput.first().isVisible()) {
            emailInput.first().fill(email);
        }
        return this;
    }

    public CheckoutPage continueToShippingStep() {
        if (continueToShippingButton.count() > 0 && continueToShippingButton.first().isVisible()) {
            continueToShippingButton.first().click();
        }
        return this;
    }
}
