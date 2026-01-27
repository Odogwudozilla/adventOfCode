package odogwudozilla.year2018.day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Advent of Code 2018 - Day 11: Chronal Charge
 * <p>
 * This puzzle involves finding the 3x3 square with the largest total power in a 300x300 grid of fuel cells.
 * Each fuel cell's power level is calculated based on its coordinates and a grid serial number.
 * <p>
 * The power level calculation follows these steps:
 * 1. Find the rack ID (X coordinate + 10)
 * 2. Start with power level = rack ID * Y coordinate
 * 3. Add the grid serial number
 * 4. Multiply by the rack ID
 * 5. Keep only the hundreds digit
 * 6. Subtract 5
 * <p>
 * Official puzzle: https://adventofcode.com/2018/day/11
 */
public class ChronalChargeAOC2018Day11 {

    private static final int GRID_SIZE = 300;
    private static final int SQUARE_SIZE = 3;

    public static void main(String[] args) {
        try {
            String input = Files.readString(Paths.get("src/main/resources/2018/day11/day11_puzzle_data.txt")).trim();
            int serialNumber = Integer.parseInt(input);

            // Solve Part 1
            String part1Result = solvePartOne(serialNumber);
            System.out.println("Part 1 - Top-left coordinate of 3x3 square with largest total power: " + part1Result);

            // Solve Part 2
            String part2Result = solvePartTwo(serialNumber);
            System.out.println("Part 2 - X,Y,size identifier of square with largest total power: " + part2Result);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Solve Part 1: Find the X,Y coordinate of the top-left fuel cell of the 3x3 square with the largest total power.
     * @param serialNumber the grid serial number from the puzzle input
     * @return the coordinate in format "X,Y"
     */
    static String solvePartOne(int serialNumber) {
        // Calculate power levels for all cells in the grid
        int[][] grid = new int[GRID_SIZE + 1][GRID_SIZE + 1]; // 1-indexed (1 to 300)

        for (int x = 1; x <= GRID_SIZE; x++) {
            for (int y = 1; y <= GRID_SIZE; y++) {
                grid[x][y] = calculatePowerLevel(x, y, serialNumber);
            }
        }

        // Find the 3x3 square with the largest total power
        int maxPower = Integer.MIN_VALUE;
        int maxX = 0;
        int maxY = 0;

        // Check all possible 3x3 squares (top-left can be from 1,1 to 298,298)
        for (int x = 1; x <= GRID_SIZE - SQUARE_SIZE + 1; x++) {
            for (int y = 1; y <= GRID_SIZE - SQUARE_SIZE + 1; y++) {
                int totalPower = calculate3x3Power(grid, x, y);

                if (totalPower > maxPower) {
                    maxPower = totalPower;
                    maxX = x;
                    maxY = y;
                }
            }
        }

        return maxX + "," + maxY;
    }

    /**
     * Solve Part 2: Find the X,Y,size identifier of the square with the largest total power.
     * Uses a Summed Area Table (SAT) for efficient computation of squares of any size.
     * @param serialNumber the grid serial number from the puzzle input
     * @return the identifier in format "X,Y,size"
     */
    static String solvePartTwo(int serialNumber) {
        // Calculate power levels for all cells in the grid
        int[][] grid = new int[GRID_SIZE + 1][GRID_SIZE + 1]; // 1-indexed (1 to 300)

        for (int x = 1; x <= GRID_SIZE; x++) {
            for (int y = 1; y <= GRID_SIZE; y++) {
                grid[x][y] = calculatePowerLevel(x, y, serialNumber);
            }
        }

        // Build Summed Area Table for O(1) range sum queries
        long[][] sat = buildSummedAreaTable(grid);

        int maxPower = Integer.MIN_VALUE;
        int maxX = 0;
        int maxY = 0;
        int maxSize = 0;

        // Try all possible square sizes and positions
        for (int size = 1; size <= GRID_SIZE; size++) {
            for (int x = 1; x <= GRID_SIZE - size + 1; x++) {
                for (int y = 1; y <= GRID_SIZE - size + 1; y++) {
                    int totalPower = getSquarePowerFromSAT(sat, x, y, size);

                    if (totalPower > maxPower) {
                        maxPower = totalPower;
                        maxX = x;
                        maxY = y;
                        maxSize = size;
                    }
                }
            }
        }

        return maxX + "," + maxY + "," + maxSize;
    }

    /**
     * Build a Summed Area Table for efficient range sum queries.
     * SAT[x][y] contains the sum of all cells from (1,1) to (x,y) inclusive.
     * @param grid the power level grid
     * @return the summed area table
     */
    private static long[][] buildSummedAreaTable(int[][] grid) {
        long[][] sat = new long[GRID_SIZE + 1][GRID_SIZE + 1];

        for (int x = 1; x <= GRID_SIZE; x++) {
            for (int y = 1; y <= GRID_SIZE; y++) {
                sat[x][y] = grid[x][y]
                          + sat[x - 1][y]
                          + sat[x][y - 1]
                          - sat[x - 1][y - 1];
            }
        }

        return sat;
    }

    /**
     * Get the sum of a square region using the Summed Area Table in O(1) time.
     * @param sat the summed area table
     * @param topLeftX the X coordinate of the top-left corner
     * @param topLeftY the Y coordinate of the top-left corner
     * @param size the size of the square
     * @return the total power of the square
     */
    private static int getSquarePowerFromSAT(long[][] sat, int topLeftX, int topLeftY, int size) {
        int x2 = topLeftX + size - 1;
        int y2 = topLeftY + size - 1;
        int x1 = topLeftX - 1;
        int y1 = topLeftY - 1;

        return (int) (sat[x2][y2] - sat[x1][y2] - sat[x2][y1] + sat[x1][y1]);
    }

    /**
     * Calculate the power level of a fuel cell at given coordinates.
     * @param x the X coordinate (1-300)
     * @param y the Y coordinate (1-300)
     * @param serialNumber the grid serial number
     * @return the power level
     */
    private static int calculatePowerLevel(int x, int y, int serialNumber) {
        // Find the fuel cell's rack ID (X coordinate + 10)
        int rackId = x + 10;

        // Begin with power level = rack ID * Y coordinate
        int powerLevel = rackId * y;

        // Increase the power level by the grid serial number
        powerLevel += serialNumber;

        // Set the power level to itself multiplied by the rack ID
        powerLevel *= rackId;

        // Keep only the hundreds digit (extract the hundreds digit from the number)
        int hundredsDigit = (powerLevel / 100) % 10;

        // Subtract 5 from the power level
        return hundredsDigit - 5;
    }

    /**
     * Calculate the total power of a 3x3 square starting at the given top-left coordinate.
     * @param grid the power level grid
     * @param topLeftX the X coordinate of the top-left corner
     * @param topLeftY the Y coordinate of the top-left corner
     * @return the total power of the 3x3 square
     */
    private static int calculate3x3Power(int[][] grid, int topLeftX, int topLeftY) {
        int totalPower = 0;

        for (int x = topLeftX; x < topLeftX + SQUARE_SIZE; x++) {
            for (int y = topLeftY; y < topLeftY + SQUARE_SIZE; y++) {
                totalPower += grid[x][y];
            }
        }

        return totalPower;
    }
}
