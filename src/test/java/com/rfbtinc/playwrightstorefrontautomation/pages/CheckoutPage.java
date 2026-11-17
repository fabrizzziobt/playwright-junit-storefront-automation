package com.rfbtinc.playwrightstorefrontautomation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.SelectOption;
import com.rfbtinc.playwrightstorefrontautomation.model.CheckoutState;
import com.rfbtinc.playwrightstorefrontautomation.model.ShippingAddress;
import com.rfbtinc.playwrightstorefrontautomation.utils.ConfigManager;

import java.util.Objects;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CheckoutPage extends BasePage {

    public enum ShippingMethod {
        STANDARD,
        EXPRESS,       // Placeholder
        NEXT_DAY       // Placeholder
    }

    public enum PaymentMethod {
        CREDIT_CARD,
        PAYPAL,        // Placeholder
        APPLE_PAY,     // Placeholder
        BUY_NOW_PAY_LATER // Placeholder
    }

    /*
     * VERIFY THESE CONTAINER SELECTORS against your checkout.
     * Sections must contain their respective fields/options, not just headings.
     */
    private static final String SHIPPING_CONTAINER =
            ".card.shipping-section";
    private static final String SHIPPING_METHOD_CONTAINER =
            ".shipping-method-list";
    private static final String PAYMENT_CONTAINER =
            ".card.payment-form";
    private static final String CREDIT_CARD_CONTAINER =
            ".adyen-checkout__loading-input__form.loading-input__form";

    // Headings / page markers
    private final Locator checkoutHeading;
    private final Locator shippingSection;
    private final Locator paymentSection;
    private final Locator reviewOrderSection;

    // Guest / contact: preserved from the original class
    private final Locator emailInput;
    private final Locator continueAsGuestButton;
    private final Locator continueToShippingButton;

    // Shipping address
    private final Locator firstNameInput;
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
    private final Locator standardShippingRadio;
    private final Locator continueToPaymentButton;

    // Billing / payment
    private final Locator billingSameAsShippingCheckbox;
    private final Locator creditCardRadio;
    private final Locator creditCardLabel;
    private final Locator creditCardSection;
    private final Locator cardNumberInput;
    private final Locator expiryDateInput;
    private final Locator cvvInput;
    private final Locator nameOnCardInput;
    private final Locator placeOrderButton;

    // Validated / observed checkout state
    private ShippingAddress shippingAddress;
    private ShippingMethod shippingMethod;
    private String shippingMethodValue;
    private String shippingMethodLabel;
    private PaymentMethod paymentMethod;
    private String paymentMethodValue;
    private boolean billingSameAsShipping;

    public CheckoutPage(Page page) {
        super(page);

        checkoutHeading = page.getByRole(
                AriaRole.HEADING,
                new Page.GetByRoleOptions()
                        .setName(text("^checkout$"))
        );

        shippingSection = page.locator(SHIPPING_CONTAINER);
        paymentSection = page.locator(PAYMENT_CONTAINER);

        reviewOrderSection = page.getByRole(
                AriaRole.HEADING,
                new Page.GetByRoleOptions()
                        .setName(text("^(review order|order review)$"))
        );

        // Guest / contact
        emailInput = page.locator("#email-guest");
                //page.getByLabel(text("^email(?: address)?\\s*\\*?$"));

        continueAsGuestButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                        .setName(text(
                                "^(guest|continue as guest|checkout as guest)$"))
        );

        continueToShippingButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                        .setName(text(
                                "^(continue|continue to shipping|next)$"))
        );

        // Shipping fields: scoped to avoid matching billing fields.
        firstNameInput = shippingSection.locator("#shippingFirstNamedefault");
        lastNameInput = shippingSection.locator("#shippingLastNamedefault");
        addressLine1Input = shippingSection.locator("#shippingAddressOnedefault");
        addressLine2Input = shippingSection.locator("#shippingAddressTwodefault");
        cityInput = shippingSection.locator("#shippingAddressCitydefault");
        stateInput = shippingSection.locator("#shippingStatedefault");
        postalCodeInput = shippingSection.locator("#shippingZipCodedefault");
        phoneInput = shippingSection.locator("#shippingPhoneNumberdefault");
        countrySelect = shippingSection.locator("#shippingCountrydefault");

        // Shipping options
        shippingMethodSection =
                page.locator(SHIPPING_METHOD_CONTAINER);

        standardShippingRadio = shippingMethodSection.getByRole(
                AriaRole.RADIO,
                new Locator.GetByRoleOptions()
                        .setName(text("^standard(?: shipping)?(?:\\s.*)?$"))
        );

        continueToPaymentButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                        .setName(text("^(continue to payment|payment|next)$"))
        );

        // Billing and payment
        billingSameAsShippingCheckbox = page.getByRole(
                AriaRole.CHECKBOX,
                new Page.GetByRoleOptions()
                        .setName(text(
                                "^(billing address and shipping address are the same"
                                        + "|same as shipping"
                                        + "|use shipping address as billing address)$"))
        );

        creditCardRadio = paymentSection.getByRole(
                AriaRole.RADIO,
                new Locator.GetByRoleOptions()
                        .setName(text("^(credit card|credit or debit card|cards)$"))
        );

        // Explicitly target the label associated with the radio input.
        creditCardLabel = paymentSection.locator("label[id='lb_scheme'][for='rb_scheme']");

        creditCardSection = paymentSection.locator(CREDIT_CARD_CONTAINER);

        /*
         * These assume ordinary fields in the page DOM.
         * Hosted payment iframes require frameLocator mappings instead.
         * They are recorded here but are not filled in this MVP flow.
         */
        cardNumberInput = creditCardSection.getByLabel(
                text("^card number\\s*\\*?$"));
        expiryDateInput = creditCardSection.getByLabel(
                text("^(expiration date|expiry date|expiration|expiry)\\s*\\*?$"));
        cvvInput = creditCardSection.getByLabel(
                text("^(cvv|cvc|security code)\\s*\\*?$"));
        nameOnCardInput = creditCardSection.getByLabel(
                text("^(name on card|cardholder name)\\s*\\*?$"));

        placeOrderButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                        .setName(text(
                                "^(place order|submit order|complete order)$"))
        );
    }

    // -------------------------------------------------------
    // Checkout entry and guest/contact flow
    // -------------------------------------------------------

    public CheckoutPage waitForCheckoutPage() {
        page.waitForLoadState(
                LoadState.LOAD,
                new Page.WaitForLoadStateOptions().setTimeout(ConfigManager.timeoutMs())
        );

        assertThat(emailInput).isVisible();
        assertThat(emailInput).isEditable();

        assertThat(continueAsGuestButton).isVisible();
        assertThat(continueAsGuestButton).isEnabled();

        return this;
    }

    public boolean isLoaded() {
        return checkoutHeading.isVisible();
    }

    public CheckoutPage continueAsGuest() {
        try {
            assertThat(emailInput).hasValue(Pattern.compile("^(?![.'])(?!.*'@)(?!.*'' )(?!.*\\.\\.)[A-Za-z0-9_.\\-+'ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõöøùúûüýþÿČčĎďĚěŘřŠšŤťŽžȘșȚțŁłŻżŚśŹźĆćŃńÓóĘęĄąĞğŞşİıœ]+@[a-zA-Z0-9](?:[a-zA-Z0-9\\-]*[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9\\-]*[a-zA-Z0-9])?)*\\.[a-zA-Z]{2,}$"));
        } catch (AssertionError cause) {
            throw new AssertionError(
                    "Cannot continue as guest: the email input is empty, "
                            + "contains only whitespace, or could not be located. "
                            + "The previous action must populate the email input.",
                    cause
            );
        }

        try {
            assertThat(continueAsGuestButton).isVisible();
        } catch (AssertionError cause) {
            throw new AssertionError(
                    "Cannot continue as guest: the 'Continue as guest' button "
                            + "is not visible or could not be uniquely located.",
                    cause
            );
        }

        try {
            assertThat(continueAsGuestButton).isEnabled();
        } catch (AssertionError cause) {
            throw new AssertionError(
                    "Cannot continue as guest: the 'Continue as guest' button "
                            + "is not enabled.",
                    cause
            );
        }

        try {
            continueAsGuestButton.click();
        } catch (TimeoutError cause) {
            throw new AssertionError(
                    "Could not click 'Continue as guest': the button did not "
                            + "become actionable before the timeout. "
                            + "Check for overlays, animation, or a changing button state.",
                    cause
            );
        }

        try {
            assertThat(shippingSection).isVisible(
                    new LocatorAssertions.IsVisibleOptions()
                            .setTimeout(15_000)
            );
        } catch (AssertionError cause) {
            throw new AssertionError(
                    "Guest checkout did not reach the shipping step within 15 seconds. "
                            + "The click completed, but the expected shipping section "
                            + "did not become visible. Check application initialization, "
                            + "email validation, checkout requests, and browser errors.",
                    cause
            );
        }

        return this;
    }


    public CheckoutPage enterContactEmail(String email) {
        // Required action: fail rather than silently skip a missing field.
        emailInput.fill(email);
        assertThat(emailInput).hasValue(email);
        return this;
    }

    public CheckoutPage continueToShippingStep() {
        continueToShippingButton.click();
        return waitForShippingSection();
    }

    public CheckoutPage waitForShippingSection() {
        assertThat(shippingSection).isVisible();
        assertThat(firstNameInput).isEditable();
        return this;
    }

    // -------------------------------------------------------
    // Shipping address
    // -------------------------------------------------------

    public CheckoutPage fillShippingAddress(ShippingAddress address) {
        Objects.requireNonNull(address, "Shipping address is required");

        // Invalidate previous address tracking before changing the form.
        shippingAddress = null;

        /*
         * Assumes native country/state <select> controls.
         * Country is selected first because it can update state options.
         */
        countrySelect.selectOption(
                new SelectOption().setValue(address.countryCode()));

        firstNameInput.fill(address.firstName());
        lastNameInput.fill(address.lastName());
        addressLine1Input.fill(address.addressLine1());
        addressLine2Input.fill(emptyIfNull(address.addressLine2()));
        cityInput.fill(address.city());

        selectStateOrProvince(address.stateOrProvince());

        postalCodeInput.fill(address.postalCode());
        phoneInput.fill(address.phone());

        return validateShippingAddress(address);
    }

    // Preserves the original multi-argument helper shape.
    public CheckoutPage fillShippingAddress(
            String firstName,
            String lastName,
            String address1,
            String address2,
            String city,
            String stateOrProvince,
            String postalCode,
            String phone,
            String country
    ) {
        return fillShippingAddress(new ShippingAddress(
                firstName, lastName, address1, address2, city,
                stateOrProvince, postalCode, phone, country
        ));
    }

    public CheckoutPage selectStateOrProvince(String value) {
        assertThat(stateInput).isVisible();

        String tag = (String) stateInput.evaluate("el => el.tagName");

        if ("SELECT".equalsIgnoreCase(tag)) {
            stateInput.selectOption(new SelectOption().setValue(value));
        } else {
            // Supports a text input, not a custom dropdown widget.
            stateInput.fill(value);
        }

        return this;
    }

    public CheckoutPage validateShippingAddress(ShippingAddress expected) {
        assertThat(firstNameInput).hasValue(expected.firstName());
        assertThat(lastNameInput).hasValue(expected.lastName());
        assertThat(addressLine1Input).hasValue(expected.addressLine1());
        assertThat(addressLine2Input).hasValue(
                emptyIfNull(expected.addressLine2()));
        assertThat(cityInput).hasValue(expected.city());
        assertThat(stateInput).hasValue(expected.stateOrProvince());
        assertThat(postalCodeInput).hasValue(expected.postalCode());
        assertThat(countrySelect).hasValue(expected.countryCode());

        // Allows separators inserted by the phone field's formatting.
        assertThat(phoneInput).hasValue(phonePattern(expected.phone()));

        shippingAddress = expected;
        return this;
    }

    // -------------------------------------------------------
    // Shipping method
    // -------------------------------------------------------

    public CheckoutPage waitForShippingMethods() {
        assertThat(shippingMethodSection).isVisible();
        assertThat(standardShippingRadio).isEnabled();
        return this;
    }

    public CheckoutPage selectShippingMethod(ShippingMethod method) {
        if (method != ShippingMethod.STANDARD) {
            throw new UnsupportedOperationException(
                    "Shipping method not implemented: " + method);
        }

        waitForShippingMethods();
        standardShippingRadio.check();
        assertThat(standardShippingRadio).isChecked();

        /*
         * Retrieve the radio's associated HTML label when available.
         * For custom controls, adapt this to their label structure.
         */
        String label = (String) standardShippingRadio.evaluate(
                "el => el.labels && el.labels.length"
                        + " ? Array.from(el.labels)"
                        + "     .map(label => label.innerText).join(' ').trim()"
                        + " : (el.getAttribute('aria-label') || '')"
        );

        if (label.isBlank()) {
            throw new IllegalStateException(
                    "Map the standard shipping label before tracking the method.");
        }

        shippingMethod = method;
        shippingMethodValue = standardShippingRadio.inputValue();
        shippingMethodLabel = label;

        return this;
    }

    // Retains the original convenience method; now explicitly means standard.
    public CheckoutPage selectDefaultShippingMethod() {
        return selectShippingMethod(ShippingMethod.STANDARD);
    }

    public CheckoutPage continueToPayment() {
        if (shippingAddress == null || shippingMethod == null) {
            throw new IllegalStateException(
                    "Validate the shipping address and select shipping first.");
        }

        // Recheck current form/selection before advancing.
        validateShippingAddress(shippingAddress);
        assertThat(standardShippingRadio).isChecked();

        try {
            continueToPaymentButton.click();
        } catch (TimeoutError cause)
        {
            throw new AssertionError(
                    "Cannot continue to Payment: shipping address is invalid,"
                            + "shipping method is missing, or "
                            + "did not click button",
                    cause
            );
        }
        return waitForPaymentSection();
    }

    // -------------------------------------------------------
    // Payment / billing
    // -------------------------------------------------------

    public CheckoutPage waitForPaymentSection() {
        assertThat(paymentSection).isVisible();
        return this;
    }

    public CheckoutPage selectPaymentMethod(PaymentMethod method) {
        if (method != PaymentMethod.CREDIT_CARD) {
            throw new UnsupportedOperationException(
                    "Payment method not implemented: " + method);
        }

        try {
            creditCardLabel.click();
        } catch (TimeoutError cause) {
            throw new AssertionError(
                    "Cannot select card payment: the Cards label "
                            + "could not be clicked before the timeout.",
                    cause
            );
        }

        //creditCardRadio.click();
        assertThat(creditCardRadio).isChecked();
        assertThat(creditCardSection).isVisible();

        paymentMethod = method;
        paymentMethodValue = creditCardRadio.inputValue();

        return this;
    }

    public CheckoutPage useBillingSameAsShipping() {
        // A required MVP condition: do not silently skip it.
        billingSameAsShippingCheckbox.setChecked(true);
        assertThat(billingSameAsShippingCheckbox).isChecked();
        billingSameAsShipping = true;
        return this;
    }

    // Compatibility with the earlier boilerplate's method name.
    public CheckoutPage useBillingSameAsShippingIfPresent() {
        return useBillingSameAsShipping();
    }

    public CheckoutPage validatePaymentStepReady() {
        if (shippingAddress == null
                || shippingMethod != ShippingMethod.STANDARD
                || paymentMethod != PaymentMethod.CREDIT_CARD
                || !billingSameAsShipping) {
            throw new IllegalStateException(
                    "Checkout is not ready: address, standard shipping, "
                            + "credit card and matching billing are required.");
        }

        assertThat(paymentSection).isVisible();
        assertThat(creditCardRadio).isChecked();
        assertThat(creditCardSection).isVisible();
        assertThat(billingSameAsShippingCheckbox).isChecked();

        return this;
    }

    public CheckoutState getCheckoutState() {
        return new CheckoutState(
                shippingAddress,
                shippingAddress != null,
                shippingMethodValue,
                shippingMethodLabel,
                billingSameAsShipping,
                paymentMethodValue
        );
    }

    // -------------------------------------------------------
    // Review markers: recorded for later implementation
    // -------------------------------------------------------

    public CheckoutPage waitForReviewOrderSection() {
        assertThat(reviewOrderSection).isVisible();
        return this;
    }

    public boolean isPlaceOrderButtonVisible() {
        return placeOrderButton.isVisible();
    }

    /*
     * Deliberately no clickPlaceOrder() or card-entry action in this MVP.
     * The requested scenario stops at the payment step.
     */

    private static Pattern text(String expression) {
        return Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
    }

    private static String emptyIfNull(String value) {
        return value == null ? "" : value;
    }

    private static Pattern phonePattern(String value) {
        String digits = Objects.requireNonNull(value, "Phone is required")
                .replaceAll("\\D", "");

        if (digits.isEmpty()) {
            throw new IllegalArgumentException("Phone must contain digits");
        }

        StringBuilder expression = new StringBuilder("^\\D*");
        for (char digit : digits.toCharArray()) {
            expression.append(digit).append("\\D*");
        }

        return Pattern.compile(expression.append("$").toString());
    }
}
