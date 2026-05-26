package odogwudozilla.year2023.day14;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2023 - Day 14: Parabolic Reflector Dish
 * <p>
 * Puzzle URL: https://adventofcode.com/2023/day/14
 */
public final class ParabolicReflectorDishAOC2023Day14 {

    private static final String INPUT_FILE = "/2023/day14/day14_puzzle_data.txt";
    private static final int TOTAL_CYCLES = 1_000_000_000;
    private static final Direction[] TILT_CYCLE = {Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.EAST};

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
        int rows = input.size();
        int cols = input.get(0).length();
        char[][] grid = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            grid[r] = input.get(r).toCharArray();
        }
        tiltNorth(grid);
        int load = calculateLoad(grid);
        return String.valueOf(load);
    }

    /**
     * Simulates tilting the grid north so all 'O' roll up as far as possible.
     */
    private static void tiltNorth(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        for (int col = 0; col < cols; col++) {
            int dest = 0; // next available row to move 'O' to
            for (int row = 0; row < rows; row++) {
                if (grid[row][col] == '#') {
                    dest = row + 1;
                } else if (grid[row][col] == 'O') {
                    if (row != dest) {
                        grid[dest][col] = 'O';
                        grid[row][col] = '.';
                    }
                    dest++;
                }
            }
        }
    }

    /**
     * Calculates the total load on the north support beams.
     */
    private static int calculateLoad(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int total = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 'O') {
                    total += (rows - r);
                }
            }
        }
        return total;
    }

    private enum Direction { NORTH, WEST, SOUTH, EAST }

    private static void tilt(char[][] grid, Direction dir) {
        int rows = grid.length;
        int cols = grid[0].length;
        switch (dir) {
            case NORTH:
                for (int col = 0; col < cols; col++) {
                    int dest = 0;
                    for (int row = 0; row < rows; row++) {
                        if (grid[row][col] == '#') {
                            dest = row + 1;
                        } else if (grid[row][col] == 'O') {
                            if (row != dest) {
                                grid[dest][col] = 'O';
                                grid[row][col] = '.';
                            }
                            dest++;
                        }
                    }
                }
                break;
            case SOUTH:
                for (int col = 0; col < cols; col++) {
                    int dest = rows - 1;
                    for (int row = rows - 1; row >= 0; row--) {
                        if (grid[row][col] == '#') {
                            dest = row - 1;
                        } else if (grid[row][col] == 'O') {
                            if (row != dest) {
                                grid[dest][col] = 'O';
                                grid[row][col] = '.';
                            }
                            dest--;
                        }
                    }
                }
                break;
            case WEST:
                for (int row = 0; row < rows; row++) {
                    int dest = 0;
                    for (int col = 0; col < cols; col++) {
                        if (grid[row][col] == '#') {
                            dest = col + 1;
                        } else if (grid[row][col] == 'O') {
                            if (col != dest) {
                                grid[row][dest] = 'O';
                                grid[row][col] = '.';
                            }
                            dest++;
                        }
                    }
                }
                break;
            case EAST:
                for (int row = 0; row < rows; row++) {
                    int dest = cols - 1;
                    for (int col = cols - 1; col >= 0; col--) {
                        if (grid[row][col] == '#') {
                            dest = col - 1;
                        } else if (grid[row][col] == 'O') {
                            if (col != dest) {
                                grid[row][dest] = 'O';
                                grid[row][col] = '.';
                            }
                            dest--;
                        }
                    }
                }
                break;
        }
    }

    private static String hashGrid(char[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (char[] row : grid) {
            sb.append(row);
            sb.append('\n');
        }
        return sb.toString();
    }

    private static char[][] copyGrid(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        char[][] copy = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            System.arraycopy(grid[r], 0, copy[r], 0, cols);
        }
        return copy;
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        int rows = input.size();
        int cols = input.get(0).length();
        char[][] grid = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            grid[r] = input.get(r).toCharArray();
        }
        java.util.Map<String, Integer> seen = new java.util.HashMap<>();
        java.util.List<String> states = new java.util.ArrayList<>();
        int cycle = 0;
        int foundCycleStart = -1, foundCycleLen = -1;
        while (cycle < TOTAL_CYCLES) {
            for (Direction dir : TILT_CYCLE) {
                tilt(grid, dir);
            }
            String hash = hashGrid(grid);
            if (seen.containsKey(hash)) {
                foundCycleStart = seen.get(hash);
                foundCycleLen = cycle - foundCycleStart;
                break;
            }
            seen.put(hash, cycle);
            states.add(hash);
            cycle++;
        }
        if (foundCycleLen != -1) {
            int remaining = (TOTAL_CYCLES - foundCycleStart - 1) % foundCycleLen;
            String finalState = states.get(foundCycleStart + remaining);
            String[] lines = finalState.split("\n");
            char[][] finalGrid = new char[rows][cols];
            for (int r = 0; r < rows; r++) {
                finalGrid[r] = lines[r].toCharArray();
            }
            return String.valueOf(calculateLoad(finalGrid));
        } else {
            return String.valueOf(calculateLoad(grid));
        }
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = ParabolicReflectorDishAOC2023Day14.class.getResourceAsStream(INPUT_FILE)) {
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
