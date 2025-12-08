package odogwudozilla;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for selecting a random unsolved Advent of Code puzzle.
 * Reads configuration and solutions database to identify available puzzles.
 * Uses simple regex-based JSON parsing to avoid external dependencies.
 */
public class PuzzleRandomizer {

    private static final String CONFIG_FILE = "/aoc_challenge_config.json";
    private static final String SOLUTIONS_FILE = "/solutions_database.json";
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    /**
     * Main method to select and display a random unsolved puzzle.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            PuzzleSelection selection = selectRandomUnsolvedPuzzle();
            if (selection != null) {
                System.out.println(selection.year + "," + selection.day);
            } else {
                System.out.println("No unsolved puzzles available");
            }
        } catch (IOException e) {
            System.err.println("Error selecting random puzzle: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Selects a random unsolved puzzle from all available puzzles.
     * @return PuzzleSelection containing year and day, or null if no unsolved puzzles exist
     */
    @Nullable
    public static PuzzleSelection selectRandomUnsolvedPuzzle() throws IOException {
        // Read configuration file
        String configContent = readResourceFile(CONFIG_FILE);
        if (configContent == null) {
            throw new IOException("Configuration file not found: " + CONFIG_FILE);
        }

        // Read solutions database
        String solutionsContent = readResourceFile(SOLUTIONS_FILE);
        if (solutionsContent == null) {
            throw new IOException("Solutions database not found: " + SOLUTIONS_FILE);
        }

        // Build set of solved puzzles
        Set<PuzzleKey> solved = buildSolvedSet(solutionsContent);

        // Build list of unsolved puzzles
        List<PuzzleSelection> unsolved = buildUnsolvedList(configContent, solved);

        // Select random puzzle
        if (unsolved.isEmpty()) {
            return null;
        }

        int randomIndex = RANDOM.nextInt(unsolved.size());
        return unsolved.get(randomIndex);
    }

    /**
     * Reads a resource file and returns its content as a string.
     * @param resourcePath path to the resource file
     * @return content of the file, or null if not found
     */
    @Nullable
    private static String readResourceFile(@NotNull String resourcePath) throws IOException {
        try (InputStream stream = PuzzleRandomizer.class.getResourceAsStream(resourcePath)) {
            if (stream == null) {
                return null;
            }
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
            return content.toString();
        }
    }

    /**
     * Builds a set of all solved puzzles from the solutions database.
     * @param solutionsJson JSON string containing solutions database
     * @return Set of PuzzleKey objects representing solved puzzles
     */
    @NotNull
    private static Set<PuzzleKey> buildSolvedSet(@NotNull String solutionsJson) {
        Set<PuzzleKey> solved = new HashSet<>();

        // Pattern to match year sections: "2015": [ ... ]
        Pattern yearPattern = Pattern.compile("\"(\\d{4})\"\\s*:\\s*\\[");
        // Pattern to match day values: "day": 1
        Pattern dayPattern = Pattern.compile("\"day\"\\s*:\\s*(\\d+)");

        Matcher yearMatcher = yearPattern.matcher(solutionsJson);

        while (yearMatcher.find()) {
            int year = Integer.parseInt(yearMatcher.group(1));
            int yearStart = yearMatcher.end();

            // Find the end of this year's array
            int yearEnd = findMatchingBracket(solutionsJson, yearStart - 1);
            if (yearEnd == -1) continue;

            String yearSection = solutionsJson.substring(yearStart, yearEnd);
            Matcher dayMatcher = dayPattern.matcher(yearSection);

            while (dayMatcher.find()) {
                int day = Integer.parseInt(dayMatcher.group(1));
                solved.add(new PuzzleKey(year, day));
            }
        }

        return solved;
    }

    /**
     * Finds the matching closing bracket for an opening bracket.
     * @param text text to search in
     * @param openBracketPos position of the opening bracket
     * @return position of matching closing bracket, or -1 if not found
     */
    private static int findMatchingBracket(@NotNull String text, int openBracketPos) {
        if (openBracketPos >= text.length() || text.charAt(openBracketPos) != '[') {
            return -1;
        }

        int depth = 1;
        for (int i = openBracketPos + 1; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '[') {
                depth++;
            } else if (c == ']') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Builds a list of all unsolved puzzles from the configuration.
     * @param configJson JSON string containing puzzle configuration
     * @param solved Set of already solved puzzles
     * @return List of PuzzleSelection objects representing unsolved puzzles
     */
    @NotNull
    private static List<PuzzleSelection> buildUnsolvedList(@NotNull String configJson, @NotNull Set<PuzzleKey> solved) {
        List<PuzzleSelection> unsolved = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();

        // Pattern to match year and totalDays: "2015": { ... "totalDays": 25 ... }
        Pattern yearPattern = Pattern.compile("\"(\\d{4})\"\\s*:\\s*\\{");
        Pattern totalDaysPattern = Pattern.compile("\"totalDays\"\\s*:\\s*(\\d+)");

        Matcher yearMatcher = yearPattern.matcher(configJson);

        while (yearMatcher.find()) {
            int year = Integer.parseInt(yearMatcher.group(1));
            int yearStart = yearMatcher.end();

            // Find the end of this year's object
            int yearEnd = findMatchingBrace(configJson, yearStart - 1);
            if (yearEnd == -1) continue;

            String yearSection = configJson.substring(yearStart, yearEnd);
            Matcher totalDaysMatcher = totalDaysPattern.matcher(yearSection);

            if (totalDaysMatcher.find()) {
                int totalDays = Integer.parseInt(totalDaysMatcher.group(1));

                for (int day = 1; day <= totalDays; day++) {
                    LocalDate puzzleDate = LocalDate.of(year, 12, day);

                    // Skip future puzzles
                    if (puzzleDate.isAfter(currentDate)) {
                        continue;
                    }

                    PuzzleKey key = new PuzzleKey(year, day);
                    if (!solved.contains(key)) {
                        unsolved.add(new PuzzleSelection(year, day));
                    }
                }
            }
        }

        return unsolved;
    }

    /**
     * Finds the matching closing brace for an opening brace.
     * @param text text to search in
     * @param openBracePos position of the opening brace
     * @return position of matching closing brace, or -1 if not found
     */
    private static int findMatchingBrace(@NotNull String text, int openBracePos) {
        if (openBracePos >= text.length() || text.charAt(openBracePos) != '{') {
            return -1;
        }

        int depth = 1;
        for (int i = openBracePos + 1; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '{') {
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Internal class representing a puzzle key for solved set lookup.
     */
    private static class PuzzleKey {
        final int year;
        final int day;

        PuzzleKey(int year, int day) {
            this.year = year;
            this.day = day;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PuzzleKey that = (PuzzleKey) o;
            return year == that.year && day == that.day;
        }

        @Override
        public int hashCode() {
            return 31 * year + day;
        }
    }

    /**
     * Class representing a selected puzzle with year and day.
     */
    public static class PuzzleSelection {
        public final int year;
        public final int day;

        public PuzzleSelection(int year, int day) {
            this.year = year;
            this.day = day;
        }

        @Override
        public String toString() {
            return "Year " + year + ", Day " + day;
        }
    }
}

