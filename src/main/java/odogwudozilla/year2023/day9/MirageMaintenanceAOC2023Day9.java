package odogwudozilla.year2023.day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Mirage Maintenance (Advent of Code 2023 Day 9)
 * <p>
 * You ride the camel through the sandstorm and stop where the ghost's maps told you to stop. The sandstorm subsequently subsides, somehow seeing you standing at an oasis!
 * <p>
 * The camel goes to get some water and you stretch your neck. As you look up, you discover what must be yet another giant floating island, this one made of metal! That must be where the parts to fix the sand machines come from.
 * <p>
 * There's even a hang glider partially buried in the sand here; once the sun rises and heats up the sand, you might be able to use the glider and the hot air to get all the way up to the metal island!
 * <p>
 * While you wait for the sun to rise, you admire the oasis hidden here in the middle of Desert Island. It must have a delicate ecosystem; you might as well take some ecological readings while you wait. Maybe you can report any environmental instabilities you find to someone so the oasis can be around for the next sandstorm-worn traveler.
 * <p>
 * You pull out your handy Oasis And Sand Instability Sensor and analyze your surroundings. The OASIS produces a report of many values and how they are changing over time (your puzzle input). Each line in the report contains the history of a single value.
 * <p>
 * Official puzzle URL: https://adventofcode.com/2023/day/9
 *
 * @author Advent of Code
 */
public class MirageMaintenanceAOC2023Day9 {
    private static final String INPUT_PATH = "src/main/resources/2023/day9/day9_puzzle_data.txt";

    /**
     * Entry point for the solution.
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(INPUT_PATH));
            int partOneResult = solvePartOne(lines);
            System.out.println("Part 1: " + partOneResult);
            int partTwoResult = solvePartTwo(lines);
            System.out.println("Part 2: " + partTwoResult);
        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1 of the puzzle: sum of extrapolated next values for each history.
     * @param lines List of input lines
     * @return Sum of extrapolated values
     */
    public static int solvePartOne(List<String> lines) {
        int sum = 0;
        for (String line : lines) {
            List<Integer> history = parseLine(line);
            int nextValue = extrapolateNextValue(history);
            sum += nextValue;
        }
        return sum;
    }

    /**
     * Solves Part 2 of the puzzle: sum of extrapolated previous values for each history.
     * @param lines List of input lines
     * @return Sum of extrapolated previous values
     */
    public static int solvePartTwo(List<String> lines) {
        int sum = 0;
        for (String line : lines) {
            List<Integer> history = parseLine(line);
            int prevValue = extrapolatePreviousValue(history);
            sum += prevValue;
        }
        return sum;
    }

    /**
     * Parses a line of space-separated integers.
     * @param line Input line
     * @return List of integers
     */
    private static List<Integer> parseLine(String line) {
        String[] tokens = line.trim().split("\\s+");
        List<Integer> result = new ArrayList<>();
        for (String token : tokens) {
            result.add(Integer.parseInt(token));
        }
        return result;
    }

    /**
     * Extrapolates the next value in the sequence using difference sequences.
     * @param history List of integers representing the history
     * @return The next value in the sequence
     */
    private static int extrapolateNextValue(List<Integer> history) {
        List<List<Integer>> diffs = new ArrayList<>();
        diffs.add(new ArrayList<>(history));
        // Generate difference sequences until all zeroes
        while (!allZero(diffs.get(diffs.size() - 1))) {
            List<Integer> prev = diffs.get(diffs.size() - 1);
            List<Integer> next = new ArrayList<>();
            for (int i = 1; i < prev.size(); i++) {
                next.add(prev.get(i) - prev.get(i - 1));
            }
            diffs.add(next);
        }
        // Extrapolate upwards
        int value = 0;
        for (int i = diffs.size() - 1; i >= 0; i--) {
            value += diffs.get(i).getLast();
        }
        return value;
    }

    /**
     * Extrapolates the previous value in the sequence using difference sequences (Part 2 logic).
     * @param history List of integers representing the history
     * @return The previous value in the sequence
     */
    private static int extrapolatePreviousValue(List<Integer> history) {
        List<List<Integer>> diffs = new ArrayList<>();
        diffs.add(new ArrayList<>(history));
        // Generate difference sequences until all zeroes
        while (!allZero(diffs.getLast())) {
            List<Integer> prev = diffs.getLast();
            List<Integer> next = new ArrayList<>();
            for (int i = 1; i < prev.size(); i++) {
                next.add(prev.get(i) - prev.get(i - 1));
            }
            diffs.add(next);
        }
        // Extrapolate backwards (prepend zero and fill from bottom up)
        int value = 0;
        for (int i = diffs.size() - 1; i >= 0; i--) {
            value = diffs.get(i).get(0) - value;
        }
        return value;
    }

    /**
     * Checks if all values in the list are zero.
     * @param list List of integers
     * @return True if all are zero, false otherwise
     */
    private static boolean allZero(List<Integer> list) {
        for (int n : list) {
            if (n != 0) {
                return false;
            }
        }
        return true;
    }
}
