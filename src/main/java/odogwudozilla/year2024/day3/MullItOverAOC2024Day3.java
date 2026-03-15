package odogwudozilla.year2024.day3;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.io.IOException;

/**
 * Mull It Over (Advent of Code 2024 Day 3)
 *
 * Scan the corrupted memory for uncorrupted mul(X,Y) instructions. Multiply X and Y for each valid instruction and sum all results.
 *
 * Official puzzle URL: https://adventofcode.com/2024/day/3
 *
 * @param args Command line arguments (not used)
 * @return void
 */
public class MullItOverAOC2024Day3 {
    private static final String INPUT_PATH = "src/main/resources/2024/day3/day3_puzzle_data.txt";
    private static final Pattern MUL_PATTERN = Pattern.compile("(?<![a-zA-Z0-9_])mul\\((\\d{1,3}),(\\d{1,3})\\)");

    /**
     * Main entry point for the solution.
     * Reads the input file and prints the results for Part 1 and Part 2.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        try {
            String input = String.join("", Files.readAllLines(Paths.get(INPUT_PATH)));
            System.out.println("Part 1: " + solvePartOne(input));
            System.out.println("Part 2: " + solvePartTwo(input));
        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1 by extracting all valid mul(X,Y) instructions and summing their products.
     * @param input The corrupted memory string
     * @return The sum of all valid multiplications
     */
    public static int solvePartOne(@org.jetbrains.annotations.NotNull String input) {
        int sum = 0;
        Matcher matcher = MUL_PATTERN.matcher(input);
        while (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            sum += x * y;
        }
        return sum;
    }

    /**
     * Solves Part 2 by handling do() and don't() instructions to enable/disable mul instructions.
     * Only enabled mul(X,Y) instructions are summed.
     * @param input The corrupted memory string
     * @return The sum of all enabled multiplications
     */
    public static int solvePartTwo(@org.jetbrains.annotations.NotNull String input) {
        int sum = 0;
        Pattern pattern = Pattern.compile("(?<![a-zA-Z0-9_])(?:(do\\(\\))|(don't\\(\\))|(mul\\((\\d{1,3}),(\\d{1,3})\\)))");
        Matcher matcher = pattern.matcher(input);
        boolean enabled = true;
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                enabled = true;
            } else if (matcher.group(2) != null) {
                enabled = false;
            } else if (matcher.group(3) != null && enabled) {
                int x = Integer.parseInt(matcher.group(4));
                int y = Integer.parseInt(matcher.group(5));
                sum += x * y;
            }
        }
        return sum;
    }
}
