package odogwudozilla.year2018.day12;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2018 - Day 12: Subterranean Sustainability
 * <p>
 * Puzzle URL: https://adventofcode.com/2018/day/12
 */
public final class SubterraneanSustainabilityAOC2018Day12 {

    private static final String INPUT_FILE = "/2018/day12/day12_puzzle_data.txt";

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
        // Parse initial state and rules
        String initialState = null;
        java.util.Map<String, Character> rules = new java.util.HashMap<>();
        for (String line : input) {
            if (line.startsWith("initial state:")) {
                initialState = line.substring("initial state:".length()).trim();
            } else if (line.contains("=>")) {
                String[] parts = line.split("=>");
                if (parts.length == 2) {
                    rules.put(parts[0].trim(), parts[1].trim().charAt(0));
                }
            }
        }
        if (initialState == null) return "invalid input";

        // Simulate 20 generations
        java.util.Set<Integer> pots = new java.util.HashSet<>();
        for (int i = 0; i < initialState.length(); i++) {
            if (initialState.charAt(i) == '#') pots.add(i);
        }
        for (int gen = 0; gen < 20; gen++) {
            java.util.Set<Integer> next = new java.util.HashSet<>();
            int min = pots.stream().min(Integer::compareTo).orElse(0) - 2;
            int max = pots.stream().max(Integer::compareTo).orElse(0) + 2;
            for (int i = min; i <= max; i++) {
                StringBuilder pat = new StringBuilder();
                for (int d = -2; d <= 2; d++) {
                    pat.append(pots.contains(i + d) ? '#' : '.');
                }
                if (rules.getOrDefault(pat.toString(), '.') == '#') {
                    next.add(i);
                }
            }
            pots = next;
        }
        int sum = pots.stream().mapToInt(Integer::intValue).sum();
        return String.valueOf(sum);
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        // Parse initial state and rules
        String initialState = null;
        java.util.Map<String, Character> rules = new java.util.HashMap<>();
        for (String line : input) {
            if (line.startsWith("initial state:")) {
                initialState = line.substring("initial state:".length()).trim();
            } else if (line.contains("=>")) {
                String[] parts = line.split("=>");
                if (parts.length == 2) {
                    rules.put(parts[0].trim(), parts[1].trim().charAt(0));
                }
            }
        }
        if (initialState == null) return "invalid input";

        // Simulate until pattern stabilizes, then extrapolate
        java.util.Set<Integer> pots = new java.util.HashSet<>();
        for (int i = 0; i < initialState.length(); i++) {
            if (initialState.charAt(i) == '#') pots.add(i);
        }
        long prevSum = 0;
        long prevDelta = 0;
        int stableCount = 0;
        long generations = 50000000000L;
        int lastGen = 0;
        for (int gen = 1; gen <= 2000; gen++) { // 2000 is arbitrary, should stabilize much earlier
            java.util.Set<Integer> next = new java.util.HashSet<>();
            int min = pots.stream().min(Integer::compareTo).orElse(0) - 2;
            int max = pots.stream().max(Integer::compareTo).orElse(0) + 2;
            for (int i = min; i <= max; i++) {
                StringBuilder pat = new StringBuilder();
                for (int d = -2; d <= 2; d++) {
                    pat.append(pots.contains(i + d) ? '#' : '.');
                }
                if (rules.getOrDefault(pat.toString(), '.') == '#') {
                    next.add(i);
                }
            }
            pots = next;
            long sum = pots.stream().mapToLong(Integer::longValue).sum();
            long delta = sum - prevSum;
            if (delta == prevDelta) {
                stableCount++;
            } else {
                stableCount = 0;
            }
            if (stableCount >= 100) { // pattern is stable for 100 generations
                long remaining = generations - gen;
                long result = sum + remaining * delta;
                return String.valueOf(result);
            }
            prevSum = sum;
            prevDelta = delta;
            lastGen = gen;
        }
        // If not stabilized, fallback to last computed sum
        return String.valueOf(pots.stream().mapToLong(Integer::longValue).sum());
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = SubterraneanSustainabilityAOC2018Day12.class.getResourceAsStream(INPUT_FILE)) {
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
