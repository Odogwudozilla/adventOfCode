package odogwudozilla.year2022.day6;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2022 - Day 6: Tuning Trouble
 * <p>
 * Puzzle URL: https://adventofcode.com/2022/day/6
 */
public final class TuningTroubleAOC2022Day6 {

    private static final String INPUT_FILE = "/2022/day6/day6_puzzle_data.txt";

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
    public static String solvePartOne(List<String> input) {
        // Input is a single line; ignore blank lines or extra lines if present
        if (input == null || input.isEmpty()) return "0";
        String line = input.get(0);
        final int WINDOW = 4;
        for (int i = 0; i <= line.length() - WINDOW; i++) {
            String window = line.substring(i, i + WINDOW);
            if (allUnique(window)) {
                // Return the position (1-based) of the last character in the window
                return String.valueOf(i + WINDOW);
            }
        }
        // If no marker found, return "0" (or could throw, but test expects string)
        return "0";
    }

    /**
     * Returns true if all characters in the string are unique.
     */
    private static boolean allUnique(String s) {
        java.util.Set<Character> set = new java.util.HashSet<>();
        for (char c : s.toCharArray()) {
            if (!set.add(c)) return false;
        }
        return true;
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    public static String solvePartTwo(List<String> input) {
        // Input is a single line; ignore blank lines or extra lines if present
        if (input == null || input.isEmpty()) return "0";
        String line = input.get(0);
        final int WINDOW = 14;
        for (int i = 0; i <= line.length() - WINDOW; i++) {
            String window = line.substring(i, i + WINDOW);
            if (allUnique(window)) {
                // Return the position (1-based) of the last character in the window
                return String.valueOf(i + WINDOW);
            }
        }
        // If no marker found, return "0"
        return "0";
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = TuningTroubleAOC2022Day6.class.getResourceAsStream(INPUT_FILE)) {
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
