package odogwudozilla.year2020.day1;

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
        int part1Product = solvePartOne(numbers);
        if (part1Product != -1) {
            System.out.println("main - Part 1: Product of two entries: " + part1Product);
        } else {
            System.out.println("main - Part 1: No solution found.");
        }
        int part2Product = solvePartTwo(numbers);
        if (part2Product != -1) {
            System.out.println("main - Part 2: Product of three entries: " + part2Product);
        } else {
            System.out.println("main - Part 2: No solution found.");
        }
    }

    /**
     * Standardised method for Part 1.
     */
    private static int solvePartOne(Set<Integer> numbers) {
        final int TARGET_SUM = 2020;
        for (int num : numbers) {
            int complement = TARGET_SUM - num;
            if (numbers.contains(complement)) {
                return num * complement;
            }
        }
        return -1;
    }

    /**
     * Standardised method for Part 2.
     */
    private static int solvePartTwo(Set<Integer> numbers) {
        final int TARGET_SUM = 2020;
        Integer[] arr = numbers.toArray(new Integer[0]);
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                int first = arr[i];
                int second = arr[j];
                int third = TARGET_SUM - first - second;
                if (numbers.contains(third) && third != first && third != second) {
                    return first * second * third;
                }
            }
        }
        return -1;
    }
}
