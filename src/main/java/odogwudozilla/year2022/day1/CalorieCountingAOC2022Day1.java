package odogwudozilla.year2022.day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Advent of Code 2022 - Day 1: Calorie Counting
 * <p>
 * The Elves are taking inventory of their food supplies by counting calories.
 * Each Elf's inventory is separated by blank lines in the input data.
 * Part 1: Find the Elf carrying the most total calories.
 * Part 2: Find the sum of calories carried by the top three Elves.
 * <p>
 * Puzzle: <a href="https://adventofcode.com/2022/day/1">https://adventofcode.com/2022/day/1</a>
 */
public class CalorieCountingAOC2022Day1 {

    private static final String INPUT_FILE = "src/main/resources/2022/day1/day1_puzzle_data.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(INPUT_FILE));

            // Calculate total calories for each Elf
            List<Integer> elfCalories = calculateElfCalories(lines);

            // Part 1: Find the maximum calories carried by any single Elf
            int maxCalories = solvePartOne(elfCalories);
            System.out.println("Part 1 - Maximum calories carried by one Elf: " + maxCalories);

            // Part 2: Find sum of top three Elves' calories
            int topThreeSum = solvePartTwo(elfCalories);
            System.out.println("Part 2 - Sum of top three Elves' calories: " + topThreeSum);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Parse the input lines and calculate total calories for each Elf.
     * Elves are separated by blank lines in the input.
     * @param lines the input lines from the puzzle data file
     * @return a list of total calories per Elf
     */
    private static List<Integer> calculateElfCalories(List<String> lines) {
        List<Integer> elfCalories = new ArrayList<>();
        int currentElfTotal = 0;

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                // Blank line indicates end of current Elf's inventory
                elfCalories.add(currentElfTotal);
                currentElfTotal = 0;
            } else {
                // Add calories to current Elf's total
                currentElfTotal += Integer.parseInt(line.trim());
            }
        }

        // Add the last Elf's total (in case file doesn't end with blank line)
        if (currentElfTotal > 0) {
            elfCalories.add(currentElfTotal);
        }

        return elfCalories;
    }

    /**
     * Solve Part 1: Find the maximum calories carried by any single Elf.
     * @param elfCalories list of total calories per Elf
     * @return the maximum calories
     */
    private static int solvePartOne(List<Integer> elfCalories) {
        return Collections.max(elfCalories);
    }

    /**
     * Solve Part 2: Find the sum of calories carried by the top three Elves.
     * @param elfCalories list of total calories per Elf
     * @return the sum of the top three values
     */
    private static int solvePartTwo(List<Integer> elfCalories) {
        // Sort in descending order
        List<Integer> sortedCalories = new ArrayList<>(elfCalories);
        sortedCalories.sort(Collections.reverseOrder());

        // Sum the top three
        int topThreeSum = 0;
        final int topElvesCount = 3;
        for (int i = 0; i < Math.min(topElvesCount, sortedCalories.size()); i++) {
            topThreeSum += sortedCalories.get(i);
        }

        return topThreeSum;
    }
}
