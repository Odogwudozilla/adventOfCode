package odogwudozilla.year2020.day10;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adapter Array (Advent of Code 2020 Day 10)
 *
 * Patched into the aircraft's data port, you discover weather forecasts of a massive tropical storm. Before you can figure out whether it will impact your vacation plans, however, your device suddenly turns off! Its battery is dead. You'll need to plug it in. There's only one problem: the charging outlet near your seat produces the wrong number of jolts. Always prepared, you make a list of all of the joltage adapters in your bag. Each of your joltage adapters is rated for a specific output joltage (your puzzle input). Any given adapter can take an input 1, 2, or 3 jolts lower than its rating and still produce its rated output joltage. In addition, your device has a built-in joltage adapter rated for 3 jolts higher than the highest-rated adapter in your bag. Treat the charging outlet near your seat as having an effective joltage rating of 0. Find a chain that uses all of your adapters to connect the charging outlet to your device's built-in adapter and count the joltage differences between the charging outlet, the adapters, and your device. What is the number of 1-jolt differences multiplied by the number of 3-jolt differences?
 *
 * Official puzzle link: https://adventofcode.com/2020/day/10
 */
public class AdapterArrayAOC2020Day10 {
    private static final String INPUT_PATH = "src/main/resources/2020/day10/day10_puzzle_data.txt";

    /**
     * Main entry point for the solution.
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) throws Exception {
        List<Integer> adapters = readInput(INPUT_PATH);
        System.out.println("solvePartOne - Part 1: " + solvePartOne(adapters));
        System.out.println("solvePartTwo - Part 2: " + solvePartTwo(adapters));
    }

    /**
     * Reads the input file and returns a list of adapter joltages.
     * @param path Path to the input file
     * @return List of adapter joltages
     */
    private static List<Integer> readInput(String path) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(path));
        List<Integer> adapters = new ArrayList<>();
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                adapters.add(Integer.parseInt(line.trim()));
            }
        }
        return adapters;
    }

    /**
     * Solves Part 1: Find the number of 1-jolt differences multiplied by the number of 3-jolt differences.
     * @param adapters List of adapter joltages
     * @return The product of 1-jolt and 3-jolt differences
     */
    public static int solvePartOne(List<Integer> adapters) {
        List<Integer> sorted = new ArrayList<>(adapters);
        Collections.sort(sorted);
        int oneJoltDiffs = 0;
        int threeJoltDiffs = 0;
        int previous = 0; // Outlet joltage
        for (int joltage : sorted) {
            int diff = joltage - previous;
            if (diff == 1) {
                oneJoltDiffs++;
            } else if (diff == 3) {
                threeJoltDiffs++;
            }
            previous = joltage;
        }
        // Account for device's built-in adapter (always +3 from highest)
        threeJoltDiffs++;
        return oneJoltDiffs * threeJoltDiffs;
    }

    /**
     * Solves Part 2: Counts the total number of distinct valid adapter arrangements.
     * @param adapters List of adapter joltages
     * @return The total number of valid arrangements
     */
    public static long solvePartTwo(List<Integer> adapters) {
        List<Integer> sorted = new ArrayList<>(adapters);
        Collections.sort(sorted);
        // Add the charging outlet (0) and device's built-in adapter (max+3)
        sorted.addFirst(0);
        sorted.add(sorted.getLast() + 3);
        // Dynamic programming: ways[i] = number of ways to reach adapter i
        long[] ways = new long[sorted.size()];
        ways[0] = 1;
        for (int i = 1; i < sorted.size(); i++) {
            for (int j = i - 1; j >= 0 && sorted.get(i) - sorted.get(j) <= 3; j--) {
                ways[i] += ways[j];
            }
        }
        return ways[ways.length - 1];
    }
}
