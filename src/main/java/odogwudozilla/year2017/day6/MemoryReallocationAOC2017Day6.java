package odogwudozilla.year2017.day6;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2017 - Day 6: Memory Reallocation
 * <p>
 * Puzzle URL: https://adventofcode.com/2017/day/6
 */
public final class MemoryReallocationAOC2017Day6 {

    private static final String INPUT_FILE = "/2017/day6/day6_puzzle_data.txt";

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
    private static String solvePartOne(List<String> input) {
        int[] banks = parseInput(input);
        java.util.Set<String> seen = new java.util.HashSet<>();
        int cycles = 0;
        while (seen.add(serialize(banks))) {
            redistribute(banks);
            cycles++;
        }
        return String.valueOf(cycles);
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        int[] banks = parseInput(input);
        java.util.Map<String, Integer> seen = new java.util.HashMap<>();
        int cycles = 0;
        while (!seen.containsKey(serialize(banks))) {
            seen.put(serialize(banks), cycles);
            redistribute(banks);
            cycles++;
        }
        int firstSeen = seen.get(serialize(banks));
        return String.valueOf(cycles - firstSeen);
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = MemoryReallocationAOC2017Day6.class.getResourceAsStream(INPUT_FILE)) {
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

    /**
     * Parses the puzzle input into an array of memory banks.
     * @param input list of input lines
     * @return array of memory banks
     */
    private static int[] parseInput(List<String> input) {
        String[] tokens = input.get(0).trim().split("\t");
        int[] banks = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            banks[i] = Integer.parseInt(tokens[i].trim());
        }
        return banks;
    }

    /**
     * Serialises the memory banks to a string for state comparison.
     * @param banks memory banks
     * @return serialised state
     */
    private static String serialize(int[] banks) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < banks.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(banks[i]);
        }
        return sb.toString();
    }

    /**
     * Redistributes blocks among the memory banks.
     * @param banks memory banks
     */
    private static void redistribute(int[] banks) {
        int maxIdx = 0;
        for (int i = 1; i < banks.length; i++) {
            if (banks[i] > banks[maxIdx]) {
                maxIdx = i;
            }
        }
        int blocks = banks[maxIdx];
        banks[maxIdx] = 0;
        int idx = (maxIdx + 1) % banks.length;
        while (blocks-- > 0) {
            banks[idx]++;
            idx = (idx + 1) % banks.length;
        }
    }
}
