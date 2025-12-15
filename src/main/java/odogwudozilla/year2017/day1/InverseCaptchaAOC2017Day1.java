package odogwudozilla.year2017.day1;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Advent of Code 2017 Day 1: Inverse Captcha
 *
 * The captcha requires reviewing a sequence of digits and finding the sum of all digits
 * that match the next digit in the list. The list is circular, so the digit after the
 * last digit is the first digit in the list.
 *
 * Part 1: Find the sum of all digits that match the next digit.
 * Part 2: Find the sum of all digits that match the digit halfway around the circular list.
 *
 * https://adventofcode.com/2017/day/1
 */
public class InverseCaptchaAOC2017Day1 {

    public static void main(String[] args) {
        try {
            String filePath = "src/main/resources/2017/day1/day1_puzzle_data.txt";
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            String input = lines.get(0).trim();

            int part1Result = solvePart1(input);
            int part2Result = solvePart2(input);

            System.out.println("Part 1: " + part1Result);
            System.out.println("Part 2: " + part2Result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Solve Part 1: Sum all digits that match the next digit in a circular list.
     */
    private static int solvePart1(String input) {
        int sum = 0;
        for (int i = 0; i < input.length(); i++) {
            int current = Character.getNumericValue(input.charAt(i));
            int next = Character.getNumericValue(input.charAt((i + 1) % input.length()));
            if (current == next) {
                sum += current;
            }
        }
        return sum;
    }

    /**
     * Solve Part 2: Sum all digits that match the digit halfway around the circular list.
     */
    private static int solvePart2(String input) {
        int sum = 0;
        int halfway = input.length() / 2;
        for (int i = 0; i < input.length(); i++) {
            int current = Character.getNumericValue(input.charAt(i));
            int opposite = Character.getNumericValue(input.charAt((i + halfway) % input.length()));
            if (current == opposite) {
                sum += current;
            }
        }
        return sum;
    }
}

