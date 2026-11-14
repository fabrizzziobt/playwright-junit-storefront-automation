package com.rfbtinc.playwrightstorefrontautomation.model;

public record CheckoutState(
        ShippingAddress shippingAddress,
        boolean shippingAddressValidated,
        String shippingMethodValue,
        String shippingMethodLabel,
        boolean billingSameAsShipping,
        String paymentMethodValue
) {
}