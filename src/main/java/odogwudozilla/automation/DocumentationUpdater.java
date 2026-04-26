package odogwudozilla.automation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Updates project documentation after a puzzle is solved.
 *
 * Responsibilities:
 * - Writes solved puzzle metadata to {@code solutions_database.json}
 * - Updates (or creates) the year-specific README
 * - Updates the main README if a new year is encountered
 */
public final class DocumentationUpdater {

    private static final Logger LOGGER = Logger.getLogger(DocumentationUpdater.class.getName());

    private static final String YEAR_README_RELATIVE_PATH = "src/main/java/odogwudozilla/year%d/README.md";
    private static final String MAIN_README_PATH = "README.md";
    private static final String SOLUTIONS_ARRAY_KEY = "adventOfCodeSolutions";
    private static final String YEARS_SECTION_MARKER = "## Years";

    private final ObjectMapper objectMapper;

    /**
     * Creates a DocumentationUpdater with a default Jackson ObjectMapper.
     */
    public DocumentationUpdater() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Writes or updates the solutions database entry for the given puzzle and answers.
     * @param info PuzzleInfo with year, day, title, and URL
     * @param partOneAnswer the Part 1 answer string, or null if not yet solved
     * @param partTwoAnswer the Part 2 answer string, or null if not yet solved
     * @throws IOException if the solutions database cannot be read or written
     */
    public void updateSolutionsDatabase(@NotNull PuzzleInfo info,
                                        @Nullable String partOneAnswer,
                                        @Nullable String partTwoAnswer) throws IOException {
        Path dbPath = Paths.get(AutomationConfig.SOLUTIONS_DB_PATH).toAbsolutePath();
        LOGGER.info("updateSolutionsDatabase - updating " + dbPath);

        ObjectNode root = (ObjectNode) objectMapper.readTree(dbPath.toFile());
        ObjectNode solutions = (ObjectNode) root.get(SOLUTIONS_ARRAY_KEY);
        String yearKey = String.valueOf(info.getYear());

        if (solutions == null) {
            LOGGER.warning("updateSolutionsDatabase - solutions node not found in database");
            return;
        }

        ArrayNode yearArray = solutions.has(yearKey)
                ? (ArrayNode) solutions.get(yearKey)
                : solutions.putArray(yearKey);

        // Remove existing entry for this day if present
        for (int i = 0; i < yearArray.size(); i++) {
            if (yearArray.get(i).path("day").asInt() == info.getDay()) {
                yearArray.remove(i);
                break;
            }
        }

        ObjectNode solutionsNode = objectMapper.createObjectNode();
        if (partOneAnswer != null) {
            solutionsNode.put("partOne", partOneAnswer);
        }
        if (partTwoAnswer != null) {
            solutionsNode.put("partTwo", partTwoAnswer);
        }

        ObjectNode entry = objectMapper.createObjectNode();
        entry.put("year", info.getYear());
        entry.put("day", info.getDay());
        entry.put("title", info.getTitle() != null ? info.getTitle() : "");
        entry.put("link", info.getPuzzleUrl());
        entry.set("solutions", solutionsNode);

        yearArray.add(entry);

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(dbPath.toFile(), root);
        LOGGER.info("updateSolutionsDatabase - entry saved for day " + info.getDay());
    }

    /**
     * Updates (or creates) the year-specific README for this puzzle.
     * Does not include solution values - only metadata and links.
     * @param info PuzzleInfo with all metadata populated
     * @throws IOException if the README cannot be read or written
     */
    public void updateYearReadme(@NotNull PuzzleInfo info) throws IOException {
        Path readmePath = Paths.get(String.format(YEAR_README_RELATIVE_PATH, info.getYear()))
                .toAbsolutePath();
        LOGGER.info("updateYearReadme - updating " + readmePath);

        Files.createDirectories(readmePath.getParent());

        String content;
        if (!Files.exists(readmePath)) {
            content = buildNewYearReadme(info);
        } else {
            content = appendPuzzleToExistingReadme(Files.readString(readmePath), info);
        }

        Files.writeString(readmePath, content);
        LOGGER.info("updateYearReadme - README updated for " + info.getYear() + " Day " + info.getDay());
    }

    /**
     * Appends a new year entry to the main README Years section if one is not present.
     * @param info PuzzleInfo containing the year
     * @return true if the main README was modified, false if the year was already listed or not found
     * @throws IOException if the main README cannot be read or written
     */
    public boolean updateMainReadmeIfNewYear(@NotNull PuzzleInfo info) throws IOException {
        Path mainReadme = Paths.get(MAIN_README_PATH).toAbsolutePath();
        if (!Files.exists(mainReadme)) {
            LOGGER.warning("updateMainReadmeIfNewYear - main README not found at " + mainReadme);
            return false;
        }

        String content = Files.readString(mainReadme);
        String yearEntry = "- [" + info.getYear() + "](src/main/java/odogwudozilla/year"
                + info.getYear() + "/README.md)";

        if (content.contains(yearEntry)) {
            return false;
        }

        int insertIndex = content.indexOf(YEARS_SECTION_MARKER);
        if (insertIndex == -1) {
            LOGGER.warning("updateMainReadmeIfNewYear - '" + YEARS_SECTION_MARKER + "' marker not found in README; cannot insert year entry");
            return false;
        }

        int lineEnd = content.indexOf('\n', insertIndex);
        String updated = content.substring(0, lineEnd + 1)
                + yearEntry + "\n"
                + content.substring(lineEnd + 1);

        Files.writeString(mainReadme, updated);
        LOGGER.info("updateMainReadmeIfNewYear - added " + info.getYear() + " to main README Years section");
        return true;
    }

    /**
     * Builds the full content for a brand-new year-specific README.
     */
    @NotNull
    private String buildNewYearReadme(@NotNull PuzzleInfo info) {
        return "# Advent of Code " + info.getYear() + "\n\n"
                + "Solutions for Advent of Code " + info.getYear() + ".\n\n"
                + "[Back to main README](../../../../../README.md)\n\n"
                + "## Implemented Puzzles\n\n"
                + buildPuzzleEntry(info);
    }

    /**
     * Appends a puzzle entry to an existing year README, or updates an existing entry.
     */
    @NotNull
    private String appendPuzzleToExistingReadme(@NotNull String existing, @NotNull PuzzleInfo info) {
        String marker = "## Implemented Puzzles";
        if (!existing.contains(marker)) {
            return existing + "\n" + buildPuzzleEntry(info);
        }
        // Avoid duplicate entries
        String dayMarker = "### Day " + info.getDay();
        if (existing.contains(dayMarker)) {
            return existing;
        }
        int insertAt = existing.lastIndexOf('\n') + 1;
        return existing.substring(0, insertAt) + "\n" + buildPuzzleEntry(info);
    }

    /**
     * Builds a Markdown entry block for a single puzzle.
     */
    @NotNull
    private String buildPuzzleEntry(@NotNull PuzzleInfo info) {
        String sourceLink = "src/main/java/odogwudozilla/year" + info.getYear()
                + "/day" + info.getDay() + "/";
        return "### Day " + info.getDay() + ": " + info.getTitle() + "\n\n"
                + "- **Puzzle:** [" + info.getTitle() + "](" + info.getPuzzleUrl() + ")\n"
                + "- **Source:** [View code](" + sourceLink + ")\n\n";
    }
}

