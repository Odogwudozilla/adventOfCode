package odogwudozilla;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
     * Selects a random unsolved puzzle from all available puzzles with prioritisation logic.
     * Priority order:
     * 1. If there is a year with zero solved days, randomly pick one and return day 1
     * 2. Otherwise, randomly select a year and return a random unsolved day from that year
     *
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

        // Build map of solved puzzles by year
        Map<Integer, Set<Integer>> solvedByYear = buildSolvedSetByYear(solutionsContent);

        // Extract all available years from config
        List<Integer> availableYears = extractAvailableYears(configContent);
        if (availableYears.isEmpty()) {
            return null;
        }

        // Find years with zero solved days
        List<Integer> unsolvedYears = new ArrayList<>();
        for (Integer year : availableYears) {
            if (!solvedByYear.containsKey(year) || solvedByYear.get(year).isEmpty()) {
                unsolvedYears.add(year);
            }
        }

        if (!unsolvedYears.isEmpty()) {
            // Priority 1: Randomly select a year with no solved days
            int selectedYear = unsolvedYears.get(RANDOM.nextInt(unsolvedYears.size()));
            return new PuzzleSelection(selectedYear, 1);
        } else {
            // Priority 2: Randomly select a year and pick a random unsolved day from that year
            List<PuzzleSelection> unsolvedPuzzles = new ArrayList<>();
            for (Integer year : availableYears) {
                Set<Integer> solved = solvedByYear.getOrDefault(year, new HashSet<>());
                int totalDays = getTotalDaysForYear(configContent, year);
                LocalDate currentDate = LocalDate.now();
                for (int day = 1; day <= totalDays; day++) {
                    LocalDate puzzleDate = LocalDate.of(year, 12, day);
                    if (puzzleDate.isAfter(currentDate)) {
                        continue; // Skip future puzzles
                    }
                    if (!solved.contains(day)) {
                        // Special handling for Day 25: only allow if all previous days are solved
                        if (day == 25) {
                            boolean allPreviousDaysSolved = true;
                            for (int prevDay = 1; prevDay < 25; prevDay++) {
                                if (!solved.contains(prevDay)) {
                                    allPreviousDaysSolved = false;
                                    break;
                                }
                            }
                            if (allPreviousDaysSolved) {
                                unsolvedPuzzles.add(new PuzzleSelection(year, day));
                            }
                        } else {
                            unsolvedPuzzles.add(new PuzzleSelection(year, day));
                        }
                    }
                }
            }
            if (unsolvedPuzzles.isEmpty()) {
                return null;
            }
            return unsolvedPuzzles.get(RANDOM.nextInt(unsolvedPuzzles.size()));
        }
    }

    /**
     * Gets the total number of available days for a given year from the config JSON.
     * @param configJson JSON string containing puzzle configuration
     * @param year the year to search in
     * @return total number of days for the year, or 25 if not found
     */
    private static int getTotalDaysForYear(@NotNull String configJson, int year) {
        Pattern yearPattern = Pattern.compile("\"" + year + "\"\\s*:\\s*\\{");
        Pattern totalDaysPattern = Pattern.compile("\"totalDays\"\\s*:\\s*(\\d+)");
        Matcher yearMatcher = yearPattern.matcher(configJson);
        if (!yearMatcher.find()) {
            return 25;
        }
        int yearStart = yearMatcher.end();
        int yearEnd = findMatchingBrace(configJson, yearStart - 1);
        if (yearEnd == -1) {
            return 25;
        }
        String yearSection = configJson.substring(yearStart, yearEnd);
        Matcher totalDaysMatcher = totalDaysPattern.matcher(yearSection);
        if (!totalDaysMatcher.find()) {
            return 25;
        }
        return Integer.parseInt(totalDaysMatcher.group(1));
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
     * Builds a map of solved puzzles organised by year.
     * @param solutionsJson JSON string containing solutions database
     * @return Map where key is year and value is Set of solved days for that year
     */
    @NotNull
    private static Map<Integer, Set<Integer>> buildSolvedSetByYear(@NotNull String solutionsJson) {
        Map<Integer, Set<Integer>> solvedByYear = new HashMap<>();

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
            Set<Integer> solvedDays = new HashSet<>();

            Matcher dayMatcher = dayPattern.matcher(yearSection);
            while (dayMatcher.find()) {
                int day = Integer.parseInt(dayMatcher.group(1));
                solvedDays.add(day);
            }

            solvedByYear.put(year, solvedDays);
        }

        return solvedByYear;
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
     * Extracts all available years from the configuration.
     * @param configJson JSON string containing puzzle configuration
     * @return List of all available years from config
     */
    @NotNull
    private static List<Integer> extractAvailableYears(@NotNull String configJson) {
        List<Integer> years = new ArrayList<>();
        Pattern yearPattern = Pattern.compile("\"(\\d{4})\"\\s*:\\s*\\{");
        Matcher yearMatcher = yearPattern.matcher(configJson);

        while (yearMatcher.find()) {
            int year = Integer.parseInt(yearMatcher.group(1));
            years.add(year);
        }

        return years;
    }

    /**
     * Finds the lowest-numbered unsolved day within a specific year.
     * @param configJson JSON string containing puzzle configuration
     * @param year the year to search in
     * @param solvedDays Set of days already solved for this year
     * @return lowest unsolved day number, or -1 if all days are solved or no valid days exist
     */
    private static int findLowestUnsolvedDay(@NotNull String configJson, int year, @Nullable Set<Integer> solvedDays) {
        LocalDate currentDate = LocalDate.now();

        // Find total days for the given year
        Pattern yearPattern = Pattern.compile("\"" + year + "\"\\s*:\\s*\\{");
        Pattern totalDaysPattern = Pattern.compile("\"totalDays\"\\s*:\\s*(\\d+)");

        Matcher yearMatcher = yearPattern.matcher(configJson);
        if (!yearMatcher.find()) {
            return -1;
        }

        int yearStart = yearMatcher.end();
        int yearEnd = findMatchingBrace(configJson, yearStart - 1);
        if (yearEnd == -1) {
            return -1;
        }

        String yearSection = configJson.substring(yearStart, yearEnd);
        Matcher totalDaysMatcher = totalDaysPattern.matcher(yearSection);

        if (!totalDaysMatcher.find()) {
            return -1;
        }

        int totalDays = Integer.parseInt(totalDaysMatcher.group(1));
        Set<Integer> solved = solvedDays != null ? solvedDays : new HashSet<>();

        for (int day = 1; day <= totalDays; day++) {
            LocalDate puzzleDate = LocalDate.of(year, 12, day);

            // Skip future puzzles
            if (puzzleDate.isAfter(currentDate)) {
                continue;
            }

            if (!solved.contains(day)) {
                // Special handling for Day 25: only allow if all previous days are solved
                if (day == 25) {
                    boolean allPreviousDaysSolved = true;
                    for (int prevDay = 1; prevDay < 25; prevDay++) {
                        if (!solved.contains(prevDay)) {
                            allPreviousDaysSolved = false;
                            break;
                        }
                    }
                    if (allPreviousDaysSolved) {
                        return day;
                    }
                } else {
                    return day;
                }
            }
        }

        return -1;
    }

    /**
     * Finds the lowest-numbered unsolved day across any available year.
     * Used as a fallback when a randomly selected year has no unsolved days.
     * @param configJson JSON string containing puzzle configuration
     * @param availableYears List of all available years
     * @param solvedByYear Map of solved puzzles organised by year
     * @return lowest unsolved day number found, or -1 if no unsolved puzzles exist
     */
    private static int findLowestUnsolvedDayAcrossYears(@NotNull String configJson, @NotNull List<Integer> availableYears,
                                                        @NotNull Map<Integer, Set<Integer>> solvedByYear) {
        for (Integer year : availableYears) {
            int day = findLowestUnsolvedDay(configJson, year, solvedByYear.get(year));
            if (day != -1) {
                return day;
            }
        }
        return -1;
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

