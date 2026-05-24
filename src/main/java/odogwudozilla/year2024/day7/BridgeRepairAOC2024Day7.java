package odogwudozilla.year2024.day7;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.jetbrains.annotations.NotNull;

/**
 * Advent of Code 2024 - Day 7: Bridge Repair
 * <p>
 * Puzzle URL: https://adventofcode.com/2024/day/7
 */
public final class BridgeRepairAOC2024Day7 {

    private static final String INPUT_FILE = "/2024/day7/day7_puzzle_data.txt";

    /** Delimiter used to separate the test value from the operand list on each line. */
    private static final String TARGET_SEPARATOR = ": ";

    /** Maximum number of parts produced when splitting a line on {@link #TARGET_SEPARATOR}. */
    private static final int TARGET_SPLIT_LIMIT = 2;

    /** Delimiter used between individual operand values. */
    private static final String OPERAND_SEPARATOR = " ";

    /** The decimal number base used for arithmetic digit-shifting in the {@code ||} concatenation operator. */
    private static final int DECIMAL_BASE = 10;

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
     * <p>
     * For each calibration equation, tries all combinations of {@code +} and {@code *}
     * operators between the operands (evaluated strictly left-to-right) using recursive
     * DFS with early pruning. Returns the sum of target values for satisfiable equations.
     * @param input list of input lines
     * @return the Part 1 answer as a String
     */
    public static @NotNull String solvePartOne(@NotNull List<String> input) {
        long total = 0L;
        for (String line : input) {
            if (line.isBlank()) {
                continue;
            }
            String[] parts = line.split(TARGET_SEPARATOR, TARGET_SPLIT_LIMIT);
            long target = Long.parseLong(parts[0].trim());
            long[] operands = parseOperands(parts[1].trim());
            if (canSolve(operands, 1, operands[0], target)) {
                total += target;
            }
        }
        return String.valueOf(total);
    }

    /**
     * Solves Part 2 of the puzzle.
     * <p>
     * Extends the Part 1 DFS to also try the digit-concatenation operator {@code ||} at each
     * operator slot (branching factor 3 per slot). Evaluated strictly left-to-right with the
     * same early-pruning rule as Part 1. Returns the sum of target values for all satisfiable
     * equations.
     * @param input list of input lines
     * @return the Part 2 answer as a String
     */
    public static @NotNull String solvePartTwo(@NotNull List<String> input) {
        long total = 0L;
        for (String line : input) {
            if (line.isBlank()) {
                continue;
            }
            String[] parts = line.split(TARGET_SEPARATOR, TARGET_SPLIT_LIMIT);
            long target = Long.parseLong(parts[0].trim());
            long[] operands = parseOperands(parts[1].trim());
            if (canSolveWithConcat(operands, 1, operands[0], target)) {
                total += target;
            }
        }
        return String.valueOf(total);
    }

    /**
     * Recursive DFS helper that evaluates all left-to-right operator combinations.
     * <p>
     * At each position, tries addition and multiplication of the next operand onto the
     * running accumulator. Prunes a branch early when the accumulator already exceeds the
     * target (safe because all operand values are positive).
     * @param operands    the sequence of numbers in the equation
     * @param index       the current position in operands (next value to process)
     * @param accumulator the running value produced by evaluating left to right so far
     * @param target      the test value the equation must equal
     * @return {@code true} if any operator combination produces {@code target}
     */
    private static boolean canSolve(@NotNull long[] operands, int index, long accumulator, long target) {
        if (index == operands.length) {
            return accumulator == target;
        }
        // Early pruning: accumulator can only grow (+ and * with positive numbers),
        // so there is no point continuing once it exceeds the target.
        if (accumulator > target) {
            return false;
        }
        long next = operands[index];
        return canSolve(operands, index + 1, accumulator + next, target)
            || canSolve(operands, index + 1, accumulator * next, target);
    }

    /**
     * Recursive DFS helper that evaluates all left-to-right operator combinations using
     * {@code +}, {@code *}, and digit concatenation {@code ||}.
     * <p>
     * At each position, tries addition, multiplication, and concatenation of the next operand
     * onto the running accumulator. Prunes a branch early when the accumulator already exceeds
     * the target (safe because all three operations with positive numbers can only grow the
     * accumulator).
     * @param operands    the sequence of numbers in the equation
     * @param index       the current position in operands (next value to process)
     * @param accumulator the running value produced by evaluating left to right so far
     * @param target      the test value the equation must equal
     * @return {@code true} if any operator combination (including {@code ||}) produces {@code target}
     */
    private static boolean canSolveWithConcat(@NotNull long[] operands, int index, long accumulator, long target) {
        if (index == operands.length) {
            return accumulator == target;
        }
        if (accumulator > target) {
            return false;
        }
        long next = operands[index];
        return canSolveWithConcat(operands, index + 1, accumulator + next, target)
            || canSolveWithConcat(operands, index + 1, accumulator * next, target)
            || canSolveWithConcat(operands, index + 1, concat(accumulator, next), target);
    }

    /**
     * Arithmetic digit concatenation of two non-negative long values.
     * <p>
     * Equivalent to appending the decimal digits of {@code b} after those of {@code a}.
     * For example, {@code concat(12, 345) = 12345}. Uses {@code String.valueOf(b).length()}
     * to determine the number of digits in {@code b}, which correctly handles {@code b = 0}
     * (returns {@code 1} digit) unlike {@code Math.log10}-based approaches.
     * @param a the left operand
     * @param b the right operand (whose digits are appended)
     * @return the concatenated value
     */
    private static long concat(long a, long b) {
        return a * (long) Math.pow(DECIMAL_BASE, String.valueOf(b).length()) + b;
    }

    /**
     * @param operandsPart the substring to the right of ": ", e.g. {@code "81 40 27"}
     * @return array of operand values as {@code long}
     */
    private static long @NotNull [] parseOperands(@NotNull String operandsPart) {
        String[] tokens = operandsPart.split(OPERAND_SEPARATOR);
        long[] values = new long[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            values[i] = Long.parseLong(tokens[i].trim());
        }
        return values;
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static @NotNull List<String> readInput() {
        try (InputStream stream = BridgeRepairAOC2024Day7.class.getResourceAsStream(INPUT_FILE)) {
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
