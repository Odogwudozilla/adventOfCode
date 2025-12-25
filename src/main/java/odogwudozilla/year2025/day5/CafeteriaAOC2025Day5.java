package odogwudozilla.year2025.day5;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Advent of Code 2025 - Day 5: Cafeteria
 * <p>
 * The Elves need help determining which ingredient IDs are fresh based on their new inventory
 * management system. The database contains fresh ingredient ID ranges and available ingredient IDs.
 * This solution checks how many available ingredients fall within the fresh ranges.
 * <p>
 * Puzzle URL: https://adventofcode.com/2025/day/5
 */
public class CafeteriaAOC2025Day5 {

    private static final String PUZZLE_DATA_FILE = "src/main/resources/2025/day5/day5_puzzle_data.txt";

    public static void main(String[] args) {
        System.out.println("main - Starting Advent of Code 2025 - Day 5: Cafeteria");

        try {
            List<String> lines = Files.readAllLines(Paths.get(PUZZLE_DATA_FILE));

            int freshCount = solvePartOne(lines);
            System.out.println("main - Part 1 - Number of fresh ingredient IDs: " + freshCount);

            long totalFreshIds = solvePartTwo(lines);
            System.out.println("main - Part 2 - Total ingredient IDs considered fresh: " + totalFreshIds);
        } catch (IOException e) {
            System.err.println("main - Error reading puzzle data file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1 of the puzzle by counting how many available ingredient IDs are fresh.
     * @param lines the input lines from the puzzle data file
     * @return the count of fresh ingredient IDs
     */
    private static int solvePartOne(@NotNull List<String> lines) {
        System.out.println("solvePartOne - Parsing fresh ingredient ranges and available IDs");

        List<Range> freshRanges = new ArrayList<>();
        List<Long> availableIds = new ArrayList<>();
        boolean parsingRanges = true;

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                parsingRanges = false;
                continue;
            }

            if (parsingRanges) {
                freshRanges.add(parseRange(line));
            } else {
                availableIds.add(Long.parseLong(line.trim()));
            }
        }

        System.out.println("solvePartOne - Parsed " + freshRanges.size() + " fresh ranges and " + availableIds.size() + " available IDs");

        int freshCount = 0;
        for (Long id : availableIds) {
            if (isFresh(id, freshRanges)) {
                freshCount++;
            }
        }

        return freshCount;
    }

    /**
     * Solves Part 2 of the puzzle by counting all unique ingredient IDs covered by the fresh ranges.
     * @param lines the input lines from the puzzle data file
     * @return the total count of unique ingredient IDs in all fresh ranges
     */
    private static long solvePartTwo(@NotNull List<String> lines) {
        System.out.println("solvePartTwo - Parsing fresh ingredient ranges");

        List<Range> freshRanges = new ArrayList<>();

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                break;
            }
            freshRanges.add(parseRange(line));
        }

        System.out.println("solvePartTwo - Parsed " + freshRanges.size() + " fresh ranges");

        List<Range> mergedRanges = mergeRanges(freshRanges);
        System.out.println("solvePartTwo - Merged into " + mergedRanges.size() + " non-overlapping ranges");

        long totalCount = 0;
        for (Range range : mergedRanges) {
            totalCount += range.size();
        }

        return totalCount;
    }

    /**
     * Merges overlapping ranges into non-overlapping ranges.
     * @param ranges the list of ranges to merge
     * @return a list of merged, non-overlapping ranges
     */
    @NotNull
    private static List<Range> mergeRanges(@NotNull List<Range> ranges) {
        if (ranges.isEmpty()) {
            return new ArrayList<>();
        }

        List<Range> sortedRanges = new ArrayList<>(ranges);
        sortedRanges.sort((r1, r2) -> Long.compare(r1.start, r2.start));

        List<Range> mergedRanges = new ArrayList<>();
        Range current = sortedRanges.get(0);

        for (int i = 1; i < sortedRanges.size(); i++) {
            Range next = sortedRanges.get(i);

            if (current.overlapsOrTouches(next)) {
                current = current.merge(next);
            } else {
                mergedRanges.add(current);
                current = next;
            }
        }

        mergedRanges.add(current);
        return mergedRanges;
    }

    /**
     * Parses a range string in the format "start-end" into a Range object.
     * @param rangeStr the range string to parse
     * @return a Range object representing the parsed range
     */
    @NotNull
    private static Range parseRange(@NotNull String rangeStr) {
        String[] parts = rangeStr.split("-");
        long start = Long.parseLong(parts[0].trim());
        long end = Long.parseLong(parts[1].trim());
        return new Range(start, end);
    }

    /**
     * Checks if an ingredient ID is fresh by verifying if it falls within any fresh range.
     * @param id the ingredient ID to check
     * @param freshRanges the list of fresh ingredient ranges
     * @return true if the ID is fresh, false otherwise
     */
    private static boolean isFresh(long id, @NotNull List<Range> freshRanges) {
        for (Range range : freshRanges) {
            if (range.contains(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Represents a range of ingredient IDs with inclusive start and end values.
     */
    private static class Range {
        private final long start;
        private final long end;

        public Range(long start, long end) {
            this.start = start;
            this.end = end;
        }

        /**
         * Checks if a value falls within this range (inclusive).
         * @param value the value to check
         * @return true if the value is within the range, false otherwise
         */
        public boolean contains(long value) {
            return value >= start && value <= end;
        }

        /**
         * Calculates the size of this range (number of IDs it contains).
         * @return the size of the range
         */
        public long size() {
            return end - start + 1;
        }

        /**
         * Checks if this range overlaps with or touches another range.
         * @param other the other range to check
         * @return true if the ranges overlap or touch, false otherwise
         */
        public boolean overlapsOrTouches(@NotNull Range other) {
            return this.start <= other.end + 1 && this.end + 1 >= other.start;
        }

        /**
         * Merges this range with another range.
         * @param other the other range to merge with
         * @return a new Range that spans both ranges
         */
        @NotNull
        public Range merge(@NotNull Range other) {
            return new Range(Math.min(this.start, other.start), Math.max(this.end, other.end));
        }
    }
}

