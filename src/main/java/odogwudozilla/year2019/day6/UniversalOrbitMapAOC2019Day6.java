package odogwudozilla.year2019.day6;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2019 - Day 6: Universal Orbit Map
 * <p>
 * Puzzle URL: https://adventofcode.com/2019/day/6
 */
public final class UniversalOrbitMapAOC2019Day6 {

    private static final String INPUT_FILE = "/2019/day6/day6_puzzle_data.txt";

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
        // Build orbit map: child -> parent
        java.util.Map<String, String> orbitMap = new java.util.HashMap<>();
        for (String line : input) {
            if (line == null || line.trim().isEmpty()) continue;
            String[] parts = line.trim().split("\\)");
            if (parts.length != 2) continue;
            orbitMap.put(parts[1], parts[0]);
        }
        // Count total orbits (direct + indirect)
        int totalOrbits = 0;
        for (String obj : orbitMap.keySet()) {
            String current = obj;
            while (orbitMap.containsKey(current)) {
                totalOrbits++;
                current = orbitMap.get(current);
            }
        }
        return String.valueOf(totalOrbits);
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        // Build orbit map: child -> parent
        java.util.Map<String, String> orbitMap = new java.util.HashMap<>();
        for (String line : input) {
            if (line == null || line.trim().isEmpty()) continue;
            String[] parts = line.trim().split("\\)");
            if (parts.length != 2) continue;
            orbitMap.put(parts[1], parts[0]);
        }
        // Find path from YOU to COM
        java.util.List<String> youPath = new java.util.ArrayList<>();
        String current = orbitMap.get("YOU");
        while (current != null) {
            youPath.add(current);
            current = orbitMap.get(current);
        }
        // Find path from SAN to COM
        java.util.List<String> sanPath = new java.util.ArrayList<>();
        current = orbitMap.get("SAN");
        while (current != null) {
            sanPath.add(current);
            current = orbitMap.get(current);
        }
        // Find first common ancestor
        int i = youPath.size() - 1;
        int j = sanPath.size() - 1;
        while (i >= 0 && j >= 0 && youPath.get(i).equals(sanPath.get(j))) {
            i--;
            j--;
        }
        // Transfers = nodes left in each path
        int transfers = (i + 1) + (j + 1);
        return String.valueOf(transfers);
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = UniversalOrbitMapAOC2019Day6.class.getResourceAsStream(INPUT_FILE)) {
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
