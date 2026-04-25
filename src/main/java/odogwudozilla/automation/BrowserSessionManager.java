package odogwudozilla.automation;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.Cookie;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Manages the Playwright browser lifecycle and injects the AoC session cookie.
 *
 * Session cookie is resolved in this order:
 * 1. Environment variable {@code AOC_SESSION}
 * 2. File {@code .aoc-session} in the project root (one line, the cookie value)
 *
 * This class is {@link AutoCloseable} - use in a try-with-resources block to
 * ensure the browser and Playwright instance are properly shut down.
 */
public final class BrowserSessionManager implements AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(BrowserSessionManager.class.getName());

    private final Playwright playwright;
    private final Browser browser;
    private final BrowserContext context;

    /**
     * Initialises Playwright, launches a Chromium browser in headless mode,
     * and injects the resolved AoC session cookie into a new browser context.
     * @throws IllegalStateException if the session cookie cannot be resolved
     */
    public BrowserSessionManager() {
        LOGGER.info("BrowserSessionManager - initialising Playwright and browser");
        String sessionValue = resolveSessionCookie();

        this.playwright = Playwright.create();
        this.browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true)
        );
        this.context = browser.newContext();

        Cookie sessionCookie = new Cookie(AutomationConfig.SESSION_COOKIE_NAME, sessionValue)
                .setDomain(AutomationConfig.SESSION_COOKIE_DOMAIN)
                .setPath("/")
                .setHttpOnly(true)
                .setSecure(true);

        context.addCookies(Collections.singletonList(sessionCookie));
        LOGGER.info("BrowserSessionManager - session cookie injected successfully");
    }

    /**
     * Creates and returns a new {@link Page} within the authenticated browser context.
     * @return a new authenticated Playwright page
     */
    @NotNull
    public Page newPage() {
        return context.newPage();
    }

    /**
     * Resolves the AoC session cookie value from the environment variable
     * or the fallback file.
     * @return the session cookie value
     * @throws IllegalStateException if neither source provides a valid value
     */
    @NotNull
    private String resolveSessionCookie() {
        String fromEnv = System.getenv(AutomationConfig.SESSION_ENV_VAR);
        if (fromEnv != null && !fromEnv.isBlank()) {
            LOGGER.info("resolveSessionCookie - loaded session from environment variable");
            return fromEnv.trim();
        }

        Path sessionFile = Paths.get(AutomationConfig.SESSION_FILE_PATH);
        if (Files.exists(sessionFile)) {
            try {
                String fromFile = Files.readString(sessionFile).trim();
                if (!fromFile.isBlank()) {
                    LOGGER.info("resolveSessionCookie - loaded session from .aoc-session file");
                    return fromFile;
                }
            } catch (IOException e) {
                LOGGER.warning("resolveSessionCookie - failed to read .aoc-session file: " + e.getMessage());
            }
        }

        throw new IllegalStateException(
                "AoC session cookie not found. Set the " + AutomationConfig.SESSION_ENV_VAR
                + " environment variable or create a .aoc-session file in the project root."
        );
    }

    @Override
    public void close() {
        LOGGER.info("BrowserSessionManager - closing browser and Playwright");
        context.close();
        browser.close();
        playwright.close();
    }
}

