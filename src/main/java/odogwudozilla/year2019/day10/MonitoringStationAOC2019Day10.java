package odogwudozilla.year2019.day10;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2019 - Day 10: Monitoring Station
 * <p>
 * Puzzle URL: https://adventofcode.com/2019/day/10
 */
public final class MonitoringStationAOC2019Day10 {

    private static final String INPUT_FILE = "/2019/day10/day10_puzzle_data.txt";

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
        // Parse asteroid positions
        java.util.List<int[]> asteroids = new java.util.ArrayList<>();
        for (int y = 0; y < input.size(); y++) {
            String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == '#') {
                    asteroids.add(new int[]{x, y});
                }
            }
        }

        int maxVisible = 0;
        for (int[] a : asteroids) {
            java.util.Set<String> angles = new java.util.HashSet<>();
            for (int[] b : asteroids) {
                if (a == b) continue;
                int dx = b[0] - a[0];
                int dy = b[1] - a[1];
                int gcd = gcd(Math.abs(dx), Math.abs(dy));
                if (gcd != 0) {
                    dx /= gcd;
                    dy /= gcd;
                }
                angles.add(dx + "," + dy);
            }
            if (angles.size() > maxVisible) {
                maxVisible = angles.size();
            }
        }
        return String.valueOf(maxVisible);
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        // Parse asteroid positions
        java.util.List<int[]> asteroids = new java.util.ArrayList<>();
        for (int y = 0; y < input.size(); y++) {
            String line = input.get(y);
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == '#') {
                    asteroids.add(new int[]{x, y});
                }
            }
        }

        // Find best station location (as in Part 1)
        int maxVisible = 0;
        int[] station = null;
        for (int[] a : asteroids) {
            java.util.Set<String> angles = new java.util.HashSet<>();
            for (int[] b : asteroids) {
                if (a == b) continue;
                int dx = b[0] - a[0];
                int dy = b[1] - a[1];
                int gcd = gcd(Math.abs(dx), Math.abs(dy));
                if (gcd != 0) {
                    dx /= gcd;
                    dy /= gcd;
                }
                angles.add(dx + "," + dy);
            }
            if (angles.size() > maxVisible) {
                maxVisible = angles.size();
                station = a;
            }
        }
        if (station == null) return "no station";

        // Map: angle -> list of asteroids (sorted by distance)
        java.util.Map<Double, java.util.List<int[]>> angleMap = new java.util.HashMap<>();
        for (int[] b : asteroids) {
            if (b == station) continue;
            int dx = b[0] - station[0];
            int dy = b[1] - station[1];
            double angle = Math.atan2(dx, -dy); // Up is 0, right is pi/2, etc.
            if (angle < 0) angle += 2 * Math.PI;
            double dist = dx * dx + dy * dy;
            angleMap.computeIfAbsent(angle, k -> new java.util.ArrayList<>()).add(new int[]{b[0], b[1], (int)dist});
        }
        // Sort each angle's list by distance (closest first)
        for (java.util.List<int[]> list : angleMap.values()) {
            list.sort(java.util.Comparator.comparingInt(a -> a[2]));
        }
        // Sort angles in increasing order
        java.util.List<Double> angles = new java.util.ArrayList<>(angleMap.keySet());
        angles.sort(Double::compareTo);

        int vaporised = 0;
        int result = -1;
        outer: while (true) {
            for (double angle : angles) {
                java.util.List<int[]> list = angleMap.get(angle);
                if (list.isEmpty()) continue;
                int[] ast = list.remove(0);
                vaporised++;
                if (vaporised == 200) {
                    result = ast[0] * 100 + ast[1];
                    break outer;
                }
            }
            boolean anyLeft = false;
            for (java.util.List<int[]> list : angleMap.values()) {
                if (!list.isEmpty()) { anyLeft = true; break; }
            }
            if (!anyLeft) break;
        }
        return String.valueOf(result);
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = MonitoringStationAOC2019Day10.class.getResourceAsStream(INPUT_FILE)) {
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

    /**
     * Computes the greatest common divisor of two integers.
     * @param a first integer
     * @param b second integer
     * @return the GCD
     */
    private static int gcd(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return a;
    }
}
