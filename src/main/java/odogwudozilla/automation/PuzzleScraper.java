package odogwudozilla.automation;

import com.microsoft.playwright.Page;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * @param info minimal PuzzleInfo containing year and day
     * @return updated PuzzleInfo with title and Part 1 (and optionally Part 2) descriptions
     */
    @NotNull
    public PuzzleInfo scrapePartOne(@NotNull PuzzleInfo info) {
        String url = info.getPuzzleUrl();
        LOGGER.info("scrapePartOne - navigating to " + url);

        try (Page page = sessionManager.newPage()) {
            page.navigate(url);
            page.waitForSelector(AutomationConfig.PUZZLE_ARTICLE_SELECTOR);

            String titleHeading = page.locator(AutomationConfig.PUZZLE_TITLE_SELECTOR)
                    .first()
                    .textContent();
            String title = extractTitleFromHeading(titleHeading, info.getDay());

            // Collect all article elements - first is Part 1, second (if present) is Part 2
            var articles = page.locator(AutomationConfig.PUZZLE_ARTICLE_SELECTOR).all();

            String partOneText = articles.isEmpty() ? "" : articles.get(0).innerText();
            LOGGER.info("scrapePartOne - scraped Part 1 for: " + title);

            PuzzleInfo updated = info.withPartOne(title, partOneText);

            if (articles.size() > 1) {
                String partTwoText = articles.get(1).innerText();
                LOGGER.info("scrapePartOne - Part 2 also available on page, scraping now");
                updated = updated.withPartTwo(partTwoText);
            }

            return updated;
        }
    }

    /**
     * Navigates to the puzzle page and scrapes the Part 2 description.
     * Call this after Part 1 has been solved and submitted correctly.
     * @param info PuzzleInfo already containing Part 1 data
     * @return updated PuzzleInfo with Part 2 description added
     */
    @NotNull
    public PuzzleInfo scrapePartTwo(@NotNull PuzzleInfo info) {
        String url = info.getPuzzleUrl();
        LOGGER.info("scrapePartTwo - navigating to " + url);

        try (Page page = sessionManager.newPage()) {
            page.navigate(url);
            page.waitForSelector(AutomationConfig.PUZZLE_ARTICLE_SELECTOR);

            var articles = page.locator(AutomationConfig.PUZZLE_ARTICLE_SELECTOR).all();

            if (articles.size() < 2) {
                LOGGER.warning("scrapePartTwo - Part 2 not yet available on page");
                return info;
            }

            String partTwoText = articles.get(1).innerText();
            LOGGER.info("scrapePartTwo - scraped Part 2 for day " + info.getDay());
            return info.withPartTwo(partTwoText);
        }
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

