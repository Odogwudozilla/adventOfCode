package odogwudozilla.year2017.day15;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2017 - Day 15: Dueling Generators
 * <p>
 * Puzzle URL: https://adventofcode.com/2017/day/15
 */
public final class DuelingGeneratorsAOC2017Day15 {

    private static final String INPUT_FILE = "/2017/day15/day15_puzzle_data.txt";
    private static final long FACTOR_A = 16_807L;
    private static final long FACTOR_B = 48_271L;
    private static final long MODULUS = 2_147_483_647L;
    private static final long LOWEST_16_BITS_MASK = 0xFFFFL;
    private static final int PART_ONE_PAIR_COUNT = 40_000_000;
    private static final int PART_TWO_PAIR_COUNT = 5_000_000;
    private static final int MULTIPLE_ANY = 1;
    private static final int MULTIPLE_A_PART_TWO = 4;
    private static final int MULTIPLE_B_PART_TWO = 8;

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
     * Solves Part 1 of the puzzle.
     * @param input list of input lines
     * @return the Part 1 answer
     */
    static String solvePartOne(List<String> input) {
        long[] startingValues = parseStartingValues(input);
        long matches = countLowest16BitMatches(
                startingValues[0],
                startingValues[1],
                PART_ONE_PAIR_COUNT,
                MULTIPLE_ANY,
                MULTIPLE_ANY
        );
        return Long.toString(matches);
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    static String solvePartTwo(List<String> input) {
        long[] startingValues = parseStartingValues(input);
        long matches = countLowest16BitMatches(
                startingValues[0],
                startingValues[1],
                PART_TWO_PAIR_COUNT,
                MULTIPLE_A_PART_TWO,
                MULTIPLE_B_PART_TWO
        );
        return Long.toString(matches);
    }

    private static long[] parseStartingValues(List<String> input) {
        if (input.size() < 2) {
            throw new IllegalArgumentException("Expected two lines for generator starting values");
        }

        return new long[]{
                parseLastNumber(input.get(0)),
                parseLastNumber(input.get(1))
        };
    }

    private static long parseLastNumber(String inputLine) {
        String[] parts = inputLine.trim().split("\\s+");
        return Long.parseLong(parts[parts.length - 1]);
    }

    private static long countLowest16BitMatches(
            long startA,
            long startB,
            int pairCount,
            int multipleA,
            int multipleB
    ) {
        long currentA = startA;
        long currentB = startB;
        long matchCount = 0L;

        for (int i = 0; i < pairCount; i++) {
            currentA = nextGeneratedValue(currentA, FACTOR_A, multipleA);
            currentB = nextGeneratedValue(currentB, FACTOR_B, multipleB);

            if ((currentA & LOWEST_16_BITS_MASK) == (currentB & LOWEST_16_BITS_MASK)) {
                matchCount++;
            }
        }

        return matchCount;
    }

    private static long nextGeneratedValue(long previousValue, long factor, int requiredMultiple) {
        long value = previousValue;
        do {
            value = (value * factor) % MODULUS;
        } while (value % requiredMultiple != 0);
        return value;
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = DuelingGeneratorsAOC2017Day15.class.getResourceAsStream(INPUT_FILE)) {
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
