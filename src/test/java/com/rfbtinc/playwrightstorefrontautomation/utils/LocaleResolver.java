package com.rfbtinc.playwrightstorefrontautomation.utils;

/**
 * Encapsulates locale-specific configuration lookup.
 */
public final class LocaleResolver {

    private LocaleResolver() {
    }

    public static String effectiveLocale() {
        return ConfigManager.locale();
    }

    public static String baseDomain() {
        return ConfigManager.domainForLocale(effectiveLocale());
    }

    public static String productDataFile() {
        return ConfigManager.productFileForLocale(effectiveLocale());
    }

    public static String browserLocale() {
        return ConfigManager.browserLocaleForLocale(effectiveLocale());
    }
}
