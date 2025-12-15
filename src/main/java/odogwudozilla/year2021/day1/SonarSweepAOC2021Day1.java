package odogwudozilla.year2021.day1;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Advent of Code 2021 Day 1: Sonar Sweep
 *
 * This puzzle involves analyzing sonar sweep data from a submarine to count depth increases.
 * Part 1: Count how many times a depth measurement increases from the previous measurement.
 * Part 2: Count how many times the sum of a three-measurement sliding window increases from the previous sum.
 *
 * Puzzle URL: https://adventofcode.com/2021/day/1
 */
public class SonarSweepAOC2021Day1 {
    private static final String PUZZLE_DATA_PATH = "src/main/resources/2021/day1/day1_puzzle_data.txt";
    private static final int WINDOW_SIZE = 3;

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(PUZZLE_DATA_PATH));
            List<Integer> depths = lines.stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            int part1Result = solvePart1(depths);
            int part2Result = solvePart2(depths);

            System.out.println("Part 1: " + part1Result);
            System.out.println("Part 2: " + part2Result);
        } catch (Exception e) {
            System.err.println("Error reading puzzle data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Part 1: Count the number of times a depth measurement increases from the previous measurement.
     */
    private static int solvePart1(List<Integer> depths) {
        int increaseCount = 0;
        for (int i = 1; i < depths.size(); i++) {
            if (depths.get(i) > depths.get(i - 1)) {
                increaseCount++;
            }
        }
        return increaseCount;
    }

    /**
     * Part 2: Count the number of times the sum of a three-measurement sliding window increases
     * from the previous sum.
     */
    private static int solvePart2(List<Integer> depths) {
        List<Integer> windowSums = calculateWindowSums(depths, WINDOW_SIZE);
        return solvePart1(windowSums);
    }

    /**
     * Calculate the sums of sliding windows of the specified size.
     */
    private static List<Integer> calculateWindowSums(List<Integer> depths, int windowSize) {
        java.util.ArrayList<Integer> windowSums = new java.util.ArrayList<>();
        for (int i = 0; i <= depths.size() - windowSize; i++) {
            int sum = 0;
            for (int j = 0; j < windowSize; j++) {
                sum += depths.get(i + j);
            }
            windowSums.add(sum);
        }
        return windowSums;
    }
}

