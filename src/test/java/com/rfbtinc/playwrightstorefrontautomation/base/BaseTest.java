package com.rfbtinc.playwrightstorefrontautomation.base;

import com.microsoft.playwright.*;
import com.rfbtinc.playwrightstorefrontautomation.config.TestEnvironment;
import com.rfbtinc.playwrightstorefrontautomation.utils.ConfigManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;
    protected TestEnvironment environment;

    @BeforeEach
    void setUp() {
        environment = new TestEnvironment();
        playwright = Playwright.create();

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(ConfigManager.headless());

        browser = switch (ConfigManager.browser().toLowerCase()) {
            case "firefox" -> playwright.firefox().launch(launchOptions);
            case "webkit" -> playwright.webkit().launch(launchOptions);
            default -> playwright.chromium().launch(launchOptions);
        };

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", ConfigManager.userAgent());
        headers.put("x-qa-testing", ConfigManager.botProtectionBypassHeader());

        context = browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1440, 900)
                .setBaseURL(environment.getBaseDomain())
                .setLocale(environment.getBrowserLocale())
                .setUserAgent( ConfigManager.userAgent())
                .setExtraHTTPHeaders(headers));

        page = context.newPage();
        page.setDefaultTimeout(ConfigManager.timeoutMs());
    }

    @AfterEach
    void tearDown() {
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
