package odogwudozilla.automation;

/**
 * Central configuration constants for the AoC automation pipeline.
 * All paths, URLs, timeouts, and signal strings are defined here.
 */
public final class AutomationConfig {

    /** Base URL for Advent of Code. */
    public static final String AOC_BASE_URL = "https://adventofcode.com";

    /** Name of the environment variable that holds the AoC session cookie value. */
    public static final String SESSION_ENV_VAR = "AOC_SESSION";

    /** Fallback file (project root) storing the session cookie value, one line. */
    public static final String SESSION_FILE_PATH = ".aoc-session";

    /** Name of the AoC session cookie as set in the browser. */
    public static final String SESSION_COOKIE_NAME = "session";

    /** Domain used when injecting the session cookie into Playwright. */
    public static final String SESSION_COOKIE_DOMAIN = ".adventofcode.com";

    /** Minimum delay in milliseconds between rate-limited requests (60 seconds). */
    public static final long RATE_LIMIT_MILLIS = 60_000L;

    /** Maximum number of answer submission retry attempts before giving up. */
    public static final int MAX_RETRY_ATTEMPTS = 3;

    /** Base path for Java source files (relative to project root). */
    public static final String JAVA_BASE_PATH = "src/main/java";

    /** Base path for resource files (relative to project root). */
    public static final String RESOURCES_BASE_PATH = "src/main/resources";

    /** Relative path to the solutions database JSON file. */
    public static final String SOLUTIONS_DB_PATH = "src/main/resources/solutions_database.json";

    /** Root Java package for all puzzle solutions. */
    public static final String PACKAGE_BASE = "odogwudozilla";

    /** Gradle wrapper executable name on Windows. */
    public static final String GRADLE_WRAPPER_WINDOWS = "gradlew.bat";

    /** Gradle wrapper executable name on Unix/macOS. */
    public static final String GRADLE_WRAPPER_UNIX = "./gradlew";

    /** String present in AoC response when answer is correct. */
    public static final String CORRECT_ANSWER_SIGNAL = "That's the right answer!";

    /** String present in AoC response when answer is wrong. */
    public static final String WRONG_ANSWER_SIGNAL = "That's not the right answer";

    /** String present in AoC response when submitting too soon. */
    public static final String TOO_SOON_SIGNAL = "You gave an answer too recently";

    /** String present in AoC response when the level is already complete or wrong level. */
    public static final String ALREADY_SOLVED_SIGNAL = "You don't seem to be solving the right level";

    /**
     * String present on the main puzzle page (not submission response) when both parts
     * of the puzzle have already been completed. Used as a pre-submission guard.
     */
    public static final String ALREADY_COMPLETE_SIGNAL = "Both parts of this puzzle are complete!";

    /** Prefix used in puzzle stdout for Part 1 answer capture. */
    public static final String PART_ONE_OUTPUT_PREFIX = "Part 1:";

    /** Prefix used in puzzle stdout for Part 2 answer capture. */
    public static final String PART_TWO_OUTPUT_PREFIX = "Part 2:";

    /** Answer value printed by the skeleton stub - must never be submitted. */
    public static final String STUB_ANSWER_VALUE = "not implemented";

    /** Selector for the puzzle description article on AoC pages. */
    public static final String PUZZLE_ARTICLE_SELECTOR = "article.day-desc";

    /** Selector for the puzzle description heading containing the title. */
    public static final String PUZZLE_TITLE_SELECTOR = "article.day-desc h2";

    /** Selector for the answer submission input field. */
    public static final String ANSWER_INPUT_SELECTOR = "input[name='answer']";

    /** Selector for the answer submission form. */
    public static final String ANSWER_FORM_SELECTOR = "form[method='post']";

    /** Selector for the main article on the post-submission response page. */
    public static final String RESPONSE_ARTICLE_SELECTOR = "article";

    // -------------------------------------------------------------------------
    // Watch mode (non-headless + slow-motion)
    // -------------------------------------------------------------------------

    /** CLI flag that enables watch mode: non-headless browser with slow-motion actions. */
    public static final String FLAG_WATCH = "--watch";

    /**
     * Slow-motion delay in milliseconds applied between Playwright actions in watch mode.
     * At 3000 ms each action is clearly visible and readable to a human observer.
     */
    public static final int WATCH_SLOW_MO_MILLIS = 3_000;

    private AutomationConfig() {
        // Utility class - not instantiable
    }
}

