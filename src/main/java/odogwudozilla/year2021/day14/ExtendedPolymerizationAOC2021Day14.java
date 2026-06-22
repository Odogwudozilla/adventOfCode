package odogwudozilla.year2021.day14;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Advent of Code 2021 - Day 14: Extended Polymerization
 * <p>
 * Puzzle URL: https://adventofcode.com/2021/day/14
 */
public final class ExtendedPolymerizationAOC2021Day14 {

    private static final String INPUT_FILE = "/2021/day14/day14_puzzle_data.txt";
    private static final int PART_ONE_STEPS = 10;
    private static final int PART_TWO_STEPS = 40;
    private static final String RULE_DELIMITER = " -> ";

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
        String polymer = input.get(0).trim();
        Map<String, Character> rules = parseRules(input);

        for (int step = 0; step < PART_ONE_STEPS; step++) {
            polymer = applyStep(polymer, rules);
        }

        return String.valueOf(computeFrequencyDelta(polymer));
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        String template = input.get(0).trim();
        Map<String, Character> rules = parseRules(input);
        Map<String, Long> pairCounts = initialisePairCounts(template);
        Map<Character, Long> elementCounts = initialiseElementCounts(template);

        runPairInsertionSteps(pairCounts, elementCounts, rules, PART_TWO_STEPS);
        return String.valueOf(computeFrequencyDelta(elementCounts));
    }

    /**
     * Builds initial adjacent pair frequencies from the template string.
     */
    private static Map<String, Long> initialisePairCounts(String template) {
        Map<String, Long> pairCounts = new HashMap<>();
        for (int i = 0; i < template.length() - 1; i++) {
            String pair = "" + template.charAt(i) + template.charAt(i + 1);
            pairCounts.merge(pair, 1L, Long::sum);
        }
        return pairCounts;
    }

    /**
     * Seeds element frequency counts from the initial template.
     */
    private static Map<Character, Long> initialiseElementCounts(String template) {
        Map<Character, Long> elementCounts = new HashMap<>();
        for (int i = 0; i < template.length(); i++) {
            elementCounts.merge(template.charAt(i), 1L, Long::sum);
        }
        return elementCounts;
    }

    /**
     * Runs the pair-frequency insertion process for a given number of steps.
     */
    private static void runPairInsertionSteps(Map<String, Long> pairCounts,
                                              Map<Character, Long> elementCounts,
                                              Map<String, Character> rules,
                                              int steps) {
        Map<String, Long> currentPairs = new HashMap<>(pairCounts);

        for (int step = 0; step < steps; step++) {
            Map<String, Long> nextPairs = new HashMap<>();

            for (Map.Entry<String, Long> entry : currentPairs.entrySet()) {
                String pair = entry.getKey();
                long count = entry.getValue();
                Character insert = rules.get(pair);

                if (insert == null) {
                    nextPairs.merge(pair, count, Long::sum);
                    continue;
                }

                String leftPair = "" + pair.charAt(0) + insert;
                String rightPair = "" + insert + pair.charAt(1);
                nextPairs.merge(leftPair, count, Long::sum);
                nextPairs.merge(rightPair, count, Long::sum);
                elementCounts.merge(insert, count, Long::sum);
            }

            currentPairs = nextPairs;
        }
    }

    /**
     * Computes max-min delta from already aggregated element counts.
     */
    private static long computeFrequencyDelta(Map<Character, Long> elementCounts) {
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        for (long count : elementCounts.values()) {
            min = Math.min(min, count);
            max = Math.max(max, count);
        }
        return max - min;
    }

    /**
     * Parses pair insertion rules from input lines.
     */
    private static Map<String, Character> parseRules(List<String> input) {
        Map<String, Character> rules = new HashMap<>();
        for (int i = 2; i < input.size(); i++) {
            String line = input.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] parts = line.split(RULE_DELIMITER);
            rules.put(parts[0], parts[1].charAt(0));
        }
        return rules;
    }

    /**
     * Applies one simultaneous insertion step to the polymer.
     */
    private static String applyStep(String polymer, Map<String, Character> rules) {
        StringBuilder nextPolymer = new StringBuilder(polymer.length() * 2);

        for (int i = 0; i < polymer.length() - 1; i++) {
            char left = polymer.charAt(i);
            char right = polymer.charAt(i + 1);
            nextPolymer.append(left);
            Character insert = rules.get("" + left + right);
            if (insert != null) {
                nextPolymer.append(insert);
            }
        }

        nextPolymer.append(polymer.charAt(polymer.length() - 1));
        return nextPolymer.toString();
    }

    /**
     * Computes most-common minus least-common element frequencies.
     */
    private static long computeFrequencyDelta(String polymer) {
        Map<Character, Long> counts = new HashMap<>();
        for (int i = 0; i < polymer.length(); i++) {
            char element = polymer.charAt(i);
            counts.put(element, counts.getOrDefault(element, 0L) + 1L);
        }

        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        for (long value : counts.values()) {
            min = Math.min(min, value);
            max = Math.max(max, value);
        }

        return max - min;
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = ExtendedPolymerizationAOC2021Day14.class.getResourceAsStream(INPUT_FILE)) {
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
