package odogwudozilla.year2023.day1;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

/**
 * Day 1: Trebuchet?!
 * https://adventofcode.com/2023/day/1
 *
 * Something is wrong with global snow production, and you've been selected to take a look. The Elves have even given
 * you a map; on it, they've used stars to mark the top fifty locations that are likely to be having problems.
 *
 * You've been doing this long enough to know that to restore snow operations, you need to check all fifty stars by December 25th.
 *
 * Collect stars by solving puzzles. Two puzzles will be made available on each day in the Advent calendar; the second
 * puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!
 *
 * The Elves are having trouble reading the values on the calibration document. Each line contains a calibration value,
 * which can be found by combining the first digit and the last digit (in that order) to form a two-digit number.
 * The sum of all calibration values is the answer.
 */
public class TrebuchetAOC2023Day1 {
    private static final String INPUT_PATH = "src/main/resources/2023/day1/day1_puzzle_data.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(INPUT_PATH));
            int sum = solvePartOne(lines);
            System.out.println("Part 1: " + sum);
            int sum2 = solvePartTwo(lines);
            System.out.println("Part 2: " + sum2);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1: Sums calibration values by combining the first and last digit of each line.
     * @param lines the input lines
     * @return the sum of calibration values
     */
    public static int solvePartOne(List<String> lines) {
        int sum = 0;
        for (String line : lines) {
            char first = 0, last = 0;
            for (char c : line.toCharArray()) {
                if (Character.isDigit(c)) {
                    if (first == 0) first = c;
                    last = c;
                }
            }
            if (first != 0 && last != 0) {
                sum += Integer.parseInt("" + first + last);
            }
        }
        return sum;
    }

    /**
     * Solves Part 2: Sums calibration values by finding the first and last digit (spelled or numeric) per line.
     * @param lines the input lines
     * @return the sum of calibration values
     */
    public static int solvePartTwo(List<String> lines) {
        int sum = 0;
        for (String line : lines) {
            int first = -1, last = -1;
            for (int i = 0; i < line.length(); i++) {
                int digit = parseDigit(line, i);
                if (digit != -1) {
                    if (first == -1) first = digit;
                    last = digit;
                }
            }
            if (first != -1 && last != -1) {
                sum += first * 10 + last;
            }
        }
        return sum;
    }

    /**
     * Parses a digit at the given position, supporting both numeric and spelled-out digits.
     * @param line the input line
     * @param pos the position to check
     * @return the digit value (1-9) or -1 if not found
     */
    private static int parseDigit(String line, int pos) {
        char c = line.charAt(pos);
        if (Character.isDigit(c)) {
            return c - '0';
        }
        String[] words = {"zero","one","two","three","four","five","six","seven","eight","nine"};
        for (int d = 1; d <= 9; d++) {
            String word = words[d];
            if (line.startsWith(word, pos)) {
                return d;
            }
        }
        return -1;
    }
}
