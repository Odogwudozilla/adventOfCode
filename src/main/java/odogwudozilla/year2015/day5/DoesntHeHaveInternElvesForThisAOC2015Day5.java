package odogwudozilla.year2015.day5;

import org.jetbrains.annotations.NotNull;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;

/**
 * Day 5: Doesn't He Have Intern-Elves For This?
 * Santa needs help figuring out which strings in his text file are naughty or nice.
 *
 * A nice string is one with all of the following properties:
 * - It contains at least three vowels (aeiou only).
 * - It contains at least one letter that appears twice in a row.
 * - It does not contain the strings ab, cd, pq, or xy.
 *
 * Official puzzle URL: https://adventofcode.com/2015/day/5
 */
public class DoesntHeHaveInternElvesForThisAOC2015Day5 {
    public static void main(String[] args) {
        String inputFile = "src/main/resources/2015/day5/day5_puzzle_data.txt";
        try {
            List<String> lines = Files.readAllLines(Paths.get(inputFile));
            System.out.println("Part 1: " + solvePartOne(lines));
            System.out.println("Part 2: " + solvePartTwo(lines));
        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1: Counts the number of nice strings according to the rules.
     * @param lines the list of input strings
     * @return the count of nice strings
     */
    public static int solvePartOne(@NotNull List<String> lines) {
        int niceCount = 0;
        for (String s : lines) {
            if (isNiceString(s)) {
                niceCount++;
            }
        }
        return niceCount;
    }

    /**
     * Checks if a string is nice according to the puzzle rules.
     * @param s the string to check
     * @return true if nice, false otherwise
     */
    private static boolean isNiceString(@NotNull String s) {
        // Check for at least three vowels
        int vowelCount = 0;
        for (char c : s.toCharArray()) {
            if ("aeiou".indexOf(c) >= 0) {
                vowelCount++;
            }
        }
        if (vowelCount < 3) {
            return false;
        }
        // Check for double letter
        boolean hasDouble = false;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == s.charAt(i - 1)) {
                hasDouble = true;
                break;
            }
        }
        if (!hasDouble) {
            return false;
        }
        // Check for forbidden substrings
        String[] forbidden = {"ab", "cd", "pq", "xy"};
        for (String f : forbidden) {
            if (s.contains(f)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Solves Part 2: Counts the number of nice strings according to the new rules.
     * @param lines the list of input strings
     * @return the count of nice strings for Part 2
     */
    public static int solvePartTwo(@NotNull List<String> lines) {
        int niceCount = 0;
        for (String s : lines) {
            if (isNiceStringPartTwo(s)) {
                niceCount++;
            }
        }
        return niceCount;
    }

    /**
     * Checks if a string is nice according to Part 2 rules.
     * @param s the string to check
     * @return true if nice, false otherwise
     */
    private static boolean isNiceStringPartTwo(@NotNull String s) {
        // Rule 1: Pair of any two letters appears at least twice without overlapping
        boolean hasPair = false;
        for (int i = 0; i < s.length() - 1; i++) {
            String pair = s.substring(i, i + 2);
            if (s.indexOf(pair, i + 2) != -1) {
                hasPair = true;
                break;
            }
        }
        if (!hasPair) {
            return false;
        }
        // Rule 2: At least one letter repeats with exactly one letter between
        for (int i = 0; i < s.length() - 2; i++) {
            if (s.charAt(i) == s.charAt(i + 2)) {
                return true;
            }
        }
        return false;
    }
}
