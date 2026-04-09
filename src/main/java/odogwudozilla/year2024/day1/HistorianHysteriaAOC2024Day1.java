package odogwudozilla.year2024.day1;

import org.jetbrains.annotations.NotNull;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Historian Hysteria (Advent of Code 2024 Day 1)
 * The Chief Historian is missing, and you must help the Senior Historians reconcile two lists of location IDs by pairing up the smallest numbers and summing the distances.
 *
 * Puzzle link: https://adventofcode.com/2024/day/1
 */
public class HistorianHysteriaAOC2024Day1 {
    public static void main(String[] args) throws Exception {
        List<Integer> leftList = new ArrayList<>();
        List<Integer> rightList = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/2024/day1/day1_puzzle_data.txt"));
        for (String line : lines) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 2) {
                leftList.add(Integer.parseInt(parts[0]));
                rightList.add(Integer.parseInt(parts[1]));
            }
        }
        int partOneResult = solvePartOne(leftList, rightList);
        System.out.println("Part 1: " + partOneResult);
        long partTwoResult = solvePartTwo(leftList, rightList);
        System.out.println("Part 2: " + partTwoResult);
    }

    /**
     * Solves Part 1: Pair up the sorted lists and sum the absolute differences.
     * @param leftList the first list of location IDs
     * @param rightList the second list of location IDs
     * @return the total distance between the lists
     */
    public static int solvePartOne(@NotNull List<Integer> leftList, @NotNull List<Integer> rightList) {
        Collections.sort(leftList);
        Collections.sort(rightList);
        int total = 0;
        for (int i = 0; i < leftList.size(); i++) {
            total += Math.abs(leftList.get(i) - rightList.get(i));
        }
        return total;
    }

    /**
     * Solves Part 2: For each number in the left list, multiply it by the number of times it appears in the right list and sum the results.
     * @param leftList the first list of location IDs
     * @param rightList the second list of location IDs
     * @return the similarity score
     */
    public static long solvePartTwo(@NotNull List<Integer> leftList, @NotNull List<Integer> rightList) {
        java.util.Map<Integer, Integer> rightCount = new java.util.HashMap<>();
        for (int num : rightList) {
            rightCount.put(num, rightCount.getOrDefault(num, 0) + 1);
        }
        long total = 0;
        for (int num : leftList) {
            total += (long) num * rightCount.getOrDefault(num, 0);
        }
        return total;
    }
}
