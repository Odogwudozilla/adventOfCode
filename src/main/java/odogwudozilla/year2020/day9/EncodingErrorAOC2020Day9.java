package odogwudozilla.year2020.day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Advent of Code 2020 Day 9: Encoding Error
 *
 * This puzzle involves finding anomalies in the XMAS encryption.
 * Part 1: Find the first number that is not the sum of two of the 25 previous numbers.
 * Part 2: Find a contiguous set of numbers that sum to the invalid number, then return
 * the sum of the smallest and largest numbers in that set.
 *
 * URL: https://adventofcode.com/2020/day/9
 */
public class EncodingErrorAOC2020Day9 {

    private static final String INPUT_FILE = "src/main/resources/2020/day9/day9_puzzle_data.txt";
    private static final int PREAMBLE_SIZE = 25;

    public static void main(String[] args) {
        try {
            List<Long> numbers = readInput(INPUT_FILE);

            long partOneAnswer = solvePartOne(numbers);
            System.out.println("Part 1: " + partOneAnswer);

            long partTwoAnswer = solvePartTwo(numbers, partOneAnswer);
            System.out.println("Part 2: " + partTwoAnswer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the puzzle input from the file.
     */
    private static List<Long> readInput(String filePath) throws IOException {
        List<Long> numbers = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        for (String line : lines) {
            if (!line.isBlank()) {
                numbers.add(Long.parseLong(line.trim()));
            }
        }
        return numbers;
    }

    /**
     * Finds the first number in the list (after the preamble) which is not the sum
     * of two of the 25 numbers before it.
     */
    private static long solvePartOne(List<Long> numbers) {
        for (int i = PREAMBLE_SIZE; i < numbers.size(); i++) {
            long target = numbers.get(i);
            List<Long> preamble = numbers.subList(i - PREAMBLE_SIZE, i);

            if (!hasSumOfTwo(preamble, target)) {
                return target;
            }
        }
        return -1;
    }

    /**
     * Checks if any two different numbers in the list sum to the target.
     */
    private static boolean hasSumOfTwo(List<Long> numbers, long target) {
        for (int i = 0; i < numbers.size(); i++) {
            for (int j = i + 1; j < numbers.size(); j++) {
                if (numbers.get(i) + numbers.get(j) == target) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Finds a contiguous set of at least two numbers that sum to the invalid number,
     * then returns the sum of the smallest and largest in that set.
     */
    private static long solvePartTwo(List<Long> numbers, long invalidNumber) {
        for (int i = 0; i < numbers.size(); i++) {
            long sum = 0;
            List<Long> contiguousSet = new ArrayList<>();

            for (int j = i; j < numbers.size(); j++) {
                long current = numbers.get(j);
                sum += current;
                contiguousSet.add(current);

                if (sum == invalidNumber && contiguousSet.size() >= 2) {
                    long min = Collections.min(contiguousSet);
                    long max = Collections.max(contiguousSet);
                    return min + max;
                }

                if (sum > invalidNumber) {
                    break;
                }
            }
        }
        return -1;
    }
}

