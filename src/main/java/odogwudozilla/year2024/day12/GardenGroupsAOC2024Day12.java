package odogwudozilla.year2024.day12;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Advent of Code 2024 - Day 12: Garden Groups
 * <p>
 * Puzzle URL: <a href="https://adventofcode.com/2024/day/12">Advent of Code 2024 Day 12</a>
 */
public final class GardenGroupsAOC2024Day12 {

    private static final String INPUT_FILE = "/2024/day12/day12_puzzle_data.txt";
    private static final int[] ROW_OFFSETS = {-1, 1, 0, 0};
    private static final int[] COLUMN_OFFSETS = {0, 0, -1, 1};

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
    static String solvePartOne(List<String> input) {
        return Long.toString(calculatePrices(input).partOnePrice());
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    static String solvePartTwo(List<String> input) {
        return Long.toString(calculatePrices(input).partTwoPrice());
    }

    private static PriceTotals calculatePrices(List<String> input) {
        if (input.isEmpty()) {
            return new PriceTotals(0L, 0L);
        }

        char[][] garden = parseGarden(input);
        boolean[][] visited = new boolean[garden.length][garden[0].length];
        long partOnePrice = 0L;
        long partTwoPrice = 0L;

        for (int row = 0; row < garden.length; row++) {
            for (int column = 0; column < garden[row].length; column++) {
                if (!visited[row][column]) {
                    RegionMetrics metrics = measureRegion(garden, visited, row, column);
                    partOnePrice += (long) metrics.area() * metrics.perimeter();
                    partTwoPrice += (long) metrics.area() * metrics.sides();
                }
            }
        }

        return new PriceTotals(partOnePrice, partTwoPrice);
    }

    private static char[][] parseGarden(List<String> input) {
        char[][] garden = new char[input.size()][];
        for (int row = 0; row < input.size(); row++) {
            garden[row] = input.get(row).toCharArray();
        }
        return garden;
    }

    private static RegionMetrics measureRegion(char[][] garden, boolean[][] visited, int startRow, int startColumn) {
        char plantType = garden[startRow][startColumn];
        Deque<Point> queue = new ArrayDeque<>();
        Map<Integer, List<Integer>> topEdges = new HashMap<>();
        Map<Integer, List<Integer>> bottomEdges = new HashMap<>();
        Map<Integer, List<Integer>> leftEdges = new HashMap<>();
        Map<Integer, List<Integer>> rightEdges = new HashMap<>();
        int area = 0;
        int perimeter = 0;

        visited[startRow][startColumn] = true;
        queue.addLast(new Point(startRow, startColumn));

        while (!queue.isEmpty()) {
            Point point = queue.removeFirst();
            area++;

            for (int directionIndex = 0; directionIndex < ROW_OFFSETS.length; directionIndex++) {
                int nextRow = point.row() + ROW_OFFSETS[directionIndex];
                int nextColumn = point.column() + COLUMN_OFFSETS[directionIndex];

                if (isSameRegionCell(garden, nextRow, nextColumn, plantType)) {
                    if (!visited[nextRow][nextColumn]) {
                        visited[nextRow][nextColumn] = true;
                        queue.addLast(new Point(nextRow, nextColumn));
                    }
                    continue;
                }

                perimeter++;
                addBoundaryEdge(topEdges, bottomEdges, leftEdges, rightEdges, point.row(), point.column(), directionIndex);
            }
        }

        int sides = countStraightSides(topEdges)
                + countStraightSides(bottomEdges)
                + countStraightSides(leftEdges)
                + countStraightSides(rightEdges);
        return new RegionMetrics(area, perimeter, sides);
    }

    private static boolean isSameRegionCell(char[][] garden, int row, int column, char plantType) {
        return row >= 0
                && row < garden.length
                && column >= 0
                && column < garden[row].length
                && garden[row][column] == plantType;
    }

    private static void addBoundaryEdge(
            Map<Integer, List<Integer>> topEdges,
            Map<Integer, List<Integer>> bottomEdges,
            Map<Integer, List<Integer>> leftEdges,
            Map<Integer, List<Integer>> rightEdges,
            int row,
            int column,
            int directionIndex
    ) {
        switch (directionIndex) {
            case 0 -> addEdge(topEdges, row, column);
            case 1 -> addEdge(bottomEdges, row + 1, column);
            case 2 -> addEdge(leftEdges, column, row);
            case 3 -> addEdge(rightEdges, column + 1, row);
            default -> throw new IllegalArgumentException("Unknown direction index: " + directionIndex);
        }
    }

    private static void addEdge(Map<Integer, List<Integer>> edgesByLine, int line, int position) {
        edgesByLine.computeIfAbsent(line, unused -> new ArrayList<>()).add(position);
    }

    private static int countStraightSides(Map<Integer, List<Integer>> edgesByLine) {
        int sides = 0;
        for (List<Integer> edgePositions : edgesByLine.values()) {
            Collections.sort(edgePositions);
            int previousPosition = Integer.MIN_VALUE;
            for (int position : edgePositions) {
                if (position != previousPosition + 1) {
                    sides++;
                }
                previousPosition = position;
            }
        }
        return sides;
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = GardenGroupsAOC2024Day12.class.getResourceAsStream(INPUT_FILE)) {
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

    private record Point(int row, int column) {
    }

    private record RegionMetrics(int area, int perimeter, int sides) {
    }

    private record PriceTotals(long partOnePrice, long partTwoPrice) {
    }
}
