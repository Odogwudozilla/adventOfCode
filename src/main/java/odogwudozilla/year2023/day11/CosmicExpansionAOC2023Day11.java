package odogwudozilla.year2023.day11;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Advent of Code 2023 - Day 11: Cosmic Expansion
 * <p>
 * Puzzle URL: https://adventofcode.com/2023/day/11
 */
public final class CosmicExpansionAOC2023Day11 {

    private static final String INPUT_FILE = "/2023/day11/day11_puzzle_data.txt";
    private static final int EXPANSION_FACTOR_PART1 = 2;
    private static final int EXPANSION_FACTOR_PART2 = 1_000_000;

    /**
     * Entry point. Reads puzzle input and prints solutions for Part 1 and Part 2.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        List<String> input = readInput();
        CosmicExpansionAOC2023Day11 solver = new CosmicExpansionAOC2023Day11();
        System.out.println("Part 1: " + solver.solvePartOne(input));
        System.out.println("Part 2: " + solver.solvePartTwo(input));
    }

    /**
     * Solves Part 1 of the puzzle with expansion factor of 2.
     * @param input list of input lines
     * @return the Part 1 answer
     */
    public String solvePartOne(List<String> input) {
        long result = solveWithExpansionFactor(input, EXPANSION_FACTOR_PART1);
        return String.valueOf(result);
    }

    /**
     * Solves Part 2 of the puzzle with expansion factor of 1,000,000.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    public String solvePartTwo(List<String> input) {
        long result = solveWithExpansionFactor(input, EXPANSION_FACTOR_PART2);
        return String.valueOf(result);
    }

    /**
     * Calculates the sum of Manhattan distances between all galaxy pairs with a given expansion factor.
     * @param input the grid as a list of strings
     * @param expansionFactor the factor by which empty rows/columns are expanded
     * @return the sum of all pairwise distances
     */
    private long solveWithExpansionFactor(List<String> input, int expansionFactor) {
        List<Galaxy> galaxies = extractGalaxies(input);
        Set<Integer> emptyRows = findEmptyRows(input);
        Set<Integer> emptyColumns = findEmptyColumns(input);

        long totalDistance = 0;
        for (int i = 0; i < galaxies.size(); i++) {
            for (int j = i + 1; j < galaxies.size(); j++) {
                Galaxy g1 = galaxies.get(i);
                Galaxy g2 = galaxies.get(j);
                long distance = calculateDistance(g1, g2, emptyRows, emptyColumns, expansionFactor);
                totalDistance += distance;
            }
        }

        return totalDistance;
    }

    /**
     * Extracts the coordinates of all galaxies from the input grid.
     * @param input the grid as a list of strings
     * @return a list of galaxy coordinates (row, col)
     */
    private List<Galaxy> extractGalaxies(List<String> input) {
        List<Galaxy> galaxies = new ArrayList<>();
        for (int row = 0; row < input.size(); row++) {
            String line = input.get(row);
            for (int col = 0; col < line.length(); col++) {
                if (line.charAt(col) == '#') {
                    galaxies.add(new Galaxy(row, col));
                }
            }
        }
        return galaxies;
    }

    /**
     * Identifies all rows that contain no galaxies (all dots).
     * @param input the grid as a list of strings
     * @return a set of empty row indices
     */
    private Set<Integer> findEmptyRows(List<String> input) {
        Set<Integer> emptyRows = new HashSet<>();
        for (int row = 0; row < input.size(); row++) {
            String line = input.get(row);
            if (line.chars().allMatch(c -> c == '.')) {
                emptyRows.add(row);
            }
        }
        return emptyRows;
    }

    /**
     * Identifies all columns that contain no galaxies (all dots).
     * @param input the grid as a list of strings
     * @return a set of empty column indices
     */
    private Set<Integer> findEmptyColumns(List<String> input) {
        Set<Integer> emptyColumns = new HashSet<>();
        if (input.isEmpty()) {
            return emptyColumns;
        }

        int numCols = input.get(0).length();
        for (int col = 0; col < numCols; col++) {
            boolean isEmpty = true;
            for (String line : input) {
                if (line.charAt(col) != '.') {
                    isEmpty = false;
                    break;
                }
            }
            if (isEmpty) {
                emptyColumns.add(col);
            }
        }
        return emptyColumns;
    }

    /**
     * Calculates the Manhattan distance between two galaxies, accounting for expansion.
     * When an empty row or column is encountered, it contributes an extra distance.
     * @param g1 first galaxy
     * @param g2 second galaxy
     * @param emptyRows set of empty row indices
     * @param emptyColumns set of empty column indices
     * @param expansionFactor the expansion multiplier
     * @return the Manhattan distance with expansion adjustments
     */
    private long calculateDistance(Galaxy g1, Galaxy g2, Set<Integer> emptyRows, Set<Integer> emptyColumns, int expansionFactor) {
        int minRow = Math.min(g1.row, g2.row);
        int maxRow = Math.max(g1.row, g2.row);
        int minCol = Math.min(g1.col, g2.col);
        int maxCol = Math.max(g1.col, g2.col);

        // Count empty rows between the two galaxies
        long emptyRowCount = 0;
        for (int r = minRow + 1; r < maxRow; r++) {
            if (emptyRows.contains(r)) {
                emptyRowCount++;
            }
        }

        // Count empty columns between the two galaxies
        long emptyColumnCount = 0;
        for (int c = minCol + 1; c < maxCol; c++) {
            if (emptyColumns.contains(c)) {
                emptyColumnCount++;
            }
        }

        // Calculate base distance
        long rowDistance = maxRow - minRow;
        long colDistance = maxCol - minCol;

        // Add expansion factor contribution (each empty row/column adds expansionFactor - 1 extra distance)
        rowDistance += emptyRowCount * (expansionFactor - 1);
        colDistance += emptyColumnCount * (expansionFactor - 1);

        return rowDistance + colDistance;
    }

    /**
     * Represents a galaxy at a specific position.
     */
    private static class Galaxy {
        int row;
        int col;

        Galaxy(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = CosmicExpansionAOC2023Day11.class.getResourceAsStream(INPUT_FILE)) {
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


