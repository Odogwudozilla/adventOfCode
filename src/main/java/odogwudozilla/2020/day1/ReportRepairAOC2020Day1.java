package odogwudozilla._2020.day1;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Report Repair (Advent of Code 2020 Day 1)
 * https://adventofcode.com/2020/day/1
 *
 * The Elves in accounting need you to fix your expense report. Find the two entries that sum to 2020 and multiply them together.
 * Part 2: Find three entries that sum to 2020 and multiply them together.
 *
 * @param args Command line arguments (not used)
 * @return None
 */
public class ReportRepairAOC2020Day1 {
    public static void main(String[] args) throws Exception {
        // main - Reading input file
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/2020/day1/day1_puzzle_data.txt"));
        Set<Integer> numbers = new HashSet<>();
        for (String line : lines) {
            numbers.add(Integer.parseInt(line.trim()));
        }
        // main - Solving Part 1
        final int TARGET_SUM = 2020;
        boolean found = false;
        for (int num : numbers) {
            int complement = TARGET_SUM - num;
            if (numbers.contains(complement)) {
                System.out.println("main - Part 1: Two entries: " + num + " and " + complement + ". Product: " + (num * complement));
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("main - Part 1: No solution found.");
        }
        // main - Solving Part 2
        boolean foundPart2 = false;
        Integer[] arr = numbers.toArray(new Integer[0]);
        outer:
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                int first = arr[i];
                int second = arr[j];
                int third = TARGET_SUM - first - second;
                if (numbers.contains(third) && third != first && third != second) {
                    System.out.println("main - Part 2: Three entries: " + first + ", " + second + ", " + third + ". Product: " + (first * second * third));
                    foundPart2 = true;
                    break outer;
                }
            }
        }
        if (!foundPart2) {
            System.out.println("main - Part 2: No solution found.");
        }
    }
}

