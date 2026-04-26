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
 * By default the browser runs headlessly. Pass {@code headless=false} and a positive
 * {@code slowMoMillis} to launch a visible browser with slowed-down actions so a
 * human observer can follow what is happening (watch mode).
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
     * Initialises Playwright in headless mode with no slow-motion delay.
     * This is the standard mode used for unattended automation.
     */
    public BrowserSessionManager() {
        this(true, 0);
    }

    /**
     * Initialises Playwright with configurable headless and slow-motion settings.
     * @param headless {@code true} to run without a visible browser window (default);
     *                 {@code false} to open a visible browser window (watch mode)
     * @param slowMoMillis additional delay in milliseconds between Playwright actions;
     *                     use {@link AutomationConfig#WATCH_SLOW_MO_MILLIS} in watch mode
     */
    public BrowserSessionManager(boolean headless, int slowMoMillis) {
        if (headless) {
            LOGGER.info("BrowserSessionManager - starting Playwright and launching headless Chromium browser");
        } else {
            LOGGER.info("BrowserSessionManager - starting Playwright in WATCH MODE "
                    + "(visible browser, slow-motion delay: " + slowMoMillis + " ms)");
        }

        String sessionValue = resolveSessionCookie();

        this.playwright = Playwright.create();
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(headless);
        if (slowMoMillis > 0) {
            launchOptions.setSlowMo(slowMoMillis);
        }

        this.browser = playwright.chromium().launch(launchOptions);
        this.context = browser.newContext();

        Cookie sessionCookie = new Cookie(AutomationConfig.SESSION_COOKIE_NAME, sessionValue)
                .setDomain(AutomationConfig.SESSION_COOKIE_DOMAIN)
                .setPath("/")
                .setHttpOnly(true)
                .setSecure(true);

        context.addCookies(Collections.singletonList(sessionCookie));
        LOGGER.info("BrowserSessionManager - browser ready; AoC session cookie injected successfully");
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
            LOGGER.info("resolveSessionCookie - session cookie loaded from environment variable " + AutomationConfig.SESSION_ENV_VAR);
            return fromEnv.trim();
        }

        Path sessionFile = Paths.get(AutomationConfig.SESSION_FILE_PATH);
        if (Files.exists(sessionFile)) {
            try {
                String fromFile = Files.readString(sessionFile).trim();
                if (!fromFile.isBlank()) {
                    LOGGER.info("resolveSessionCookie - session cookie loaded from file: " + AutomationConfig.SESSION_FILE_PATH);
                    return fromFile;
                }
            } catch (IOException e) {
                LOGGER.warning("resolveSessionCookie - failed to read " + AutomationConfig.SESSION_FILE_PATH + ": " + e.getMessage());
            }
        }

        throw new IllegalStateException(
                "AoC session cookie not found. Set the " + AutomationConfig.SESSION_ENV_VAR
                + " environment variable or create a .aoc-session file in the project root."
        );
    }

    @Override
    public void close() {
        LOGGER.info("BrowserSessionManager - shutting down browser and releasing Playwright resources");
        context.close();
        browser.close();
        playwright.close();
    }
}

