package odogwudozilla.year2023.day8;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2023 - Day 8: Haunted Wasteland
 * <p>
 * Puzzle URL: https://adventofcode.com/2023/day/8
 */
public final class HauntedWastelandAOC2023Day8 {

    private static final String INPUT_FILE = "/2023/day8/day8_puzzle_data.txt";

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
        String instructions = input.get(0).trim();
        java.util.Map<String, String[]> map = parseMap(input);
        String node = "AAA";
        int steps = 0;
        int idx = 0;
        while (!"ZZZ".equals(node)) {
            char dir = instructions.charAt(idx % instructions.length());
            node = map.get(node)[dir == 'L' ? 0 : 1];
            steps++;
            idx++;
        }
        return String.valueOf(steps);
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        String instructions = input.get(0).trim();
        java.util.Map<String, String[]> map = parseMap(input);
        java.util.List<String> nodes = new java.util.ArrayList<>();
        for (String key : map.keySet()) {
            if (key.endsWith("A")) {
                nodes.add(key);
            }
        }
        long[] cycles = new long[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            String node = nodes.get(i);
            int idx = 0;
            int steps = 0;
            while (!node.endsWith("Z")) {
                char dir = instructions.charAt(idx % instructions.length());
                node = map.get(node)[dir == 'L' ? 0 : 1];
                steps++;
                idx++;
            }
            cycles[i] = steps;
        }
        return String.valueOf(lcm(cycles));
    }

    private static java.util.Map<String, String[]> parseMap(List<String> input) {
        java.util.Map<String, String[]> map = new java.util.HashMap<>();
        for (int i = 2; i < input.size(); i++) {
            String line = input.get(i).trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(" = ");
            String key = parts[0];
            String[] lr = parts[1].substring(1, parts[1].length() - 1).split(", ");
            map.put(key, lr);
        }
        return map;
    }

    private static long lcm(long[] nums) {
        long res = nums[0];
        for (int i = 1; i < nums.length; i++) {
            res = lcm(res, nums[i]);
        }
        return res;
    }

    private static long lcm(long a, long b) {
        return a * b / gcd(a, b);
    }

    private static long gcd(long a, long b) {
        while (b != 0) {
            long t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = HauntedWastelandAOC2023Day8.class.getResourceAsStream(INPUT_FILE)) {
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
