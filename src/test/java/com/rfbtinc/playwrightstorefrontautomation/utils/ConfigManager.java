package com.rfbtinc.playwrightstorefrontautomation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads test runtime configuration from test.properties and system properties.
 */
public final class ConfigManager {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config/test.properties")) {
            if (input == null) {
                throw new IllegalStateException("config/test.properties not found");
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load test.properties", e);
        }
    }

    private ConfigManager() {
    }

    public static String browser() {
        return System.getProperty("browser", PROPERTIES.getProperty("browser", "chromium"));
    }

    public static boolean headless() {
        return Boolean.parseBoolean(
                System.getProperty("headless", PROPERTIES.getProperty("headless", "true")));
    }

    public static double timeoutMs() {
        return Double.parseDouble(PROPERTIES.getProperty("default.timeout.ms", "15000"));
    }

    public static String locale() {
        return System.getProperty("locale", PROPERTIES.getProperty("default.locale", "en_US"));
    }

    public static String userAgent() {
        return PROPERTIES.getProperty("user.agentstring", "");
    }   

    public static String botProtectionBypassHeader() {
        return PROPERTIES.getProperty("header.token", "");
    }

    public static String domainForLocale(String locale) {
        return required("domain." + locale);
    }

    public static String productFileForLocale(String locale) {
        return required("product.data.file." + locale);
    }

    public static String browserLocaleForLocale(String locale) {
        return PROPERTIES.getProperty("browser.locale." + locale, locale.replace('_', '-'));
    }

    private static String required(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing required config key: " + key);
        }
        return value;
    }
}
