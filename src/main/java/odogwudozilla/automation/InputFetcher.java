package odogwudozilla.automation;

import com.microsoft.playwright.Page;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Fetches the raw puzzle input for a given year and day from Advent of Code,
 * then writes it to the appropriate resource file in the project.
 *
 * The input is written to:
 * {@code src/main/resources/<year>/day<day>/day<day>_puzzle_data.txt}
 */
public final class InputFetcher {

    private static final Logger LOGGER = Logger.getLogger(InputFetcher.class.getName());

    private final BrowserSessionManager sessionManager;

    /**
     * Creates an InputFetcher using the provided session manager.
     * @param sessionManager the authenticated browser session manager
     */
    public InputFetcher(@NotNull BrowserSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Fetches the puzzle input from AoC and writes it to the resource file.
     * If the puzzle carries an inline input (detected during description scraping), that is used
     * directly and the network request to {@code /input} is skipped entirely.
     * Creates intermediate directories if they do not exist.
     * @param info PuzzleInfo containing year, day, and optional inline input
     * @throws IOException if the resource file cannot be written, or if the AoC session
     *                     cookie is detected as expired or invalid
     */
    public void fetchAndSave(@NotNull PuzzleInfo info) throws IOException {
        // Fast path: use inline input detected during description scraping
        if (info.getInlineInput() != null) {
            LOGGER.info("fetchAndSave - using inline input found on description page (skipping /input URL)");
            writeInputToFile(info.getInlineInput(), info);
            return;
        }

        String inputUrl = info.getPuzzleUrl() + "/input";
        LOGGER.info("fetchAndSave - requesting puzzle input from AoC: " + inputUrl);

        String inputContent;
        try (Page page = sessionManager.newPage()) {
            page.navigate(inputUrl);
            inputContent = page.locator("body").textContent();
        }

        if (inputContent == null || inputContent.isBlank()) {
            throw new IOException("fetchAndSave - AoC returned an empty input for " + info
                    + ". The session cookie may be invalid or expired.");
        }

        // Detect expired/invalid session cookie
        if (inputContent.contains(AutomationConfig.SESSION_EXPIRED_SIGNAL)) {
            throw new IOException(
                    "[ACTION REQUIRED] AoC session cookie is expired or invalid.\n"
                    + "  The /input endpoint returned: \"" + AutomationConfig.SESSION_EXPIRED_SIGNAL + "...\"\n"
                    + "  - Log in to https://adventofcode.com\n"
                    + "  - Open DevTools (F12) -> Application -> Cookies -> copy the 'session' value\n"
                    + "  - Paste it into your .aoc-session file in the project root\n"
                    + "  - Re-run the same command - the pending state will resume automatically"
            );
        }

        writeInputToFile(inputContent, info);
    }

    /**
     * Trims and writes the given input content to the puzzle data file, creating directories
     * as needed. Logs the number of lines saved.
     * @param content the raw input content to write
     * @param info PuzzleInfo used to resolve the output path
     * @throws IOException if the file cannot be written
     */
    private void writeInputToFile(@NotNull String content, @NotNull PuzzleInfo info) throws IOException {
        String trimmed = content.trim();
        Path outputPath = buildOutputPath(info);
        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, trimmed + System.lineSeparator());
        LOGGER.info("fetchAndSave - puzzle input saved (" + trimmed.lines().count()
                + " lines) -> " + outputPath);
    }

    /**
     * Resolves the output file path for the puzzle input data file.
     * @param info PuzzleInfo containing year and day
     * @return absolute Path to the data file
     */
    @NotNull
    private Path buildOutputPath(@NotNull PuzzleInfo info) {
        return Paths.get(AutomationConfig.RESOURCES_BASE_PATH)
                .resolve(String.valueOf(info.getYear()))
                .resolve("day" + info.getDay())
                .resolve("day" + info.getDay() + "_puzzle_data.txt")
                .toAbsolutePath();
    }
}

