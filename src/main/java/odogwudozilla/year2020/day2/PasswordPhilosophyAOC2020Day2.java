package odogwudozilla.year2020.day2;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Password Philosophy (Advent of Code 2020 Day 2)
 * Validate passwords against two different policies.
 * Part 1 checks if a letter appears within a specified range.
 * Part 2 checks if a letter appears in exactly one of two specified positions.
 *
 * Official puzzle URL: https://adventofcode.com/2020/day/2
 *
 * @author Advent of Code assistant
 */
public class PasswordPhilosophyAOC2020Day2 {
    private static final String INPUT_PATH = "src/main/resources/2020/day2/day2_puzzle_data.txt";
    private static final Pattern POLICY_PATTERN = Pattern.compile("(\\d+)-(\\d+) (\\w): (\\w+)");

    /**
     * Main method to solve Part 1 of the puzzle.
     * Reads the input file and counts valid passwords according to the first policy.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(INPUT_PATH));
        int validCount = solvePartOne(lines);
        System.out.println("main - Part 1 valid password count: " + validCount);

        int validCountPart2 = solvePartTwo(lines);
        System.out.println("main - Part 2 valid password count: " + validCountPart2);
    }

    /**
     * Counts valid passwords according to the first policy (Part 1).
     * @param lines List of password entries
     * @return Number of valid passwords
     */
    private static int solvePartOne(List<String> lines) {
        int validCount = 0;
        for (String line : lines) {
            Matcher m = POLICY_PATTERN.matcher(line);
            if (m.matches()) {
                int min = Integer.parseInt(m.group(1));
                int max = Integer.parseInt(m.group(2));
                char letter = m.group(3).charAt(0);
                String password = m.group(4);
                long count = password.chars().filter(c -> c == letter).count();
                if (count >= min && count <= max) {
                    validCount++;
                }
            }
        }
        return validCount;
    }

    /**
     * Counts valid passwords according to the second policy (Part 2).
     * @param lines List of password entries
     * @return Number of valid passwords
     */
    private static int solvePartTwo(List<String> lines) {
        int validCountPart2 = 0;
        for (String line : lines) {
            Matcher m = POLICY_PATTERN.matcher(line);
            if (m.matches()) {
                int pos1 = Integer.parseInt(m.group(1));
                int pos2 = Integer.parseInt(m.group(2));
                char letter = m.group(3).charAt(0);
                String password = m.group(4);
                boolean match1 = pos1 <= password.length() && password.charAt(pos1 - 1) == letter;
                boolean match2 = pos2 <= password.length() && password.charAt(pos2 - 1) == letter;
                if (match1 ^ match2) {
                    validCountPart2++;
                }
            }
        }
        return validCountPart2;
    }
}
