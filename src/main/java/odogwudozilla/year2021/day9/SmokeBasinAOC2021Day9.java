
package odogwudozilla.year2021.day9;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Advent of Code 2021 - Day 9: Smoke Basin
 * <p>
 * Puzzle URL: https://adventofcode.com/2021/day/9
 */
public final class SmokeBasinAOC2021Day9 {

    private static final String INPUT_FILE = "/2021/day9/day9_puzzle_data.txt";
    private static final int BASIN_BOUNDARY = 9;
    private static final int[] DIRECTIONS_ROW = {-1, 1, 0, 0};
    private static final int[] DIRECTIONS_COL = {0, 0, -1, 1};

    /**
     * Entry point. Reads puzzle input and prints solutions for Part 1 and Part 2.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        List<String> input = readInput();
        String part1 = solvePartOne(input);
        String part2 = solvePartTwo(input);
        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
    }

    /**
     * Solves Part 1: Sums the risk levels of all low points in the heightmap.
     * A low point is lower than any of its orthogonal neighbours.
     * Risk level is 1 + value at the low point.
     *
     * @param lines List of input lines representing the heightmap
     * @return String representation of the sum of risk levels
     */
    public static String solvePartOne(List<String> lines) {
        int rows = lines.size();
        int cols = lines.get(0).length();
        int[][] grid = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            String line = lines.get(i);
            for (int j = 0; j < cols; j++) {
                grid[i][j] = line.charAt(j) - '0';
            }
        }
        int sum = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int val = grid[row][col];
                boolean isLow = true;
                for (int d = 0; d < 4; d++) {
                    int nRow = row + DIRECTIONS_ROW[d];
                    int nCol = col + DIRECTIONS_COL[d];
                    if (nRow >= 0 && nRow < rows && nCol >= 0 && nCol < cols) {
                        if (grid[nRow][nCol] <= val) {
                            isLow = false;
                            break;
                        }
                    }
                }
                if (isLow) {
                    sum += 1 + val;
                }
            }
        }
        return String.valueOf(sum);
    }

    /**
     * Solves Part 2 of the puzzle: Finds all basins, computes their sizes, and returns the product of the three largest basin sizes.
     * @param lines list of input lines
     * @return the Part 2 answer as a String
     */
    private static String solvePartTwo(List<String> lines) {
        int rows = lines.size();
        int cols = lines.get(0).length();
        int[][] grid = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            String line = lines.get(i);
            for (int j = 0; j < cols; j++) {
                grid[i][j] = line.charAt(j) - '0';
            }
        }
        boolean[][] visited = new boolean[rows][cols];
        List<Integer> basinSizeList = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int val = grid[row][col];
                boolean isLow = true;
                for (int d = 0; d < 4; d++) {
                    int nRow = row + DIRECTIONS_ROW[d];
                    int nCol = col + DIRECTIONS_COL[d];
                    if (nRow >= 0 && nRow < rows && nCol >= 0 && nCol < cols) {
                        if (grid[nRow][nCol] <= val) {
                            isLow = false;
                            break;
                        }
                    }
                }
                if (isLow && !visited[row][col]) {
                    int basinSize = floodFill(grid, visited, row, col);
                    basinSizeList.add(basinSize);
                }
            }
        }
        basinSizeList.sort(Collections.reverseOrder());
        int product = 1;
        for (int k = 0; k < 3 && k < basinSizeList.size(); k++) {
            product *= basinSizeList.get(k);
        }
        return String.valueOf(product);
    }

    /**
     * Flood fill from a low point to find the size of its basin.
     * @param grid the heightmap
     * @param visited visited cells
     * @param i row
     * @param j col
     * @return size of the basin
     */
    private static int floodFill(int[][] grid, boolean[][] visited, int i, int j) {
        int rows = grid.length;
        int cols = grid[0].length;
        if (i < 0 || i >= rows || j < 0 || j >= cols) return 0;
        if (visited[i][j]) return 0;
        if (grid[i][j] == BASIN_BOUNDARY) return 0;
        visited[i][j] = true;
        int size = 1;
        for (int d = 0; d < 4; d++) {
            int nRow = i + DIRECTIONS_ROW[d];
            int nCol = j + DIRECTIONS_COL[d];
            size += floodFill(grid, visited, nRow, nCol);
        }
        return size;
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = SmokeBasinAOC2021Day9.class.getResourceAsStream(INPUT_FILE)) {
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
