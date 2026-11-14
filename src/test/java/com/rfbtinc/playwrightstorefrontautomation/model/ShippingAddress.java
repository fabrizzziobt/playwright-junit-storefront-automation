package com.rfbtinc.playwrightstorefrontautomation.model;

public record ShippingAddress(
        String firstName,
        String lastName,
        String addressLine1,
        String addressLine2,
        String city,
        String stateOrProvince,
        String postalCode,
        String countryCode,
        String phone
) {
}
