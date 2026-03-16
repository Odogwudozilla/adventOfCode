package odogwudozilla.year2024.day8;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

/**
 * Resonant Collinearity (Advent of Code 2024 Day 8)
 *
 * For each frequency, find all pairs of antennas with the same frequency. For each pair, mark antinodes at points collinear with the antennas, where one antenna is twice as far from the antinode as the other. Count all unique antinode locations within the map.
 *
 * Official puzzle URL: https://adventofcode.com/2024/day/8
 *
 * @param args Command line arguments
 * @return void
 */
public class ResonantCollinearityAOC2024Day8 {
    private static final String INPUT_PATH = "src/main/resources/2024/day8/day8_puzzle_data.txt";

    /**
     * Main entry point for the solution.
     * Reads the input file and prints the result for Part 1.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(INPUT_PATH));
            System.out.println("Part 1: " + solvePartOne(lines));
            System.out.println("Part 2: " + solvePartTwo(lines));
        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1 by finding all unique antinode locations within the map.
     * @param lines The map as a list of strings
     * @return The number of unique antinode locations
     */
    public static int solvePartOne(@org.jetbrains.annotations.NotNull List<String> lines) {
        int rows = lines.size();
        int cols = lines.get(0).length();
        Map<Character, List<int[]>> freqToAntennas = new HashMap<>();
        // Collect all antenna positions by frequency
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char ch = lines.get(r).charAt(c);
                if (Character.isLetterOrDigit(ch)) {
                    freqToAntennas.computeIfAbsent(ch, k -> new ArrayList<>()).add(new int[]{r, c});
                }
            }
        }
        Set<String> antinodes = new HashSet<>();
        // For each frequency, process all pairs
        for (Map.Entry<Character, List<int[]>> entry : freqToAntennas.entrySet()) {
            List<int[]> antennas = entry.getValue();
            int n = antennas.size();
            for (int i = 0; i < n; i++) {
                int[] a = antennas.get(i);
                for (int j = i + 1; j < n; j++) {
                    int[] b = antennas.get(j);
                    // Antinode on one side
                    int dr = b[0] - a[0];
                    int dc = b[1] - a[1];
                    int r1 = a[0] - dr;
                    int c1 = a[1] - dc;
                    int r2 = b[0] + dr;
                    int c2 = b[1] + dc;
                    if (r1 >= 0 && r1 < rows && c1 >= 0 && c1 < cols) {
                        antinodes.add(r1 + "," + c1);
                    }
                    if (r2 >= 0 && r2 < rows && c2 >= 0 && c2 < cols) {
                        antinodes.add(r2 + "," + c2);
                    }
                }
            }
        }
        return antinodes.size();
    }

    /**
     * Solves Part 2 by finding all unique antinode locations within the map, where an antinode occurs at any grid position exactly in line with at least two antennas of the same frequency, regardless of distance.
     * @param lines The map as a list of strings
     * @return The number of unique antinode locations
     */
    public static int solvePartTwo(@org.jetbrains.annotations.NotNull List<String> lines) {
        int rows = lines.size();
        int cols = lines.get(0).length();
        Map<Character, List<int[]>> freqToAntennas = new HashMap<>();
        // Collect all antenna positions by frequency
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char ch = lines.get(r).charAt(c);
                if (Character.isLetterOrDigit(ch)) {
                    freqToAntennas.computeIfAbsent(ch, k -> new ArrayList<>()).add(new int[]{r, c});
                }
            }
        }
        Set<String> antinodes = new HashSet<>();
        // For each frequency, process all pairs (i < j)
        for (Map.Entry<Character, List<int[]>> entry : freqToAntennas.entrySet()) {
            List<int[]> antennas = entry.getValue();
            int n = antennas.size();
            if (n < 2) continue;
            for (int i = 0; i < n; i++) {
                int[] a = antennas.get(i);
                for (int j = i + 1; j < n; j++) {
                    int[] b = antennas.get(j);
                    int dr = b[0] - a[0];
                    int dc = b[1] - a[1];
                    // Step along the line in both directions
                    int r = a[0], c = a[1];
                    while (true) {
                        r -= dr;
                        c -= dc;
                        if (r < 0 || r >= rows || c < 0 || c >= cols) break;
                        antinodes.add(r + "," + c);
                    }
                    r = a[0]; c = a[1];
                    while (true) {
                        r += dr;
                        c += dc;
                        if (r < 0 || r >= rows || c < 0 || c >= cols) break;
                        antinodes.add(r + "," + c);
                    }
                    // All antennas on the line are antinodes
                    for (int k = 0; k < n; k++) {
                        int[] p = antennas.get(k);
                        // Check if p is on the line through a and b
                        int dx1 = p[0] - a[0];
                        int dy1 = p[1] - a[1];
                        int dx2 = b[0] - a[0];
                        int dy2 = b[1] - a[1];
                        if (dx1 * dy2 == dy1 * dx2) {
                            antinodes.add(p[0] + "," + p[1]);
                        }
                    }
                }
            }
        }
        return antinodes.size();
    }

    // Part 2 logic will be added here if needed
}
