package odogwudozilla.year2023.day13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Advent of Code 2023 - Day 13: Point of Incidence
 * This puzzle involves finding lines of reflection in patterns of ash (.) and rocks (#).
 * Each pattern can have either a vertical or horizontal line of reflection.
 * The solution calculates: sum of columns left of vertical reflections + 100 × rows above horizontal reflections.
 * @see <a href="https://adventofcode.com/2023/day/13">Puzzle URL</a>
 */
public class PointOfIncidenceAOC2023Day13 {

    private static final String INPUT_FILE = "src/main/resources/2023/day13/day13_puzzle_data.txt";
    private static final int HORIZONTAL_MULTIPLIER = 100;

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(INPUT_FILE));
            List<List<String>> patterns = parsePatterns(lines);

            long part1Result = solvePartOne(patterns);
            System.out.println("Part 1 - Total reflection summary: " + part1Result);

            long part2Result = solvePartTwo(patterns);
            System.out.println("Part 2 - Total reflection summary with smudge: " + part2Result);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Solves Part 1: Find the line of reflection in each pattern.
     * @param patterns the list of patterns to analyse
     * @return the summary number (columns left of vertical + 100 × rows above horizontal)
     */
    private static long solvePartOne(List<List<String>> patterns) {
        long totalSummary = 0;

        for (List<String> pattern : patterns) {
            // Try to find vertical reflection first
            int verticalReflection = findVerticalReflection(pattern, -1);
            if (verticalReflection > 0) {
                totalSummary += verticalReflection;
                continue;
            }

            // Try to find horizontal reflection
            int horizontalReflection = findHorizontalReflection(pattern, -1);
            if (horizontalReflection > 0) {
                totalSummary += (long) horizontalReflection * HORIZONTAL_MULTIPLIER;
            }
        }

        return totalSummary;
    }

    /**
     * Solves Part 2: Find the line of reflection after fixing one smudge.
     * @param patterns the list of patterns to analyse
     * @return the summary number after fixing smudges
     */
    private static long solvePartTwo(List<List<String>> patterns) {
        long totalSummary = 0;

        for (List<String> pattern : patterns) {
            // Find the original reflection to exclude it
            int originalVertical = findVerticalReflection(pattern, -1);
            int originalHorizontal = findHorizontalReflection(pattern, -1);

            // Try to find new reflection with exactly one difference (the smudge)
            int verticalReflection = findVerticalReflectionWithSmudge(pattern, originalVertical);
            if (verticalReflection > 0) {
                totalSummary += verticalReflection;
                continue;
            }

            int horizontalReflection = findHorizontalReflectionWithSmudge(pattern, originalHorizontal);
            if (horizontalReflection > 0) {
                totalSummary += (long) horizontalReflection * HORIZONTAL_MULTIPLIER;
            }
        }

        return totalSummary;
    }

    /**
     * Parses the input into separate patterns (separated by blank lines).
     * @param lines the input lines
     * @return list of patterns
     */
    private static List<List<String>> parsePatterns(List<String> lines) {
        List<List<String>> patterns = new ArrayList<>();
        List<String> currentPattern = new ArrayList<>();

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                if (!currentPattern.isEmpty()) {
                    patterns.add(new ArrayList<>(currentPattern));
                    currentPattern.clear();
                }
            } else {
                currentPattern.add(line);
            }
        }

        // Add the last pattern if it exists
        if (!currentPattern.isEmpty()) {
            patterns.add(currentPattern);
        }

        return patterns;
    }

    /**
     * Finds a vertical line of reflection in the pattern.
     * @param pattern the pattern to analyse
     * @param excludeColumn the column to exclude (from previous search, -1 if none)
     * @return the number of columns to the left of the reflection line, or 0 if not found
     */
    private static int findVerticalReflection(List<String> pattern, int excludeColumn) {
        if (pattern.isEmpty()) {
            return 0;
        }

        int width = pattern.get(0).length();

        // Try each possible vertical line position (between columns)
        for (int col = 1; col < width; col++) {
            if (col == excludeColumn) {
                continue;
            }

            if (isVerticalReflection(pattern, col)) {
                return col;
            }
        }

        return 0;
    }

    /**
     * Checks if there's a perfect vertical reflection at the given column position.
     * @param pattern the pattern to check
     * @param col the column position (number of columns to the left of the reflection line)
     * @return true if there's a perfect reflection
     */
    private static boolean isVerticalReflection(List<String> pattern, int col) {
        int width = pattern.get(0).length();

        for (String row : pattern) {
            // Check how many columns can be compared
            int compareDistance = Math.min(col, width - col);

            for (int offset = 0; offset < compareDistance; offset++) {
                int leftIdx = col - offset - 1;
                int rightIdx = col + offset;

                if (row.charAt(leftIdx) != row.charAt(rightIdx)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Finds a horizontal line of reflection in the pattern.
     * @param pattern the pattern to analyse
     * @param excludeRow the row to exclude (from previous search, -1 if none)
     * @return the number of rows above the reflection line, or 0 if not found
     */
    private static int findHorizontalReflection(List<String> pattern, int excludeRow) {
        int height = pattern.size();

        // Try each possible horizontal line position (between rows)
        for (int row = 1; row < height; row++) {
            if (row == excludeRow) {
                continue;
            }

            if (isHorizontalReflection(pattern, row)) {
                return row;
            }
        }

        return 0;
    }

    /**
     * Checks if there's a perfect horizontal reflection at the given row position.
     * @param pattern the pattern to check
     * @param row the row position (number of rows above the reflection line)
     * @return true if there's a perfect reflection
     */
    private static boolean isHorizontalReflection(List<String> pattern, int row) {
        int height = pattern.size();

        // Check how many rows can be compared
        int compareDistance = Math.min(row, height - row);

        for (int offset = 0; offset < compareDistance; offset++) {
            int topIdx = row - offset - 1;
            int bottomIdx = row + offset;

            if (!pattern.get(topIdx).equals(pattern.get(bottomIdx))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Finds a vertical reflection with exactly one smudge (one character difference).
     * @param pattern the pattern to analyse
     * @param excludeColumn the original column to exclude
     * @return the number of columns to the left of the new reflection line
     */
    private static int findVerticalReflectionWithSmudge(List<String> pattern, int excludeColumn) {
        if (pattern.isEmpty()) {
            return 0;
        }

        int width = pattern.get(0).length();

        for (int col = 1; col < width; col++) {
            if (col == excludeColumn) {
                continue;
            }

            if (countVerticalDifferences(pattern, col) == 1) {
                return col;
            }
        }

        return 0;
    }

    /**
     * Counts the number of character differences for a vertical reflection.
     * @param pattern the pattern to check
     * @param col the column position
     * @return the number of differences
     */
    private static int countVerticalDifferences(List<String> pattern, int col) {
        int width = pattern.get(0).length();
        int differences = 0;

        for (String row : pattern) {
            int compareDistance = Math.min(col, width - col);

            for (int offset = 0; offset < compareDistance; offset++) {
                int leftIdx = col - offset - 1;
                int rightIdx = col + offset;

                if (row.charAt(leftIdx) != row.charAt(rightIdx)) {
                    differences++;
                }
            }
        }

        return differences;
    }

    /**
     * Finds a horizontal reflection with exactly one smudge (one character difference).
     * @param pattern the pattern to analyse
     * @param excludeRow the original row to exclude
     * @return the number of rows above the new reflection line
     */
    private static int findHorizontalReflectionWithSmudge(List<String> pattern, int excludeRow) {
        int height = pattern.size();

        for (int row = 1; row < height; row++) {
            if (row == excludeRow) {
                continue;
            }

            if (countHorizontalDifferences(pattern, row) == 1) {
                return row;
            }
        }

        return 0;
    }

    /**
     * Counts the number of character differences for a horizontal reflection.
     * @param pattern the pattern to check
     * @param row the row position
     * @return the number of differences
     */
    private static int countHorizontalDifferences(List<String> pattern, int row) {
        int height = pattern.size();
        int differences = 0;

        int compareDistance = Math.min(row, height - row);

        for (int offset = 0; offset < compareDistance; offset++) {
            int topIdx = row - offset - 1;
            int bottomIdx = row + offset;

            String topRow = pattern.get(topIdx);
            String bottomRow = pattern.get(bottomIdx);

            for (int i = 0; i < topRow.length(); i++) {
                if (topRow.charAt(i) != bottomRow.charAt(i)) {
                    differences++;
                }
            }
        }

        return differences;
    }
}

