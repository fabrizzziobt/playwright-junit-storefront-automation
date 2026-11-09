package com.rfbtinc.playwrightstorefrontautomation.utils;

import com.microsoft.playwright.Page;

public final class CloudflareDetector {

    private CloudflareDetector() {
    }

    public static boolean isChallengePage(Page page) {
        String title = page.title().toLowerCase();
        String content = page.content().toLowerCase();

        return title.contains("just a moment")
                || content.contains("checking your browser")
                || content.contains("cf-challenge")
                || content.contains("cloudflare")
                || page.locator("#challenge-form").count() > 0
                || page.locator("iframe[src*='captcha']").count() > 0;
    }

    public static void failIfBlocked(Page page) {
        if (isChallengePage(page)) {
            throw new IllegalStateException(
                    "Cloudflare challenge detected. This environment is not automation-ready. " +
                    "Use allowlisting, bypass rules, or a dedicated test access path.");
        }
    }
}
