package odogwudozilla.year2015.day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Advent of Code 2015 - Day 10: Elves Look, Elves Say
 *
 * This puzzle involves the look-and-say sequence (also known as the Morris number sequence).
 * Starting with a string of digits, each iteration describes the previous result by counting
 * consecutive runs of digits. For example:
 * - "1" becomes "11" (one 1)
 * - "11" becomes "21" (two 1s)
 * - "21" becomes "1211" (one 2, then one 1)
 * - "1211" becomes "111221" (one 1, one 2, then two 1s)
 * - "111221" becomes "312211" (three 1s, two 2s, then one 1)
 *
 * Part 1: Apply the process 40 times and find the length of the result.
 * Part 2: Apply the process 50 times and find the length of the result.
 *
 * @see <a href="https://adventofcode.com/2015/day/10">AoC 2015 Day 10</a>
 */
public class ElvesLookElvesSayAOC2015Day10 {

    private static final String INPUT_FILE = "src/main/resources/2015/day10/day10_puzzle_data.txt";
    private static final int PART_ONE_ITERATIONS = 40;
    private static final int PART_TWO_ITERATIONS = 50;

    public static void main(String[] args) {
        try {
            String input = readInput();

            System.out.println("Advent of Code 2015 - Day 10: Elves Look, Elves Say");
            System.out.println("=".repeat(60));

            long part1Result = solvePartOne(input);
            System.out.println("Part 1 - Length after " + PART_ONE_ITERATIONS + " iterations: " + part1Result);

            long part2Result = solvePartTwo(input);
            System.out.println("Part 2 - Length after " + PART_TWO_ITERATIONS + " iterations: " + part2Result);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1 of the puzzle by applying the look-and-say process 40 times.
     * @param input the initial sequence
     * @return the length of the resulting sequence
     */
    public static long solvePartOne(String input) {
        return applyLookAndSay(input, PART_ONE_ITERATIONS);
    }

    /**
     * Solves Part 2 of the puzzle by applying the look-and-say process 50 times.
     * @param input the initial sequence
     * @return the length of the resulting sequence
     */
    public static long solvePartTwo(String input) {
        return applyLookAndSay(input, PART_TWO_ITERATIONS);
    }

    /**
     * Applies the look-and-say transformation a specified number of times.
     * @param initial the starting sequence
     * @param iterations the number of times to apply the transformation
     * @return the length of the final sequence
     */
    private static long applyLookAndSay(String initial, int iterations) {
        String current = initial;
        for (int i = 0; i < iterations; i++) {
            current = lookAndSay(current);
        }
        return current.length();
    }

    /**
     * Applies one iteration of the look-and-say transformation.
     * Counts consecutive runs of the same digit and describes them.
     * @param input the input sequence
     * @return the transformed sequence
     */
    private static String lookAndSay(String input) {
        StringBuilder result = new StringBuilder();

        int i = 0;
        while (i < input.length()) {
            char currentDigit = input.charAt(i);
            int count = 1;

            // Count consecutive occurrences of the current digit
            while (i + count < input.length() && input.charAt(i + count) == currentDigit) {
                count++;
            }

            // Append the count and the digit to the result
            result.append(count).append(currentDigit);

            // Move to the next different digit
            i += count;
        }

        return result.toString();
    }

    /**
     * Reads the puzzle input from the file.
     * Extracts just the numeric sequence from the input text.
     * @return the puzzle input sequence
     * @throws IOException if the file cannot be read
     */
    private static String readInput() throws IOException {
        String content = Files.readString(Paths.get(INPUT_FILE)).trim();
        // Extract the number from "Your puzzle input is XXXXXXXXXX."
        if (content.contains("Your puzzle input is")) {
            content = content.replace("Your puzzle input is", "").trim();
            if (content.endsWith(".")) {
                content = content.substring(0, content.length() - 1);
            }
        }
        return content;
    }
}

