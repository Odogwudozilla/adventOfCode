package odogwudozilla.automation;

import com.microsoft.playwright.Page;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Scrapes puzzle metadata from Advent of Code puzzle pages.
 *
 * Extracts the puzzle title and description text for Part 1 and - when available -
 * Part 2 from the authenticated Playwright page.
 */
public final class PuzzleScraper {

    private static final Logger LOGGER = Logger.getLogger(PuzzleScraper.class.getName());

    /** Regex to extract the puzzle title from the heading "--- Day N: Title ---". */
    private static final Pattern TITLE_PATTERN = Pattern.compile("---\\s*Day\\s+\\d+:\\s*(.+?)\\s*---");

    private final BrowserSessionManager sessionManager;

    /**
     * Creates a PuzzleScraper using the provided session manager.
     * @param sessionManager the authenticated browser session manager
     */
    public PuzzleScraper(@NotNull BrowserSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Navigates to the puzzle page and scrapes the title and Part 1 description.
     * If Part 2 is already unlocked on the same page, it is scraped as well.
     * Also checks for an inline puzzle input embedded on the page (e.g. "Your puzzle
     * input is X.") and stores it in the returned PuzzleInfo when found.
     * @param info minimal PuzzleInfo containing year and day
     * @return updated PuzzleInfo with title, Part 1 (and optionally Part 2) descriptions,
     *         and inline input if detected
     * @throws IOException if the session cookie appears to be expired or the page cannot be loaded
     */
    @NotNull
    public PuzzleInfo scrapePartOne(@NotNull PuzzleInfo info) throws IOException {
        String url = info.getPuzzleUrl();
        LOGGER.info("scrapePartOne - opening AoC puzzle page: " + url);

        try (Page page = sessionManager.newPage()) {
            page.navigate(url);

            // Guard: if AoC redirected away from the expected puzzle URL, the session is expired.
            String landingUrl = page.url();
            if (!landingUrl.startsWith(url)) {
                throw new IOException(
                        "[ACTION REQUIRED] AoC session cookie is expired or invalid.\n"
                        + "  Browser was redirected to: " + landingUrl + "\n"
                        + "  Expected puzzle URL prefix: " + url + "\n"
                        + "  - Log in to https://adventofcode.com\n"
                        + "  - Open DevTools (F12) -> Application -> Cookies -> copy the 'session' value\n"
                        + "  - Paste it into your .aoc-session file in the project root\n"
                        + "  - Re-run the same command - the pending state will resume automatically"
                );
            }

            page.waitForSelector(AutomationConfig.PUZZLE_ARTICLE_SELECTOR);

            String titleHeading = page.locator(AutomationConfig.PUZZLE_TITLE_SELECTOR)
                    .first()
                    .textContent();
            String title = extractTitleFromHeading(titleHeading, info.getDay());

            var articles = page.locator(AutomationConfig.PUZZLE_ARTICLE_SELECTOR).all();

            String partOneText = articles.isEmpty() ? "" : articles.get(0).innerText();
            LOGGER.info("scrapePartOne - successfully scraped Part 1 description for: \"" + title + "\"");

            PuzzleInfo updated = info.withPartOne(title, partOneText);

            if (articles.size() > 1) {
                String partTwoText = articles.get(1).innerText();
                LOGGER.info("scrapePartOne - Part 2 is already unlocked on the page; scraped both parts in one request");
                updated = updated.withPartTwo(partTwoText);
            }

            // Look for an inline puzzle input on the description page (e.g. "Your puzzle input is X.")
            String inlineInput = extractInlineInput(page);
            if (inlineInput != null) {
                LOGGER.info("scrapePartOne - detected inline puzzle input on description page: \"" + inlineInput + "\"");
                updated = updated.withInlineInput(inlineInput);
            }

            return updated;
        }
    }

    /**
     * Navigates to the puzzle page and scrapes the Part 2 description.
     * Call this after Part 1 has been solved and submitted correctly.
     * @param info PuzzleInfo already containing Part 1 data
     * @return updated PuzzleInfo with Part 2 description added
     * @throws IOException if the session cookie appears to be expired or the page cannot be loaded
     */
    @NotNull
    public PuzzleInfo scrapePartTwo(@NotNull PuzzleInfo info) throws IOException {
        String url = info.getPuzzleUrl();
        LOGGER.info("scrapePartTwo - re-opening puzzle page to check for Part 2: " + url);

        try (Page page = sessionManager.newPage()) {
            page.navigate(url);
            page.waitForSelector(AutomationConfig.PUZZLE_ARTICLE_SELECTOR);

            var articles = page.locator(AutomationConfig.PUZZLE_ARTICLE_SELECTOR).all();

            if (articles.size() < 2) {
                LOGGER.warning("scrapePartTwo - Part 2 is not yet unlocked for " + info.getYear() + " Day " + info.getDay() + "; only " + articles.size() + " article(s) found on page");
                return info;
            }

            String partTwoText = articles.get(1).innerText();
            LOGGER.info("scrapePartTwo - Part 2 description scraped for " + info.getYear() + " Day " + info.getDay());
            return info.withPartTwo(partTwoText);
        }
    }

    /**
     * Attempts to extract an inline puzzle input from the AoC description page.
     *
     * <p>Strategy 1 - paragraph with code element: looks for a {@code <p>} containing the text
     * "Your puzzle input is" and returns the text of the first {@code <code>} child element.</p>
     *
     * <p>Strategy 2 - plain paragraph text: if no {@code <code>} element is found, extracts the
     * text following the marker phrase directly from the paragraph (e.g. "Your puzzle input is 347991.").</p>
     *
     * <p>Strategy 3 - bare pre block: looks for a {@code <pre>} element that is a direct child
     * of {@code <main>} (i.e. outside any {@code <article>}), which some puzzles use for
     * multi-line inline inputs.</p>
     *
     * @param page the already-navigated Playwright page
     * @return the extracted inline input string, or null if no inline input is detected
     */
    @Nullable
    private String extractInlineInput(@NotNull Page page) {
        // Strategy 1 & 2: <p>Your puzzle input is <code>VALUE</code></p> or plain text
        try {
            var paraLocator = page.locator(
                    "xpath=//p[contains(., '" + AutomationConfig.INLINE_INPUT_TEXT_MARKER + "')]");
            if (paraLocator.count() > 0) {
                // Try to get the <code> child element value first (more precise)
                var codeLocator = paraLocator.first()
                        .locator("xpath=.//code");
                if (codeLocator.count() > 0) {
                    String codeText = codeLocator.first().textContent();
                    if (codeText != null && !codeText.isBlank()) {
                        return codeText.trim();
                    }
                }
                // Fallback: extract plain text after the marker phrase
                String paraText = paraLocator.first().textContent();
                if (paraText != null) {
                    int markerIndex = paraText.indexOf(AutomationConfig.INLINE_INPUT_TEXT_MARKER);
                    if (markerIndex != -1) {
                        String afterMarker = paraText
                                .substring(markerIndex + AutomationConfig.INLINE_INPUT_TEXT_MARKER.length())
                                .trim();
                        if (afterMarker.endsWith(".")) {
                            afterMarker = afterMarker.substring(0, afterMarker.length() - 1).trim();
                        }
                        if (!afterMarker.isBlank()) {
                            return afterMarker;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.fine("extractInlineInput - paragraph strategy failed: " + e.getMessage());
        }

        // Strategy 3: bare <pre> block that is a direct child of <main> (outside any article)
        try {
            Object preText = page.evaluate(
                    "() => { const el = document.querySelector('main > pre'); "
                    + "return el ? el.textContent : null; }");
            if (preText instanceof String preString && !preString.isBlank()) {
                return preString.trim();
            }
        } catch (Exception e) {
            LOGGER.fine("extractInlineInput - pre-block strategy failed: " + e.getMessage());
        }

        return null;
    }

    /**
     * Extracts the human-readable title from an AoC heading string such as
     * "--- Day 1: Not Quite Lisp ---".
     * @param heading the raw heading text from the page
     * @param day the day number used as fallback if parsing fails
     * @return the extracted title, or a generated fallback title
     */
    @NotNull
    private String extractTitleFromHeading(@Nullable String heading, int day) {
        if (heading == null || heading.isBlank()) {
            return "Day" + day;
        }
        Matcher matcher = TITLE_PATTERN.matcher(heading.trim());
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        LOGGER.warning("extractTitleFromHeading - could not parse title from: " + heading);
        return "Day" + day;
    }
}

