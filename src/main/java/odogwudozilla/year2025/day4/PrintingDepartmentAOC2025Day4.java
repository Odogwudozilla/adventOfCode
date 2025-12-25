package odogwudozilla.year2025.day4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Advent of Code 2025 - Day 4: Printing Department
 * <p>
 * The rolls of paper (@) are arranged on a large grid. Forklifts can only access
 * a roll of paper if there are fewer than four rolls of paper in the eight adjacent positions.
 * This solution calculates how many rolls of paper can be accessed by a forklift.
 * <p>
 * Puzzle URL: <a href="https://adventofcode.com/2025/day/4">https://adventofcode.com/2025/day/4</a>
 */
public class PrintingDepartmentAOC2025Day4 {

    private static final char PAPER_ROLL = '@';
    private static final int MAX_ADJACENT_ROLLS = 3;

    /**
     * Main method to solve the puzzle.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(
                Paths.get("src/main/resources/2025/day4/day4_puzzle_data.txt")
            );

            char[][] grid = parseGrid(lines);
            int accessibleRolls = solvePartOne(grid);
            System.out.println("PrintingDepartmentAOC2025Day4 - Part 1");
            System.out.println("Number of rolls accessible by forklift: " + accessibleRolls);
            System.out.println();

            // Part 2: Iteratively remove accessible rolls
            char[][] grid2 = parseGrid(lines);
            int totalRemovedRolls = solvePartTwo(grid2);
            System.out.println("PrintingDepartmentAOC2025Day4 - Part 2");
            System.out.println("Total rolls removed: " + totalRemovedRolls);

        } catch (IOException e) {
            System.err.println("PrintingDepartmentAOC2025Day4 - Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Parses the input lines into a 2D character grid.
     * @param lines the input lines from the puzzle data
     * @return a 2D character array representing the grid
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
     * Solve Part 1: Counts the number of rolls that can be accessed by a forklift.
     * @param grid the grid containing the paper rolls
     * @return the count of accessible rolls
     */
    private static int solvePartOne(char[][] grid) {
        return countAccessibleRolls(grid);
    }

    /**
     * Solve Part 2: Iteratively removes accessible rolls and returns the total removed.
     * @param grid the grid containing the paper rolls
     * @return the total number of rolls removed
     */
    private static int solvePartTwo(char[][] grid) {
        return removeAllAccessibleRolls(grid);
    }

    /**
     * Counts the number of rolls that can be accessed by a forklift.
     * A roll is accessible if it has fewer than 4 adjacent rolls.
     * @param grid the grid containing the paper rolls
     * @return the count of accessible rolls
     */
    private static int countAccessibleRolls(char[][] grid) {
        int accessibleCount = 0;
        int rows = grid.length;
        int cols = grid[0].length;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (PAPER_ROLL == grid[row][col]) {
                    int adjacentRolls = countAdjacentRolls(grid, row, col);
                    if (adjacentRolls <= MAX_ADJACENT_ROLLS) {
                        accessibleCount++;
                    }
                }
            }
        }

        return accessibleCount;
    }

    /**
     * Counts the number of paper rolls in the eight adjacent positions.
     * @param grid the grid containing the paper rolls
     * @param row the row index of the current position
     * @param col the column index of the current position
     * @return the count of adjacent paper rolls
     */
    private static int countAdjacentRolls(char[][] grid, int row, int col) {
        int count = 0;
        int rows = grid.length;
        int cols = grid[0].length;

        // Define the 8 adjacent directions: N, NE, E, SE, S, SW, W, NW
        int[] rowOffsets = {-1, -1, 0, 1, 1, 1, 0, -1};
        int[] colOffsets = {0, 1, 1, 1, 0, -1, -1, -1};

        for (int i = 0; i < 8; i++) {
            int newRow = row + rowOffsets[i];
            int newCol = col + colOffsets[i];

            if (isValidPosition(newRow, newCol, rows, cols) && PAPER_ROLL == grid[newRow][newCol]) {
                count++;
            }
        }

        return count;
    }

    /**
     * Checks if a position is within the grid boundaries.
     * @param row the row index
     * @param col the column index
     * @param rows the total number of rows
     * @param cols the total number of columns
     * @return true if the position is valid, false otherwise
     */
    private static boolean isValidPosition(int row, int col, int rows, int cols) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    /**
     * Iteratively removes all accessible rolls until no more can be removed.
     * A roll is accessible if it has fewer than 4 adjacent rolls.
     * @param grid the grid containing the paper rolls (will be modified)
     * @return the total number of rolls removed
     */
    private static int removeAllAccessibleRolls(char[][] grid) {
        int totalRemoved = 0;
        boolean rollsRemoved;

        do {
            rollsRemoved = false;
            int rows = grid.length;
            int cols = grid[0].length;

            // Find all accessible rolls in this iteration
            boolean[][] toRemove = new boolean[rows][cols];
            int removedThisRound = 0;

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    if (PAPER_ROLL == grid[row][col]) {
                        int adjacentRolls = countAdjacentRolls(grid, row, col);
                        if (adjacentRolls <= MAX_ADJACENT_ROLLS) {
                            toRemove[row][col] = true;
                            removedThisRound++;
                        }
                    }
                }
            }

            // Remove all accessible rolls found in this iteration
            if (removedThisRound > 0) {
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        if (toRemove[row][col]) {
                            grid[row][col] = '.';
                        }
                    }
                }
                totalRemoved += removedThisRound;
                rollsRemoved = true;
            }

        } while (rollsRemoved);

        return totalRemoved;
    }
}

