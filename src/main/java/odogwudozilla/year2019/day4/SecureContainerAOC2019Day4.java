package odogwudozilla.year2019.day4;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2019 - Day 4: Secure Container
 * <p>
 * Given a range of six-digit numbers, count how many passwords satisfy rules about
 * non-decreasing digits and adjacent duplicate digits.
 * <p>
 * Puzzle URL: https://adventofcode.com/2019/day/4
 */
public final class SecureContainerAOC2019Day4 {

    private static final String INPUT_FILE = "/2019/day4/day4_puzzle_data.txt";

    /**
     * Entry point. Reads puzzle input and prints solutions for Part 1 and Part 2.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        List<String> input = readInput();
        System.out.println("Part 1: " + solvePartOne(input));
        System.out.println("Part 2: " + solvePartTwo(input));
    }

    /**
     * Solves Part 1: counts passwords in the range where digits never decrease
     * and at least two adjacent digits are the same.
     * @param input list of input lines containing the range "low-high"
     * @return count of valid passwords as a string
     */
    private static String solvePartOne(List<String> input) {
        int[] range = parseRange(input.get(0));
        int low = range[0];
        int high = range[1];
        int count = 0;
        for (int candidate = low; candidate <= high; candidate++) {
            if (isNonDecreasing(candidate) && hasAdjacentPair(candidate)) {
                count++;
            }
        }
        return String.valueOf(count);
    }

    /**
     * Solves Part 2: counts passwords in the range where digits never decrease
     * and at least one group of exactly two adjacent identical digits exists.
     * @param input list of input lines containing the range "low-high"
     * @return count of valid passwords as a string
     */
    private static String solvePartTwo(List<String> input) {
        int[] range = parseRange(input.get(0));
        int low = range[0];
        int high = range[1];
        int count = 0;
        for (int candidate = low; candidate <= high; candidate++) {
            if (isNonDecreasing(candidate) && hasExactPair(candidate)) {
                count++;
            }
        }
        return String.valueOf(count);
    }

    /**
     * Parses the range string "low-high" into an integer array.
     * @param rangeStr the range string, e.g. "145852-616942"
     * @return array of two integers: [low, high]
     */
    private static int[] parseRange(String rangeStr) {
        String[] parts = rangeStr.split("-");
        return new int[]{Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim())};
    }

    /**
     * Returns true if the digits of the number never decrease from left to right.
     * @param number the candidate password number
     * @return true if non-decreasing
     */
    private static boolean isNonDecreasing(int number) {
        String digits = String.valueOf(number);
        for (int i = 1; i < digits.length(); i++) {
            if (digits.charAt(i) < digits.charAt(i - 1)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the number contains at least one pair of adjacent identical digits.
     * @param number the candidate password number
     * @return true if an adjacent pair exists
     */
    private static boolean hasAdjacentPair(int number) {
        String digits = String.valueOf(number);
        for (int i = 1; i < digits.length(); i++) {
            if (digits.charAt(i) == digits.charAt(i - 1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the number contains at least one group of exactly two adjacent
     * identical digits (the pair must not be part of a larger group of three or more).
     * @param number the candidate password number
     * @return true if an exact-pair group exists
     */
    private static boolean hasExactPair(int number) {
        String digits = String.valueOf(number);
        int index = 0;
        while (index < digits.length()) {
            char current = digits.charAt(index);
            int groupLength = 1;
            while (index + groupLength < digits.length() && digits.charAt(index + groupLength) == current) {
                groupLength++;
            }
            if (groupLength == 2) {
                return true;
            }
            index += groupLength;
        }
        return false;
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = SecureContainerAOC2019Day4.class.getResourceAsStream(INPUT_FILE)) {
            if (stream == null) {
                throw new IllegalStateException("Input file not found: " + INPUT_FILE);
            }
            Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8);
            List<String> lines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            return lines;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read input file", e);
        }
    }
}
