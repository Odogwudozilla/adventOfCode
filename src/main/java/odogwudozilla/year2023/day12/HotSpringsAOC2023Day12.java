package odogwudozilla.year2023.day12;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Advent of Code 2023 - Day 12: Hot Springs
 * <p>
 * Puzzle URL: https://adventofcode.com/2023/day/12
 */
public final class HotSpringsAOC2023Day12 {

    private static final String INPUT_FILE = "/2023/day12/day12_puzzle_data.txt";

    /** Bit-shift used to pack patternIndex into the high bits of the memo key. */
    private static final int GROUP_INDEX_BITS = 8;

    /** Number of times each row is unfolded for Part 2. */
    private static final int UNFOLD_FACTOR = 5;

    /** Separator character inserted between repeated copies when unfolding a pattern. */
    private static final char UNFOLD_SEPARATOR = '?';

    /** Character representing an operational (undamaged) spring. */
    private static final char OPERATIONAL = '.';
    /** Character representing a damaged spring. */
    private static final char DAMAGED = '#';
    /** Character representing a spring of unknown condition. */
    private static final char UNKNOWN = '?';

    /**
     * Entry point. Reads puzzle input and prints solutions for Part 1 and Part 2.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        List<String> input = readInput();
        System.out.println("Part 1: " + solvePartOne(input));
        System.out.println("Part 2: " + solvePartTwo(input));
    }

    /**
     * Solves Part 1: counts the total valid arrangements across all rows using
     * top-down memoised recursion over (patternIndex, groupIndex) state space.
     * @param input list of input lines
     * @return the Part 1 answer as a String
     */
    public static String solvePartOne(List<String> input) {
        long total = 0L;
        for (String line : input) {
            if (line.isBlank()) {
                continue;
            }
            String[] parts = line.split(" ");
            char[] pattern = parts[0].toCharArray();
            int[] groups = parseGroups(parts[1]);
            Map<Long, Long> memo = new HashMap<>();
            total += countArrangements(pattern, groups, 0, 0, memo);
        }
        return String.valueOf(total);
    }

    /**
     * Solves Part 2: unfolds each row ×5 (pattern joined by '?', groups repeated ×5) then
     * counts total valid arrangements using the same memoised recursion as Part 1.
     * @param input list of input lines
     * @return the Part 2 answer as a String
     */
    public static String solvePartTwo(List<String> input) {
        long total = 0L;
        for (String line : input) {
            if (line.isBlank()) {
                continue;
            }
            String[] parts = line.split(" ");
            String patternStr = parts[0];
            int[] groups = parseGroups(parts[1]);

            // Unfold: repeat pattern UNFOLD_FACTOR times joined by UNFOLD_SEPARATOR
            StringBuilder unfoldedPattern = new StringBuilder();
            for (int i = 0; i < UNFOLD_FACTOR; i++) {
                if (i > 0) {
                    unfoldedPattern.append(UNFOLD_SEPARATOR);
                }
                unfoldedPattern.append(patternStr);
            }

            // Unfold: repeat groups UNFOLD_FACTOR times
            int[] unfoldedGroups = new int[groups.length * UNFOLD_FACTOR];
            for (int i = 0; i < UNFOLD_FACTOR; i++) {
                System.arraycopy(groups, 0, unfoldedGroups, i * groups.length, groups.length);
            }

            Map<Long, Long> memo = new HashMap<>();
            total += countArrangements(unfoldedPattern.toString().toCharArray(), unfoldedGroups, 0, 0, memo);
        }
        return String.valueOf(total);
    }

    // -----------------------------------------------------------------------
    // Core recursive helper
    // -----------------------------------------------------------------------

    /**
     * Counts the number of valid arrangements for the suffix of {@code pattern}
     * starting at index {@code pi}, satisfying the remaining groups from index
     * {@code gi} onwards.
     *
     * @param pattern the spring condition character array
     * @param groups  the contiguous damaged-group sizes
     * @param pi      current position in the pattern
     * @param gi      current position in the groups array
     * @param memo    memoisation cache keyed on a packed (pi, gi) long
     * @return the number of valid arrangements for this sub-problem
     */
    private static long countArrangements(char[] pattern, int[] groups, int pi, int gi,
                                          Map<Long, Long> memo) {
        long key = packKey(pi, gi);
        Long cached = memo.get(key);
        if (cached != null) {
            return cached;
        }

        // Base case: all groups have been placed
        if (gi == groups.length) {
            // Valid only if no '#' remains unaccounted for in the rest of the pattern
            for (int i = pi; i < pattern.length; i++) {
                if (pattern[i] == DAMAGED) {
                    memo.put(key, 0L);
                    return 0L;
                }
            }
            memo.put(key, 1L);
            return 1L;
        }

        // Base case: pattern exhausted but groups remain
        if (pi >= pattern.length) {
            memo.put(key, 0L);
            return 0L;
        }

        long result = 0L;
        char current = pattern[pi];

        // Branch: treat current position as operational ('.')
        if (current == OPERATIONAL || current == UNKNOWN) {
            result += countArrangements(pattern, groups, pi + 1, gi, memo);
        }

        // Branch: attempt to place the next group starting at pi
        if (current == DAMAGED || current == UNKNOWN) {
            int groupSize = groups[gi];
            int end = pi + groupSize;  // exclusive end of the group

            // The group must fit within the pattern
            if (end <= pattern.length) {
                // All characters in [pi, end) must be '#' or '?'
                boolean canPlace = true;
                for (int i = pi; i < end; i++) {
                    if (pattern[i] == OPERATIONAL) {
                        canPlace = false;
                        break;
                    }
                }

                if (canPlace) {
                    // The character immediately after the group must be a separator ('.' or '?') or end-of-pattern
                    if (end == pattern.length) {
                        result += countArrangements(pattern, groups, end, gi + 1, memo);
                    } else if (pattern[end] != DAMAGED) {
                        // Skip the mandatory separator by advancing to end + 1
                        result += countArrangements(pattern, groups, end + 1, gi + 1, memo);
                    }
                }
            }
        }

        memo.put(key, result);
        return result;
    }

    // -----------------------------------------------------------------------
    // Utility helpers
    // -----------------------------------------------------------------------

    /**
     * Parses a comma-separated group-size string such as {@code "1,3,1,6"} into an int[].
     * @param groupStr the raw group-size string from the puzzle input
     * @return array of group sizes
     */
    private static int[] parseGroups(String groupStr) {
        String[] tokens = groupStr.split(",");
        int[] groups = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            groups[i] = Integer.parseInt(tokens[i].trim());
        }
        return groups;
    }

    /**
     * Packs two non-negative indices into a single long to serve as a memo cache key.
     * groupIndex is stored in the low {@value #GROUP_INDEX_BITS} bits; patternIndex
     * occupies the remaining high bits.
     *
     * @param patternIndex current pattern position
     * @param groupIndex   current group position
     * @return packed long key
     */
    private static long packKey(int patternIndex, int groupIndex) {
        return ((long) patternIndex << GROUP_INDEX_BITS) | groupIndex;
    }

    // -----------------------------------------------------------------------
    // Input reading
    // -----------------------------------------------------------------------

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = HotSpringsAOC2023Day12.class.getResourceAsStream(INPUT_FILE)) {
            if (stream == null) {
                throw new IllegalStateException("Input file not found: " + INPUT_FILE);
            }
            Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8);
            List<String> lines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            return lines;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read input file", e);
        }
    }
}
