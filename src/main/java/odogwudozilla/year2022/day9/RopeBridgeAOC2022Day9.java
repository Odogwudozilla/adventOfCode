package odogwudozilla.year2022.day9;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Advent of Code 2022 - Day 9: Rope Bridge
 * <p>
 * Puzzle URL: https://adventofcode.com/2022/day/9
 */
public final class RopeBridgeAOC2022Day9 {

    private static final String INPUT_FILE = "/2022/day9/day9_puzzle_data.txt";
    private static final int PART_ONE_KNOTS = 2;
    private static final int PART_TWO_KNOTS = 10;

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
        return String.valueOf(countVisitedTailPositions(input, PART_ONE_KNOTS));
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    static String solvePartTwo(List<String> input) {
        return String.valueOf(countVisitedTailPositions(input, PART_TWO_KNOTS));
    }

    private static int countVisitedTailPositions(List<String> input, int knotCount) {
        int[] xPositions = new int[knotCount];
        int[] yPositions = new int[knotCount];
        Set<String> visitedTailPositions = new HashSet<>();
        visitedTailPositions.add(encodePosition(0, 0));

        for (String line : input) {
            if (line == null || line.isBlank()) {
                continue;
            }

            String[] parts = line.split("\\s+");
            char direction = parts[0].charAt(0);
            int steps = Integer.parseInt(parts[1]);

            for (int i = 0; i < steps; i++) {
                moveHead(direction, xPositions, yPositions);
                updateFollowingKnots(xPositions, yPositions);
                visitedTailPositions.add(encodePosition(xPositions[knotCount - 1], yPositions[knotCount - 1]));
            }
        }

        return visitedTailPositions.size();
    }

    private static void moveHead(char direction, int[] xPositions, int[] yPositions) {
        if (direction == 'U') {
            yPositions[0]++;
        } else if (direction == 'D') {
            yPositions[0]--;
        } else if (direction == 'L') {
            xPositions[0]--;
        } else if (direction == 'R') {
            xPositions[0]++;
        } else {
            throw new IllegalArgumentException("Unknown direction: " + direction);
        }
    }

    private static void updateFollowingKnots(int[] xPositions, int[] yPositions) {
        for (int knot = 1; knot < xPositions.length; knot++) {
            int deltaX = xPositions[knot - 1] - xPositions[knot];
            int deltaY = yPositions[knot - 1] - yPositions[knot];

            if (Math.abs(deltaX) <= 1 && Math.abs(deltaY) <= 1) {
                continue;
            }

            xPositions[knot] += Integer.compare(deltaX, 0);
            yPositions[knot] += Integer.compare(deltaY, 0);
        }
    }

    private static String encodePosition(int x, int y) {
        return x + "," + y;
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = RopeBridgeAOC2022Day9.class.getResourceAsStream(INPUT_FILE)) {
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
