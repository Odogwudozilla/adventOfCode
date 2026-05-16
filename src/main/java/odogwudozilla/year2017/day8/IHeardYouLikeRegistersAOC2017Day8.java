package odogwudozilla.year2017.day8;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Advent of Code 2017 - Day 8: I Heard You Like Registers
 * <p>
 * Puzzle URL: https://adventofcode.com/2017/day/8
 */
public final class IHeardYouLikeRegistersAOC2017Day8 {

    private static final String INPUT_FILE = "/2017/day8/day8_puzzle_data.txt";

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
     * Solves Part 1 of the puzzle.
     * @param input list of input lines
     * @return the Part 1 answer
     */
    private static String solvePartOne(List<String> input) {
        Map<String, Integer> registers = new HashMap<>();

        for (String line : input) {
            if (line.trim().isEmpty()) {
                continue;
            }

            // Parse: b inc 5 if a > 1
            String[] parts = line.split(" if ");
            String[] instruction = parts[0].trim().split(" ");
            String condition = parts[1].trim();

            String targetRegister = instruction[0];
            String operation = instruction[1];
            int value = Integer.parseInt(instruction[2]);

            registers.putIfAbsent(targetRegister, 0);

            if (evaluateCondition(registers, condition)) {
                int current = registers.get(targetRegister);
                if ("inc".equals(operation)) {
                    registers.put(targetRegister, current + value);
                } else {
                    registers.put(targetRegister, current - value);
                }
            }
        }

        int maxValue = registers.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        return String.valueOf(maxValue);
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        Map<String, Integer> registers = new HashMap<>();
        int maxEver = 0;

        for (String line : input) {
            if (line.trim().isEmpty()) {
                continue;
            }

            // Parse: b inc 5 if a > 1
            String[] parts = line.split(" if ");
            String[] instruction = parts[0].trim().split(" ");
            String condition = parts[1].trim();

            String targetRegister = instruction[0];
            String operation = instruction[1];
            int value = Integer.parseInt(instruction[2]);

            registers.putIfAbsent(targetRegister, 0);

            if (evaluateCondition(registers, condition)) {
                int current = registers.get(targetRegister);
                int newValue;
                if ("inc".equals(operation)) {
                    newValue = current + value;
                } else {
                    newValue = current - value;
                }
                registers.put(targetRegister, newValue);
                maxEver = Math.max(maxEver, newValue);
            }
        }

        return String.valueOf(maxEver);
    }

    /**
     * Evaluates a condition string against the current register values.
     * @param registers map of register values
     * @param condition condition string (e.g., "a > 1")
     * @return whether the condition is true
     */
    private static boolean evaluateCondition(Map<String, Integer> registers, String condition) {
        // Extract the parts: register, operator, value
        String[] parts = condition.split(" ");
        String register = parts[0];
        String operator = parts[1];
        int compareValue = Integer.parseInt(parts[2]);

        int registerValue = registers.getOrDefault(register, 0);

        return switch (operator) {
            case ">" -> registerValue > compareValue;
            case "<" -> registerValue < compareValue;
            case ">=" -> registerValue >= compareValue;
            case "<=" -> registerValue <= compareValue;
            case "==" -> registerValue == compareValue;
            case "!=" -> registerValue != compareValue;
            default -> false;
        };
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = IHeardYouLikeRegistersAOC2017Day8.class.getResourceAsStream(INPUT_FILE)) {
            if (stream == null) {
                throw new IllegalStateException("Input file not found: " + INPUT_FILE);
            }
            Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8);
            java.util.List<String> lines = new java.util.ArrayList<>();
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            return lines;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read input file", e);
        }
    }
}
