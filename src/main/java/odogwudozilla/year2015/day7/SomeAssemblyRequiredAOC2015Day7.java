package odogwudozilla.year2015.day7;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Advent of Code 2015 - Day 7: Some Assembly Required
 * <p>
 * Puzzle URL: https://adventofcode.com/2015/day/7
 */
public final class SomeAssemblyRequiredAOC2015Day7 {

    private static final String INPUT_FILE = "/2015/day7/day7_puzzle_data.txt";

    /** Mask applied after every gate operation to enforce 16-bit unsigned semantics. */
    private static final int MASK_16_BIT = 0xFFFF;

    /** Instruction token count for a unary NOT operation (e.g. {@code NOT x}). */
    private static final int TOKEN_COUNT_NOT = 2;

    /** Instruction token count for a binary gate (e.g. {@code x AND y}). */
    private static final int TOKEN_COUNT_BINARY = 3;

    /**
     * The Part 1 answer used to seed wire {@code b} before the Part 2
     * re-evaluation. Defined as a named constant to avoid a magic number.
     */
    private static final int PART_ONE_ANSWER_FOR_WIRE_B = 956;

    /**
     * Entry point. Reads puzzle input and prints solutions for Part 1 and Part 2.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        List<String> input = readInput();
        System.out.println("Part 1: " + solvePartOne(input));
        System.out.println("Part 2: " + solvePartTwo(input));
    }

    /**
     * Solves Part 1: evaluates the logic circuit and returns the signal on wire {@code a}.
     *
     * @param input list of instruction lines from the puzzle input
     * @return the 16-bit unsigned signal value on wire {@code a} as a string
     */
    public static @NotNull String solvePartOne(@NotNull List<String> input) {
        Map<String, String[]> circuit = parseCircuit(input);
        Map<String, Integer> cache = new HashMap<>();
        return String.valueOf(evaluate("a", circuit, cache));
    }

    /**
     * Solves Part 2: overrides wire {@code b} with the Part 1 answer
     * ({@value PART_ONE_ANSWER_FOR_WIRE_B}), resets all other wire values,
     * and re-evaluates the circuit to obtain the new signal on wire {@code a}.
     *
     * <p>The Part 1 evaluator is reused unchanged. The only difference from
     * Part 1 is that the memo cache is seeded with
     * {@code ("b", PART_ONE_ANSWER_FOR_WIRE_B)} before recursion begins,
     * so wire {@code b}'s original instruction is never reached.
     *
     * @param input list of instruction lines from the puzzle input
     * @return the new 16-bit unsigned signal value on wire {@code a} as a string
     */
    public static @NotNull String solvePartTwo(@NotNull List<String> input) {
        Map<String, String[]> circuit = parseCircuit(input);
        Map<String, Integer> cache = new HashMap<>();
        // Seed wire "b" with the Part 1 answer; all other wires start uncached.
        cache.put("b", PART_ONE_ANSWER_FOR_WIRE_B);
        return String.valueOf(evaluate("a", circuit, cache));
    }

    // -------------------------------------------------------------------------
    // Parsing
    // -------------------------------------------------------------------------

    /**
     * Parses all instruction lines into a circuit map.
     * <p>
     * Each entry maps a wire name to the raw token array that defines its source
     * expression (the left-hand side of the {@code ->} separator).
     *
     * @param lines raw puzzle input lines
     * @return map of wire name to its instruction tokens
     */
    private static @NotNull Map<String, String[]> parseCircuit(@NotNull List<String> lines) {
        Map<String, String[]> circuit = new HashMap<>();
        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            // Split "expression -> targetWire"
            String[] parts = line.split(" -> ");
            String targetWire = parts[1].trim();
            String[] tokens = parts[0].trim().split(" ");
            circuit.put(targetWire, tokens);
        }
        return circuit;
    }

    // -------------------------------------------------------------------------
    // Evaluation
    // -------------------------------------------------------------------------

    /**
     * Recursively resolves the 16-bit signal for a wire or numeric literal token,
     * using {@code cache} to avoid redundant recomputation.
     *
     * <p>Instruction shapes handled:
     * <ul>
     *   <li>Numeric literal  — {@code 123}</li>
     *   <li>Wire passthrough — {@code someWire}</li>
     *   <li>NOT             — {@code NOT x}</li>
     *   <li>Binary gate     — {@code x AND y}, {@code x OR y},
     *                          {@code x LSHIFT n}, {@code x RSHIFT n}</li>
     * </ul>
     *
     * @param token   wire name or numeric literal to resolve
     * @param circuit map of wire name to its raw instruction tokens
     * @param cache   memoisation store; resolved values are written here
     * @return the 16-bit unsigned integer value for the given token
     * @throws IllegalArgumentException if an unknown gate operator is encountered
     */
    private static int evaluate(@NotNull String token,
                                @NotNull Map<String, String[]> circuit,
                                @NotNull Map<String, Integer> cache) {
        // Numeric literal — evaluate immediately without a map lookup
        if (Character.isDigit(token.charAt(0))) {
            return Integer.parseInt(token);
        }

        // Return cached result if already computed
        if (cache.containsKey(token)) {
            return cache.get(token);
        }

        String[] tokens = circuit.get(token);
        int result;

        if (tokens.length == 1) {
            // Direct assignment: e.g. "123 -> x" or "someWire -> x"
            result = evaluate(tokens[0], circuit, cache) & MASK_16_BIT;

        } else if (tokens.length == TOKEN_COUNT_NOT) {
            // Unary NOT: "NOT x"
            int operand = evaluate(tokens[1], circuit, cache);
            result = (~operand) & MASK_16_BIT;

        } else if (tokens.length == TOKEN_COUNT_BINARY) {
            // Binary gate: "a OP b"
            int left = evaluate(tokens[0], circuit, cache);
            int right = evaluate(tokens[2], circuit, cache);
            String operator = tokens[1];
            result = switch (operator) {
                case "AND"    -> (left & right) & MASK_16_BIT;
                case "OR"     -> (left | right) & MASK_16_BIT;
                case "LSHIFT" -> (left << right) & MASK_16_BIT;
                case "RSHIFT" -> (left >>> right) & MASK_16_BIT;
                default -> throw new IllegalArgumentException(
                        "Unknown gate operator: " + operator);
            };
        } else {
            throw new IllegalArgumentException(
                    "Unrecognised instruction shape for wire: " + token);
        }

        cache.put(token, result);
        return result;
    }

    // -------------------------------------------------------------------------
    // Input reading
    // -------------------------------------------------------------------------

    /**
     * Reads the puzzle input file from the classpath.
     *
     * @return list of input lines
     */
    private static @NotNull List<String> readInput() {
        try (InputStream stream = SomeAssemblyRequiredAOC2015Day7.class.getResourceAsStream(INPUT_FILE)) {
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
