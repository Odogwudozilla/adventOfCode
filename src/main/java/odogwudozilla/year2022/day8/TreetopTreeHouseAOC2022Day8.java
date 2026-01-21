package odogwudozilla.year2022.day8;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Advent of Code 2022 Day 8: Treetop Tree House
 *
 * The expedition comes across a peculiar patch of tall trees planted in a grid. We need to determine
 * how many trees are visible from outside the grid when looking directly along a row or column.
 * A tree is visible if all of the other trees between it and an edge of the grid are shorter than it.
 *
 * https://adventofcode.com/2022/day/8
 */
public class TreetopTreeHouseAOC2022Day8 {

    private static final String PUZZLE_INPUT_PATH = "src/main/resources/2022/day8/day8_puzzle_data.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(PUZZLE_INPUT_PATH));
            int[][] grid = parseGrid(lines);

            int partOne = solvePartOne(grid);
            System.out.println("Part One: " + partOne);

            int partTwo = solvePartTwo(grid);
            System.out.println("Part Two: " + partTwo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse the input into a 2D grid of tree heights.
     */
    private static int[][] parseGrid(List<String> lines) {
        int rows = lines.size();
        int cols = lines.get(0).length();
        int[][] grid = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            String line = lines.get(i);
            for (int j = 0; j < cols; j++) {
                grid[i][j] = Character.getNumericValue(line.charAt(j));
            }
        }

        return grid;
    }

    /**
     * Count the number of trees visible from outside the grid.
     * A tree is visible if all trees between it and an edge are shorter than it.
     */
    private static int solvePartOne(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int visibleCount = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (isVisible(grid, i, j)) {
                    visibleCount++;
                }
            }
        }

        return visibleCount;
    }

    /**
     * Check if a tree at position (row, col) is visible from any edge.
     */
    private static boolean isVisible(int[][] grid, int row, int col) {
        int rows = grid.length;
        int cols = grid[0].length;
        int treeHeight = grid[row][col];

        // Trees on the edge are always visible
        if (row == 0 || row == rows - 1 || col == 0 || col == cols - 1) {
            return true;
        }

        // Check if visible from the left
        boolean visibleFromLeft = true;
        for (int j = 0; j < col; j++) {
            if (grid[row][j] >= treeHeight) {
                visibleFromLeft = false;
                break;
            }
        }
        if (visibleFromLeft) {
            return true;
        }

        // Check if visible from the right
        boolean visibleFromRight = true;
        for (int j = col + 1; j < cols; j++) {
            if (grid[row][j] >= treeHeight) {
                visibleFromRight = false;
                break;
            }
        }
        if (visibleFromRight) {
            return true;
        }

        // Check if visible from the top
        boolean visibleFromTop = true;
        for (int i = 0; i < row; i++) {
            if (grid[i][col] >= treeHeight) {
                visibleFromTop = false;
                break;
            }
        }
        if (visibleFromTop) {
            return true;
        }

        // Check if visible from the bottom
        boolean visibleFromBottom = true;
        for (int i = row + 1; i < rows; i++) {
            if (grid[i][col] >= treeHeight) {
                visibleFromBottom = false;
                break;
            }
        }

        return visibleFromBottom;
    }

    /**
     * Calculate the scenic score for each tree and return the maximum.
     * Scenic score is the product of viewing distances in all four directions.
     */
    private static int solvePartTwo(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int maxScenicScore = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int scenicScore = calculateScenicScore(grid, i, j);
                maxScenicScore = Math.max(maxScenicScore, scenicScore);
            }
        }

        return maxScenicScore;
    }

    /**
     * Calculate the scenic score for a tree at position (row, col).
     * Scenic score is the product of the viewing distances in all four directions.
     */
    private static int calculateScenicScore(int[][] grid, int row, int col) {
        int rows = grid.length;
        int cols = grid[0].length;
        int treeHeight = grid[row][col];

        // Count trees visible to the left
        int leftDistance = 0;
        for (int j = col - 1; j >= 0; j--) {
            leftDistance++;
            if (grid[row][j] >= treeHeight) {
                break;
            }
        }

        // Count trees visible to the right
        int rightDistance = 0;
        for (int j = col + 1; j < cols; j++) {
            rightDistance++;
            if (grid[row][j] >= treeHeight) {
                break;
            }
        }

        // Count trees visible upward
        int upDistance = 0;
        for (int i = row - 1; i >= 0; i--) {
            upDistance++;
            if (grid[i][col] >= treeHeight) {
                break;
            }
        }

        // Count trees visible downward
        int downDistance = 0;
        for (int i = row + 1; i < rows; i++) {
            downDistance++;
            if (grid[i][col] >= treeHeight) {
                break;
            }
        }

        return leftDistance * rightDistance * upDistance * downDistance;
    }
}

