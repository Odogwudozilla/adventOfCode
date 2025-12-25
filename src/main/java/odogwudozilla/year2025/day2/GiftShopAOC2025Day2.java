package odogwudozilla.year2025.day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Advent of Code 2025 - Day 2: Gift Shop
 * <p>
 * Part 1: Find all invalid product IDs in given ranges. Invalid IDs are those made only of some
 * sequence of digits repeated exactly twice (e.g., 55, 6464, 123123).
 * <p>
 * Part 2: Find all invalid product IDs where the ID is made of some sequence repeated at least twice
 * (e.g., 12341234, 123123123, 1212121212, 1111111).
 * <p>
 * Puzzle URL: https://adventofcode.com/2025/day/2
 */
public class GiftShopAOC2025Day2 {

    private static final String INPUT_FILE = "src/main/resources/2025/day2/day2_puzzle_data.txt";

    public static void main(String[] args) {
        try {
            String input = Files.readString(Paths.get(INPUT_FILE)).trim();

            long sumOfInvalidIdsPart1 = solvePartOne(input);
            System.out.println("Part 1 - Sum of all invalid IDs: " + sumOfInvalidIdsPart1);

            long sumOfInvalidIdsPart2 = solvePartTwo(input);
            System.out.println("Part 2 - Sum of all invalid IDs: " + sumOfInvalidIdsPart2);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Solves Part 1: Find and sum all invalid product IDs in the given ranges.
     * @param input the comma-separated ranges of product IDs
     * @return the sum of all invalid product IDs
     */
    private static long solvePartOne(String input) {
        String[] ranges = input.split(",");
        List<Long> invalidIds = new ArrayList<>();

        for (String range : ranges) {
            range = range.trim();
            String[] parts = range.split("-");

            if (2 != parts.length) {
                System.err.println("Invalid range format: " + range);
                continue;
            }

            long start = Long.parseLong(parts[0]);
            long end = Long.parseLong(parts[1]);

            // Find all invalid IDs in this range
            for (long id = start; id <= end; id++) {
                if (isInvalidIdPart1(id)) {
                    invalidIds.add(id);
                }
            }
        }

        // Sum all invalid IDs
        return invalidIds.stream().mapToLong(Long::longValue).sum();
    }

    /**
     * Solves Part 2: Find and sum all invalid product IDs in the given ranges.
     * Invalid IDs are those made of a sequence repeated at least twice.
     * @param input the comma-separated ranges of product IDs
     * @return the sum of all invalid product IDs
     */
    private static long solvePartTwo(String input) {
        String[] ranges = input.split(",");
        List<Long> invalidIds = new ArrayList<>();

        for (String range : ranges) {
            range = range.trim();
            String[] parts = range.split("-");

            if (2 != parts.length) {
                System.err.println("Invalid range format: " + range);
                continue;
            }

            long start = Long.parseLong(parts[0]);
            long end = Long.parseLong(parts[1]);

            // Find all invalid IDs in this range
            for (long id = start; id <= end; id++) {
                if (isInvalidIdPart2(id)) {
                    invalidIds.add(id);
                }
            }
        }

        // Sum all invalid IDs
        return invalidIds.stream().mapToLong(Long::longValue).sum();
    }

    /**
     * Checks if a product ID is invalid for Part 1 (made of a digit sequence repeated exactly twice).
     * @param id the product ID to check
     * @return true if the ID is invalid, false otherwise
     */
    private static boolean isInvalidIdPart1(long id) {
        String idStr = String.valueOf(id);
        int length = idStr.length();

        // ID must have even length to be a repeated sequence
        if (0 != length % 2) {
            return false;
        }

        int halfLength = length / 2;
        String firstHalf = idStr.substring(0, halfLength);
        String secondHalf = idStr.substring(halfLength);

        // Check if both halves are identical and first half doesn't start with 0
        return firstHalf.equals(secondHalf) && '0' != firstHalf.charAt(0);
    }

    /**
     * Checks if a product ID is invalid for Part 2 (made of a digit sequence repeated at least twice).
     * @param id the product ID to check
     * @return true if the ID is invalid, false otherwise
     */
    private static boolean isInvalidIdPart2(long id) {
        String idStr = String.valueOf(id);
        int length = idStr.length();

        // Try all possible pattern lengths from 1 to length/2
        for (int patternLength = 1; patternLength <= length / 2; patternLength++) {
            // Check if the length is divisible by the pattern length
            if (0 != length % patternLength) {
                continue;
            }

            String pattern = idStr.substring(0, patternLength);

            // Skip patterns starting with 0
            if ('0' == pattern.charAt(0)) {
                continue;
            }

            // Check if the entire ID is made of this pattern repeated
            boolean isRepeatedPattern = true;
            int repetitions = length / patternLength;

            // Must be repeated at least twice
            if (repetitions < 2) {
                continue;
            }

            for (int i = 0; i < repetitions; i++) {
                int start = i * patternLength;
                int end = start + patternLength;
                String segment = idStr.substring(start, end);

                if (!pattern.equals(segment)) {
                    isRepeatedPattern = false;
                    break;
                }
            }

            if (isRepeatedPattern) {
                return true;
            }
        }

        return false;
    }
}

