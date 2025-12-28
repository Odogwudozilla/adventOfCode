package odogwudozilla.year2017.day2;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.io.IOException;

/**
 * Corruption Checksum (Advent of Code 2017 Day 2)
 * <p>
 * The spreadsheet consists of rows of apparently-random numbers. For each row, determine the difference between the largest value and the smallest value; the checksum is the sum of all of these differences.
 * <p>
 * Official puzzle link: https://adventofcode.com/2017/day/2
 *
 * @author GitHub Copilot
 */
public class CorruptionChecksumAOC2017Day2 {
    /**
     * Entry point for the solution.
     * Reads input and prints the result for Part 1.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        String inputPath = "src/main/resources/2017/day2/day2_puzzle_data.txt";
        try {
            List<String> lines = Files.readAllLines(Paths.get(inputPath));
            int checksum = solvePartOne(lines);
            System.out.println("main - Part 1 checksum: " + checksum);
            int partTwoResult = solvePartTwo(lines);
            System.out.println("main - Part 2 sum: " + partTwoResult);
        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Calculates the spreadsheet checksum for Part 1.
     *
     * @param lines List of input lines from the puzzle data file
     * @return The checksum value
     */
    public static int solvePartOne(List<String> lines) {
        int checksum = 0;
        for (String line : lines) {
            List<Integer> numbers = Arrays.stream(line.trim().split("\\s+"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            int max = numbers.stream().max(Integer::compareTo).orElse(0);
            int min = numbers.stream().min(Integer::compareTo).orElse(0);
            checksum += (max - min);
        }
        return checksum;
    }

    /**
     * Calculates the sum for Part 2 by finding the only two numbers in each row where one evenly divides the other.
     *
     * @param lines List of input lines from the puzzle data file
     * @return The sum of each row's division result
     */
    public static int solvePartTwo(List<String> lines) {
        int total = 0;
        for (String line : lines) {
            List<Integer> numbers = Arrays.stream(line.trim().split("\\s+"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            boolean found = false;
            for (int i = 0; i < numbers.size() && !found; i++) {
                for (int j = 0; j < numbers.size(); j++) {
                    if (i != j && numbers.get(i) % numbers.get(j) == 0) {
                        total += numbers.get(i) / numbers.get(j);
                        found = true;
                        break;
                    }
                }
            }
        }
        return total;
    }
}
