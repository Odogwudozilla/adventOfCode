package odogwudozilla.automation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable data holder for all metadata about a selected AoC puzzle.
 * Populated progressively as the automation pipeline advances.
 */
public final class PuzzleInfo {

    private final int year;
    private final int day;

    @Nullable
    private final String title;

    @Nullable
    private final String partOneDescription;

    @Nullable
    private final String partTwoDescription;

    @NotNull
    private final String puzzleUrl;

    /**
     * Puzzle input detected inline on the description page (e.g. "Your puzzle input is X.").
     * Null when the input must be fetched from the dedicated {@code /input} URL.
     */
    @Nullable
    private final String inlineInput;

    /**
     * Constructs a PuzzleInfo with all fields.
     * @param year the puzzle year
     * @param day the puzzle day
     * @param title the puzzle title extracted from the AoC page
     * @param partOneDescription plain-text Part 1 description
     * @param partTwoDescription plain-text Part 2 description, or null if not yet available
     * @param puzzleUrl the canonical AoC URL for this puzzle
     * @param inlineInput puzzle input found inline on the description page, or null
     */
    public PuzzleInfo(int year,
                      int day,
                      @Nullable String title,
                      @Nullable String partOneDescription,
                      @Nullable String partTwoDescription,
                      @NotNull String puzzleUrl,
                      @Nullable String inlineInput) {
        this.year = year;
        this.day = day;
        this.title = title;
        this.partOneDescription = partOneDescription;
        this.partTwoDescription = partTwoDescription;
        this.puzzleUrl = puzzleUrl;
        this.inlineInput = inlineInput;
    }

    /**
     * Convenience factory for a minimal PuzzleInfo created before scraping occurs.
     * @param year the puzzle year
     * @param day the puzzle day
     * @return a PuzzleInfo with only year, day, and derived URL populated
     */
    @NotNull
    public static PuzzleInfo minimal(int year, int day) {
        String url = AutomationConfig.AOC_BASE_URL + "/" + year + "/day/" + day;
        return new PuzzleInfo(year, day, null, null, null, url, null);
    }

    /**
     * Returns a copy of this PuzzleInfo with an updated title and Part 1 description.
     * @param newTitle the scraped puzzle title
     * @param newPartOneDescription the scraped Part 1 description text
     * @return updated PuzzleInfo
     */
    @NotNull
    public PuzzleInfo withPartOne(@NotNull String newTitle, @NotNull String newPartOneDescription) {
        return new PuzzleInfo(year, day, newTitle, newPartOneDescription, partTwoDescription, puzzleUrl, inlineInput);
    }

    /**
     * Returns a copy of this PuzzleInfo with an updated Part 2 description.
     * @param newPartTwoDescription the scraped Part 2 description text
     * @return updated PuzzleInfo
     */
    @NotNull
    public PuzzleInfo withPartTwo(@NotNull String newPartTwoDescription) {
        return new PuzzleInfo(year, day, title, partOneDescription, newPartTwoDescription, puzzleUrl, inlineInput);
    }

    /**
     * Returns a copy of this PuzzleInfo with the inline input set.
     * Used when the puzzle input is embedded on the description page rather than
     * available at the dedicated {@code /input} URL.
     * @param input the inline puzzle input extracted from the description page
     * @return updated PuzzleInfo
     */
    @NotNull
    public PuzzleInfo withInlineInput(@NotNull String input) {
        return new PuzzleInfo(year, day, title, partOneDescription, partTwoDescription, puzzleUrl, input);
    }

    public int getYear() {
        return year;
    }

    public int getDay() {
        return day;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getPartOneDescription() {
        return partOneDescription;
    }

    @Nullable
    public String getPartTwoDescription() {
        return partTwoDescription;
    }

    @NotNull
    public String getPuzzleUrl() {
        return puzzleUrl;
    }

    @Nullable
    public String getInlineInput() {
        return inlineInput;
    }

    @Override
    public String toString() {
        return "PuzzleInfo{year=" + year + ", day=" + day + ", title='" + title + "'}";
    }
}

