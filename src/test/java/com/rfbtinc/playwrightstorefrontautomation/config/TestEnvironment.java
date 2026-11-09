package com.rfbtinc.playwrightstorefrontautomation.config;

import com.rfbtinc.playwrightstorefrontautomation.utils.LocaleResolver;

/**
 * Value object representing the current runtime test environment.
 */
public class TestEnvironment {
    private final String locale;
    private final String baseDomain;
    private final String browserLocale;
    private final String productDataFile;

    public TestEnvironment() {
        this.locale = LocaleResolver.effectiveLocale();
        this.baseDomain = LocaleResolver.baseDomain();
        this.browserLocale = LocaleResolver.browserLocale();
        this.productDataFile = LocaleResolver.productDataFile();
    }

    public String getLocale() {
        return locale;
    }

    public String getBaseDomain() {
        return baseDomain;
    }

    public String getBrowserLocale() {
        return browserLocale;
    }

    public String getProductDataFile() {
        return productDataFile;
    }
}
