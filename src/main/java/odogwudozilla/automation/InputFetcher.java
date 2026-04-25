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
     * Creates intermediate directories if they do not exist.
     * @param info PuzzleInfo containing year and day
     * @throws IOException if the resource file cannot be written
     */
    public void fetchAndSave(@NotNull PuzzleInfo info) throws IOException {
        String inputUrl = info.getPuzzleUrl() + "/input";
        LOGGER.info("fetchAndSave - fetching input from " + inputUrl);

        String inputContent;
        try (Page page = sessionManager.newPage()) {
            page.navigate(inputUrl);
            // The input page renders as plain text - grab the full body text content
            inputContent = page.locator("body").textContent();
        }

        if (inputContent == null || inputContent.isBlank()) {
            throw new IOException("fetchAndSave - received empty input for " + info);
        }

        Path outputPath = buildOutputPath(info);
        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, inputContent.trim() + System.lineSeparator());

        LOGGER.info("fetchAndSave - input written to " + outputPath);
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

