package odogwudozilla.year2023.day21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Advent of Code 2023 - Day 21: Step Counter
 *
 * Part 1: The Elf needs to determine which garden plots he can reach with exactly 64 steps.
 * Part 2: The map repeats infinitely in all directions, and we need to find reachable plots
 * after 26,501,365 steps. This uses quadratic extrapolation based on the repeating pattern.
 *
 * Puzzle: https://adventofcode.com/2023/day/21
 */
public class StepCounterAOC2023Day21 {

    private static final int TARGET_STEPS_PART1 = 64;
    private static final long TARGET_STEPS_PART2 = 26501365L;
    private static final char ROCK = '#';
    private static final char START = 'S';

    private static class Position {
        final int row;
        final int col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Position)) return false;
            Position position = (Position) o;
            return row == position.row && col == position.col;
        }

        @Override
        public int hashCode() {
            return 131 * row + col;
        }
    }

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(
                Paths.get("src/main/resources/2023/day21/day21_puzzle_data.txt")
            );

            char[][] grid = parseGrid(lines);
            Position start = findStartPosition(grid);

            int reachablePlotsPart1 = solvePartOne(grid, start);
            System.out.println("Part 1 - Garden plots reachable in exactly " + TARGET_STEPS_PART1 + " steps: " + reachablePlotsPart1);

            long reachablePlotsPart2 = solvePartTwo(grid, start);
            System.out.println("Part 2 - Garden plots reachable in exactly " + TARGET_STEPS_PART2 + " steps: " + reachablePlotsPart2);

        } catch (IOException e) {
            System.err.println("Error reading puzzle input: " + e.getMessage());
        }
    }

    /**
     * Parse the input lines into a 2D grid.
     * @param lines the input lines
     * @return 2D character array representing the grid
     */
    private static char[][] parseGrid(List<String> lines) {
        int rows = lines.size();
        int cols = lines.get(0).length();
        char[][] grid = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        return grid;
    }

    /**
     * Find the starting position marked with 'S' in the grid.
     * @param grid the garden grid
     * @return the starting position
     */
    private static Position findStartPosition(char[][] grid) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col] == START) {
                    return new Position(row, col);
                }
            }
        }
        throw new IllegalStateException("Start position not found in grid");
    }

    /**
     * Count the number of garden plots reachable in exactly the target number of steps
     * on an infinite repeating grid using quadratic extrapolation.
     * The key insight is that the growth follows a quadratic pattern based on the grid size.
     * @param grid the garden grid (repeats infinitely)
     * @param start the starting position
     * @param targetSteps the exact number of steps
     * @return count of reachable plots
     */
    private static long countReachablePlotsInfinite(char[][] grid, Position start, long targetSteps) {
        int gridSize = grid.length;

        // The target steps follows the pattern: remainder + gridSize * n
        // For 26501365 with gridSize=131: 26501365 = 65 + 131 * 202300
        long remainder = targetSteps % gridSize;
        long multiplier = targetSteps / gridSize;

        // Sample three points to fit a quadratic polynomial
        // We need values at: remainder, remainder + gridSize, remainder + 2*gridSize
        long[] sampleSteps = new long[3];
        long[] sampleValues = new long[3];

        for (int i = 0; i < 3; i++) {
            sampleSteps[i] = remainder + (long) i * gridSize;
            sampleValues[i] = countReachablePlotsInfiniteBFS(grid, start, (int) sampleSteps[i]);
        }

        // Use Lagrange interpolation to find the value at the target
        // f(x) = a*x^2 + b*x + c
        // We have three points, so we can solve for a, b, c
        long y0 = sampleValues[0];
        long y1 = sampleValues[1];
        long y2 = sampleValues[2];

        // Using finite differences for quadratic sequence
        long diff1_0 = y1 - y0;
        long diff1_1 = y2 - y1;
        long diff2 = diff1_1 - diff1_0;

        // Quadratic formula: f(n) = y0 + diff1_0 * n + diff2 * n * (n-1) / 2
        long n = multiplier;
        return y0 + diff1_0 * n + diff2 * n * (n - 1) / 2;
    }

    /**
     * Count reachable plots on an infinite grid using BFS.
     * The grid repeats infinitely, so we use modulo arithmetic for coordinates.
     * @param grid the base grid pattern
     * @param start the starting position
     * @param targetSteps the exact number of steps
     * @return count of reachable plots
     */
    private static long countReachablePlotsInfiniteBFS(char[][] grid, Position start, int targetSteps) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Directions: north, south, east, west
        int[] deltaRow = {-1, 1, 0, 0};
        int[] deltaCol = {0, 0, 1, -1};

        // Use a map to track positions (can be outside original grid bounds)
        Set<Position> currentPositions = new HashSet<>();
        currentPositions.add(start);

        for (int step = 0; step < targetSteps; step++) {
            Set<Position> nextPositions = new HashSet<>();

            for (Position current : currentPositions) {
                for (int dir = 0; dir < 4; dir++) {
                    int newRow = current.row + deltaRow[dir];
                    int newCol = current.col + deltaCol[dir];

                    // Use modulo to handle infinite grid (wrap around)
                    int gridRow = ((newRow % rows) + rows) % rows;
                    int gridCol = ((newCol % cols) + cols) % cols;

                    // Check if it's a valid garden plot in the base pattern
                    if (grid[gridRow][gridCol] == ROCK) {
                        continue;
                    }

                    Position nextPos = new Position(newRow, newCol);
                    nextPositions.add(nextPos);
                }
            }

            currentPositions = nextPositions;
        }

        return currentPositions.size();
    }

    /**
     * Count the number of garden plots reachable in exactly the target number of steps.
     * Uses BFS to explore positions. Since we need exactly targetSteps, we track positions
     * at even/odd steps separately. Positions reachable in targetSteps will be those at
     * the same parity (even/odd) as targetSteps.
     * @param grid the garden grid
     * @param start the starting position
     * @param targetSteps the exact number of steps
     * @return count of reachable plots
     */
    private static int countReachablePlots(char[][] grid, Position start, int targetSteps) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Directions: north, south, east, west
        int[] deltaRow = {-1, 1, 0, 0};
        int[] deltaCol = {0, 0, 1, -1};

        // Track visited positions at each step parity
        Set<Position> evenSteps = new HashSet<>();
        Set<Position> oddSteps = new HashSet<>();

        Queue<Position> queue = new LinkedList<>();
        queue.add(start);
        evenSteps.add(start);

        int currentStep = 0;

        while (!queue.isEmpty() && currentStep < targetSteps) {
            int levelSize = queue.size();
            currentStep++;

            Set<Position> currentStepSet = (currentStep % 2 == 0) ? evenSteps : oddSteps;
            Set<Position> nextLevelPositions = new HashSet<>();

            for (int i = 0; i < levelSize; i++) {
                Position current = queue.poll();

                // Explore all four directions
                for (int dir = 0; dir < 4; dir++) {
                    int newRow = current.row + deltaRow[dir];
                    int newCol = current.col + deltaCol[dir];

                    // Check bounds
                    if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) {
                        continue;
                    }

                    // Check if it's a valid garden plot
                    if (grid[newRow][newCol] == ROCK) {
                        continue;
                    }

                    Position nextPos = new Position(newRow, newCol);

                    // Check if we've already visited this position at this parity
                    if (currentStepSet.contains(nextPos)) {
                        continue;
                    }

                    currentStepSet.add(nextPos);
                    nextLevelPositions.add(nextPos);
                }
            }

            // Add all new positions for the next level
            queue.addAll(nextLevelPositions);
        }

        // Return positions at the target step parity
        return (targetSteps % 2 == 0) ? evenSteps.size() : oddSteps.size();
    }

    /**
     * Solve Part 1: Finite grid, 64 steps
     */
    private static int solvePartOne(char[][] grid, Position start) {
        return countReachablePlots(grid, start, TARGET_STEPS_PART1);
    }

    /**
     * Solve Part 2: Infinite repeating grid, 26501365 steps
     */
    private static long solvePartTwo(char[][] grid, Position start) {
        return countReachablePlotsInfinite(grid, start, TARGET_STEPS_PART2);
    }
}
