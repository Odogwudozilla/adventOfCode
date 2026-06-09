package odogwudozilla.year2024.day11;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2024 - Day 11: Plutonian Pebbles
 * <p>
 * Puzzle URL: https://adventofcode.com/2024/day/11
 */
public final class PlutonianPebblesAOC2024Day11 {

    private static final String INPUT_FILE = "/2024/day11/day11_puzzle_data.txt";

    /**
     * Entry point. Reads puzzle input and prints solutions for Part 1 and Part 2.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        List<String> input = readInput();
        System.out.println("Part 1: " + solvePartOne(input));
        System.out.println("Part 2: " + solvePartTwo(input));
    }

    private static final int PART_ONE_BLINKS = 25;
    private static final int PART_TWO_BLINKS = 75;
    private static final long MULTIPLIER = 2024L;

    /**
     * Solves Part 1: counts the number of stones after 25 blinks.
     *
     * @param input list of input lines containing the initial stone numbers
     * @return total stone count after 25 blinks as a string
     */
    private static String solvePartOne(final List<String> input) {
        return String.valueOf(simulate(input, PART_ONE_BLINKS));
    }

    /**
     * Solves Part 2: counts the number of stones after 75 blinks.
     *
     * @param input list of input lines containing the initial stone numbers
     * @return total stone count after 75 blinks as a string
     */
    private static String solvePartTwo(final List<String> input) {
        return String.valueOf(simulate(input, PART_TWO_BLINKS));
    }

    /**
     * Simulates the stone evolution for a given number of blinks using a frequency map.
     * Each entry in the map is {@code stoneValue -> count}, allowing identical stones
     * to be processed together in O(distinct values) per blink rather than O(total stones).
     *
     * @param input  puzzle input lines
     * @param blinks number of blinks to simulate
     * @return total number of stones after the specified number of blinks
     */
    private static long simulate(final List<String> input, final int blinks) {
        java.util.Map<Long, Long> stoneCounts = new java.util.HashMap<>();
        for (final String token : input.get(0).trim().split("\\s+")) {
            final long value = Long.parseLong(token);
            stoneCounts.merge(value, 1L, Long::sum);
        }

        for (int i = 0; i < blinks; i++) {
            final java.util.Map<Long, Long> next = new java.util.HashMap<>();
            for (final java.util.Map.Entry<Long, Long> entry : stoneCounts.entrySet()) {
                final long stone = entry.getKey();
                final long count = entry.getValue();

                if (stone == 0L) {
                    next.merge(1L, count, Long::sum);
                } else {
                    final String digits = Long.toString(stone);
                    final int numDigits = digits.length();
                    if (numDigits % 2 == 0) {
                        final int mid = numDigits / 2;
                        final long left = Long.parseLong(digits.substring(0, mid));
                        final long right = Long.parseLong(digits.substring(mid));
                        next.merge(left, count, Long::sum);
                        next.merge(right, count, Long::sum);
                    } else {
                        next.merge(stone * MULTIPLIER, count, Long::sum);
                    }
                }
            }
            stoneCounts = next;
        }

        return stoneCounts.values().stream().mapToLong(Long::longValue).sum();
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = PlutonianPebblesAOC2024Day11.class.getResourceAsStream(INPUT_FILE)) {
            if (stream == null) {
                throw new IllegalStateException("Input file not found: " + INPUT_FILE);
            }
            Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8);
            java.util.List<String> lines = new java.util.ArrayList<>();
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            return lines;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read input file", e);
        }
    }
}
