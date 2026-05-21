package odogwudozilla.year2021.day8;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/**
 * Advent of Code 2021 - Day 8: Seven Segment Search
 * <p>
 * Puzzle URL: https://adventofcode.com/2021/day/8
 */
public final class SevenSegmentSearchAOC2021Day8 {

    private static final String INPUT_FILE = "/2021/day8/day8_puzzle_data.txt";

    /** Segment count uniquely identifying digit 1. */
    private static final int LEN_DIGIT_ONE = 2;
    /** Segment count uniquely identifying digit 7. */
    private static final int LEN_DIGIT_SEVEN = 3;
    /** Segment count uniquely identifying digit 4. */
    private static final int LEN_DIGIT_FOUR = 4;
    /** Segment count uniquely identifying digit 8. */
    private static final int LEN_DIGIT_EIGHT = 7;
    /** Segment count shared by digits 2, 3, and 5. */
    private static final int LEN_FIVE_SEGMENT = 5;
    /** Segment count shared by digits 0, 6, and 9. */
    private static final int LEN_SIX_SEGMENT = 6;

    /**
     * Segment lengths that uniquely identify a digit.
     * <ul>
     *   <li>2 → digit 1 (only digit using exactly 2 segments)</li>
     *   <li>3 → digit 7 (only digit using exactly 3 segments)</li>
     *   <li>4 → digit 4 (only digit using exactly 4 segments)</li>
     *   <li>7 → digit 8 (only digit using exactly 7 segments)</li>
     * </ul>
     */
    private static final Set<Integer> UNIQUE_SEGMENT_LENGTHS = Set.of(LEN_DIGIT_ONE, LEN_DIGIT_SEVEN, LEN_DIGIT_FOUR, LEN_DIGIT_EIGHT);

    /** Positional multiplier for the thousands digit in a four-digit output value. */
    private static final int MULTIPLIER_THOUSANDS = 1000;
    /** Positional multiplier for the hundreds digit in a four-digit output value. */
    private static final int MULTIPLIER_HUNDREDS = 100;
    /** Positional multiplier for the tens digit in a four-digit output value. */
    private static final int MULTIPLIER_TENS = 10;
    /** Positional multiplier for the units digit in a four-digit output value. */
    private static final int MULTIPLIER_UNITS = 1;
    /** Number of output tokens per display entry. */
    private static final int OUTPUT_TOKEN_COUNT = 4;

    /**
     * Entry point. Reads puzzle input and prints solutions for Part 1 and Part 2.
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        List<String> input = readInput();
        System.out.println("Part 1: " + solvePartOne(input));
        System.out.println("Part 2: " + solvePartTwo(input));
    }

    /**
     * Counts how many times a digit with a unique segment count (1, 4, 7, or 8) appears
     * in the four-digit output values across all input lines.
     * <p>
     * Only the output side (right of {@code |}) is examined. Tokens whose length is in
     * {@code {2, 3, 4, 7}} correspond uniquely to digits 1, 7, 4, and 8 respectively.
     * @param input list of input lines, each containing ten signal patterns, a {@code |}
     *              delimiter, and four output tokens
     * @return the total count of easy-digit occurrences as a {@link String}
     */
    static String solvePartOne(@NotNull List<String> input) {
        int count = 0;
        for (String line : input) {
            if (line.isBlank()) {
                continue;
            }
            String[] halves = line.split("\\|", 2);
            String outputSection = halves[1].strip();
            String[] outputTokens = outputSection.split("\\s+");
            for (String token : outputTokens) {
                if (UNIQUE_SEGMENT_LENGTHS.contains(token.length())) {
                    count++;
                }
            }
        }
        return String.valueOf(count);
    }

    /**
     * Decodes every display entry by applying constraint-propagation set-intersection
     * logic to the ten signal patterns, then sums all four-digit decoded output values.
     * <p>
     * Deduction sequence per line:
     * <ol>
     *   <li>Anchor digits 1, 4, 7, and 8 from their unique segment counts.</li>
     *   <li>Resolve the six-segment group (0, 6, 9) via {@code containsAll} checks against digit 1 and digit 4.</li>
     *   <li>Resolve the five-segment group (2, 3, 5) via {@code containsAll} checks against digit 1 and digit 6.</li>
     *   <li>Build a sorted-character-string → digit map and decode the four output tokens.</li>
     * </ol>
     * @param input list of input lines, each containing ten signal patterns, a {@code |}
     *              delimiter, and four output tokens
     * @return the sum of all decoded four-digit output values as a {@link String}
     */
    static String solvePartTwo(@NotNull List<String> input) {
        int total = 0;
        for (String line : input) {
            if (line.isBlank()) {
                continue;
            }
            String[] halves = line.split("\\|", 2);
            String[] patterns = halves[0].strip().split("\\s+");
            String[] outputTokens = halves[1].strip().split("\\s+");

            // Step 1: anchor identification from unique segment lengths
            Set<Character> one = null;
            Set<Character> four = null;
            Set<Character> seven = null;
            Set<Character> eight = null;
            List<Set<Character>> fiveSegCandidates = new ArrayList<>();
            List<Set<Character>> sixSegCandidates = new ArrayList<>();

            for (String pattern : patterns) {
                Set<Character> chars = toCharSet(pattern);
                switch (pattern.length()) {
                    case LEN_DIGIT_ONE   -> one   = chars;
                    case LEN_DIGIT_SEVEN -> seven = chars;
                    case LEN_DIGIT_FOUR  -> four  = chars;
                    case LEN_DIGIT_EIGHT -> eight = chars;
                    case LEN_FIVE_SEGMENT -> fiveSegCandidates.add(chars);
                    case LEN_SIX_SEGMENT  -> sixSegCandidates.add(chars);
                    default -> { /* no other lengths exist */ }
                }
            }

            // Step 2: resolve six-segment group (0, 6, 9)
            // digit 6: the sole len=6 pattern that does NOT contain both wires of digit 1
            // digit 9: contains all wires of digit 4
            // digit 0: the remaining len=6 pattern
            Set<Character> six  = null;
            Set<Character> nine = null;
            Set<Character> zero = null;

            for (Set<Character> candidate : sixSegCandidates) {
                if (!candidate.containsAll(one)) {
                    six = candidate;
                } else if (candidate.containsAll(four)) {
                    nine = candidate;
                } else {
                    zero = candidate;
                }
            }

            // Step 3: resolve five-segment group (2, 3, 5)
            // digit 3: the sole len=5 pattern that contains both wires of digit 1
            // digit 5: its wires are all present in digit 6 (six.containsAll(fiveCandidate))
            // digit 2: the remaining len=5 pattern
            Set<Character> three = null;
            Set<Character> five  = null;
            Set<Character> two   = null;

            for (Set<Character> candidate : fiveSegCandidates) {
                if (candidate.containsAll(one)) {
                    three = candidate;
                } else if (six.containsAll(candidate)) {
                    five = candidate;
                } else {
                    two = candidate;
                }
            }

            // Step 4: build sorted-character-string → digit lookup map
            Map<String, Integer> digitMap = new HashMap<>();
            digitMap.put(sortChars(zero),  0);
            digitMap.put(sortChars(one),   1);
            digitMap.put(sortChars(two),   2);
            digitMap.put(sortChars(three), 3);
            digitMap.put(sortChars(four),  4);
            digitMap.put(sortChars(five),  5);
            digitMap.put(sortChars(six),   6);
            digitMap.put(sortChars(seven), 7);
            digitMap.put(sortChars(eight), 8);
            digitMap.put(sortChars(nine),  9);

            // Step 5: decode the four output tokens into a four-digit integer
            int[] multipliers = {MULTIPLIER_THOUSANDS, MULTIPLIER_HUNDREDS, MULTIPLIER_TENS, MULTIPLIER_UNITS};
            int value = 0;
            for (int i = 0; i < OUTPUT_TOKEN_COUNT; i++) {
                value += digitMap.get(sortChars(outputTokens[i])) * multipliers[i];
            }
            total += value;
        }
        return String.valueOf(total);
    }

    /**
     * Converts a signal-pattern string into a {@link Set} of its constituent characters.
     * @param pattern the signal-pattern string (e.g. {@code "gcbe"})
     * @return a mutable {@link Set} containing each character of the pattern
     */
    private static Set<Character> toCharSet(@NotNull String pattern) {
        Set<Character> set = new HashSet<>();
        for (char c : pattern.toCharArray()) {
            set.add(c);
        }
        return set;
    }

    /**
     * Returns a string of the given characters sorted in ascending order.
     * Used to produce canonical map keys that are independent of the original character order.
     * @param chars the set of characters to sort
     * @return a {@link String} of the characters sorted alphabetically
     */
    private static String sortChars(@NotNull Set<Character> chars) {
        char[] arr = new char[chars.size()];
        int idx = 0;
        for (char c : chars) {
            arr[idx++] = c;
        }
        Arrays.sort(arr);
        return new String(arr);
    }

    /**
     * Returns a string of the characters of the given token sorted in ascending order.
     * Used to normalise output tokens before performing map lookups.
     * @param token the output token string (e.g. {@code "fcadb"})
     * @return a {@link String} of the token's characters sorted alphabetically
     */
    private static String sortChars(@NotNull String token) {
        char[] arr = token.toCharArray();
        Arrays.sort(arr);
        return new String(arr);
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = SevenSegmentSearchAOC2021Day8.class.getResourceAsStream(INPUT_FILE)) {
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
