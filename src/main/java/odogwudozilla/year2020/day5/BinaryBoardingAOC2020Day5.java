package odogwudozilla.year2020.day5;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2020 - Day 5: Binary Boarding
 * <p>
 * Puzzle URL: https://adventofcode.com/2020/day/5
 */
public final class BinaryBoardingAOC2020Day5 {

    private static final String INPUT_FILE = "/2020/day5/day5_puzzle_data.txt";
    private static final int ROW_LENGTH = 7;
    private static final int COL_LENGTH = 3;
    private static final int ROWS = 128;
    private static final int COLS = 8;

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
        // Find the highest seat ID among all boarding passes.
        int maxSeatId = Integer.MIN_VALUE;
        for (String pass : input) {
            int seatId = decodeSeatId(pass);
            if (seatId > maxSeatId) {
                maxSeatId = seatId;
            }
        }
        return String.valueOf(maxSeatId);
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        // Find the missing seat ID (not at the very front or back, and both neighbours exist).
        java.util.Set<Integer> seatIds = new java.util.HashSet<>();
        int minSeatId = Integer.MAX_VALUE;
        int maxSeatId = Integer.MIN_VALUE;
        for (String pass : input) {
            int seatId = decodeSeatId(pass);
            seatIds.add(seatId);
            if (seatId < minSeatId) {
                minSeatId = seatId;
            }
            if (seatId > maxSeatId) {
                maxSeatId = seatId;
            }
        }
        for (int id = minSeatId + 1; id < maxSeatId; id++) {
            if (!seatIds.contains(id) && seatIds.contains(id - 1) && seatIds.contains(id + 1)) {
                return String.valueOf(id);
            }
        }
        return "not found";
    }

    /**
     * Decodes a boarding pass string to its seat ID.
     * @param pass the boarding pass string
     * @return the seat ID
     */
    private static int decodeSeatId(@org.jetbrains.annotations.NotNull String pass) {
        int row = 0;
        for (int i = 0; i < ROW_LENGTH; i++) {
            row <<= 1;
            char c = pass.charAt(i);
            if ('B' == c) {
                row |= 1;
            }
        }
        int col = 0;
        for (int i = 0; i < COL_LENGTH; i++) {
            col <<= 1;
            char c = pass.charAt(ROW_LENGTH + i);
            if ('R' == c) {
                col |= 1;
            }
        }
        return row * COLS + col;
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = BinaryBoardingAOC2020Day5.class.getResourceAsStream(INPUT_FILE)) {
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
