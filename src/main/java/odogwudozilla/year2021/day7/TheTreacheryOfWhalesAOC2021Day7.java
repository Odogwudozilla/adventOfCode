package odogwudozilla.year2021.day7;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2021 - Day 7: The Treachery of Whales
 * <p>
 * Puzzle URL: https://adventofcode.com/2021/day/7
 */
public final class TheTreacheryOfWhalesAOC2021Day7 {

    private static final String INPUT_FILE = "/2021/day7/day7_puzzle_data.txt";

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
        // Parse crab positions
        int[] positions = parsePositions(input);
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (int pos : positions) {
            if (pos < min) min = pos;
            if (pos > max) max = pos;
        }
        int minFuel = Integer.MAX_VALUE;
        for (int align = min; align <= max; align++) {
            int fuel = 0;
            for (int pos : positions) {
                fuel += Math.abs(pos - align);
            }
            if (fuel < minFuel) minFuel = fuel;
        }
        return String.valueOf(minFuel);
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        // Parse crab positions
        int[] positions = parsePositions(input);
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (int pos : positions) {
            if (pos < min) min = pos;
            if (pos > max) max = pos;
        }
        int minFuel = Integer.MAX_VALUE;
        for (int align = min; align <= max; align++) {
            int fuel = 0;
            for (int pos : positions) {
                int dist = Math.abs(pos - align);
                fuel += dist * (dist + 1) / 2; // Triangular number cost
            }
            if (fuel < minFuel) minFuel = fuel;
        }
        return String.valueOf(minFuel);
    }

    /**
     * Parses the crab positions from the input.
     * @param input list of input lines
     * @return array of crab positions
     */
    private static int[] parsePositions(List<String> input) {
        String[] tokens = input.get(0).split(",");
        int[] arr = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            arr[i] = Integer.parseInt(tokens[i].trim());
        }
        return arr;
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = TheTreacheryOfWhalesAOC2021Day7.class.getResourceAsStream(INPUT_FILE)) {
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
