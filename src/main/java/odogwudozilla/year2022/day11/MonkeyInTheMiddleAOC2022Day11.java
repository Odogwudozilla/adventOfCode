package odogwudozilla.year2022.day11;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2022 - Day 11: Monkey in the Middle
 * <p>
 * Puzzle URL: https://adventofcode.com/2022/day/11
 */
public final class MonkeyInTheMiddleAOC2022Day11 {

    private static final String INPUT_FILE = "/2022/day11/day11_puzzle_data.txt";
    private static final int PART_ONE_ROUNDS = 20;
    private static final int PART_TWO_ROUNDS = 10_000;
    private static final long RELIEF_DIVISOR = 3L;
    private static final long NO_MODULUS = 0L;

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
    public static String solvePartOne(List<String> input) {
        List<Monkey> monkeys = parseMonkeys(input);
        runRounds(monkeys, PART_ONE_ROUNDS);
        return String.valueOf(computeMonkeyBusiness(monkeys));
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        List<Monkey> monkeys = parseMonkeys(input);
        long commonModulus = computeCommonModulus(monkeys);
        runRounds(monkeys, PART_TWO_ROUNDS, false, commonModulus);
        return String.valueOf(computeMonkeyBusiness(monkeys));
    }

    private static List<Monkey> parseMonkeys(List<String> input) {
        List<Monkey> monkeys = new ArrayList<>();
        List<String> block = new ArrayList<>();

        for (String line : input) {
            if (line == null || line.isBlank()) {
                if (!block.isEmpty()) {
                    monkeys.add(parseMonkey(block));
                    block.clear();
                }
                continue;
            }
            block.add(line.trim());
        }

        if (!block.isEmpty()) {
            monkeys.add(parseMonkey(block));
        }

        return monkeys;
    }

    private static Monkey parseMonkey(List<String> block) {
        Deque<Long> items = parseItems(block.get(1));

        String operationExpression = block.get(2).substring(block.get(2).indexOf("=") + 1).trim();
        String[] operationParts = operationExpression.split("\\s+");
        char operation = operationParts[1].charAt(0);
        String rightHandSide = operationParts[2];

        long divisor = parseTrailingNumber(block.get(3));
        int ifTrue = (int) parseTrailingNumber(block.get(4));
        int ifFalse = (int) parseTrailingNumber(block.get(5));

        return new Monkey(items, operation, rightHandSide, divisor, ifTrue, ifFalse);
    }

    private static Deque<Long> parseItems(String startingItemsLine) {
        Deque<Long> items = new ArrayDeque<>();
        String[] parts = startingItemsLine.split(":", 2);
        if (parts.length < 2 || parts[1].isBlank()) {
            return items;
        }

        String[] tokens = parts[1].split(",");
        for (String token : tokens) {
            String trimmed = token.trim();
            if (!trimmed.isEmpty()) {
                items.addLast(Long.parseLong(trimmed));
            }
        }
        return items;
    }

    private static long parseTrailingNumber(String line) {
        String[] tokens = line.split("\\s+");
        return Long.parseLong(tokens[tokens.length - 1]);
    }

    private static void runRounds(List<Monkey> monkeys, int rounds) {
        runRounds(monkeys, rounds, true, NO_MODULUS);
    }

    private static void runRounds(List<Monkey> monkeys, int rounds, boolean applyRelief, long modulus) {
        for (int round = 0; round < rounds; round++) {
            for (Monkey monkey : monkeys) {
                while (!monkey.items.isEmpty()) {
                    long oldValue = monkey.items.removeFirst();
                    long newValue = applyOperation(oldValue, monkey);
                    long adjustedValue = applyRelief ? newValue / RELIEF_DIVISOR : newValue;
                    if (modulus > NO_MODULUS) {
                        adjustedValue %= modulus;
                    }

                    int destination = (adjustedValue % monkey.divisor == 0)
                            ? monkey.ifTrue
                            : monkey.ifFalse;

                    monkeys.get(destination).items.addLast(adjustedValue);
                    monkey.inspections++;
                }
            }
        }
    }

    private static long computeCommonModulus(List<Monkey> monkeys) {
        long modulus = 1L;
        for (Monkey monkey : monkeys) {
            modulus = lcm(modulus, monkey.divisor);
        }
        return modulus;
    }

    private static long lcm(long a, long b) {
        return a / gcd(a, b) * b;
    }

    private static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private static long applyOperation(long oldValue, Monkey monkey) {
        long rhsValue = "old".equals(monkey.rightHandSide)
                ? oldValue
                : Long.parseLong(monkey.rightHandSide);

        if (monkey.operation == '+') {
            return oldValue + rhsValue;
        }
        return oldValue * rhsValue;
    }

    private static long computeMonkeyBusiness(List<Monkey> monkeys) {
        long highest = 0L;
        long secondHighest = 0L;

        for (Monkey monkey : monkeys) {
            long inspections = monkey.inspections;
            if (inspections > highest) {
                secondHighest = highest;
                highest = inspections;
            } else if (inspections > secondHighest) {
                secondHighest = inspections;
            }
        }

        return highest * secondHighest;
    }

    private static final class Monkey {
        private final Deque<Long> items;
        private final char operation;
        private final String rightHandSide;
        private final long divisor;
        private final int ifTrue;
        private final int ifFalse;
        private long inspections;

        private Monkey(Deque<Long> items,
                       char operation,
                       String rightHandSide,
                       long divisor,
                       int ifTrue,
                       int ifFalse) {
            this.items = items;
            this.operation = operation;
            this.rightHandSide = rightHandSide;
            this.divisor = divisor;
            this.ifTrue = ifTrue;
            this.ifFalse = ifFalse;
            this.inspections = 0L;
        }
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = MonkeyInTheMiddleAOC2022Day11.class.getResourceAsStream(INPUT_FILE)) {
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
