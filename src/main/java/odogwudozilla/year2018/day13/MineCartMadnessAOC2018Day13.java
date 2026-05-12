package odogwudozilla.year2018.day13;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;

/**
 * Advent of Code 2018 - Day 13: Mine Cart Madness
 *
 * Puzzle URL: https://adventofcode.com/2018/day/13
 */
public final class MineCartMadnessAOC2018Day13 {
    private static final String INPUT_FILE = "src/main/resources/2018/day13/day13_puzzle_data.txt";

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
        // TODO: Implement Part 1 logic
        return "";
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        // TODO: Implement Part 2 logic
        return "";
    }

    /**
     * Reads the puzzle input file from the filesystem.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try {
            return Files.readAllLines(Paths.get(INPUT_FILE));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read input file: " + INPUT_FILE, e);
        }
    }
}
