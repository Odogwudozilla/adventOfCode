package odogwudozilla.year2015.day18;

/**
 * Like a GIF For Your Yard (Advent of Code 2015 Day 18)
 *
 * After the million lights incident, the fire code has gotten stricter: now, at most ten thousand lights are allowed. You arrange them in a 100x100 grid.
 *
 * Never one to let you down, Santa again mails you instructions on the ideal lighting configuration. With so few lights, he says, you'll have to resort to animation.
 *
 * Start by setting your lights to the included initial configuration (your puzzle input). A # means "on", and a . means "off".
 *
 * Then, animate your grid in steps, where each step decides the next configuration based on the current one. Each light's next state (either on or off) depends on its current state and the current states of the eight lights adjacent to it (including diagonals). Lights on the edge of the grid might have fewer than eight neighbors; the missing ones always count as "off".
 *
 * The state a light should have next is based on its current state (on or off) plus the number of neighbors that are on:
 * - A light which is on stays on when 2 or 3 neighbors are on, and turns off otherwise.
 * - A light which is off turns on if exactly 3 neighbors are on, and stays off otherwise.
 * All of the lights update simultaneously; they all consider the same current state before moving to the next.
 *
 * Official puzzle URL: https://adventofcode.com/2015/day/18
 */

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;

public class LikeAGIFForYourYardAOC2015Day18 {
    private static final int GRID_SIZE = 100;
    private static final int STEPS = 100;
    private static final String INPUT_PATH = "src/main/resources/2015/day18/day18_puzzle_data.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(INPUT_PATH));
            boolean[][] grid = parseGrid(lines);
            int partOneResult = solvePartOne(grid);
            System.out.println("Part 1: " + partOneResult);
            int partTwoResult = solvePartTwo(grid);
            System.out.println("Part 2: " + partTwoResult);
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }

    private static boolean[][] parseGrid(List<String> lines) {
        boolean[][] grid = new boolean[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            String line = lines.get(i);
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = line.charAt(j) == '#';
            }
        }
        return grid;
    }

    /**
     * Solves Part 1: Simulate 100 steps and count lights that are on.
     * @param initialGrid the initial grid state
     * @return number of lights on after 100 steps
     */
    public static int solvePartOne(boolean[][] initialGrid) {
        boolean[][] grid = copyGrid(initialGrid);
        for (int step = 0; step < STEPS; step++) {
            grid = nextStep(grid);
        }
        return countOn(grid);
    }

    /**
     * Solves Part 2: Simulate 100 steps with corners always on.
     * @param initialGrid the initial grid state
     * @return number of lights on after 100 steps (corners stuck on)
     */
    public static int solvePartTwo(boolean[][] initialGrid) {
        boolean[][] grid = copyGrid(initialGrid);
        forceCornersOn(grid);
        for (int step = 0; step < STEPS; step++) {
            grid = nextStepWithCornersOn(grid);
        }
        return countOn(grid);
    }

    private static void forceCornersOn(boolean[][] grid) {
        grid[0][0] = true;
        grid[0][GRID_SIZE - 1] = true;
        grid[GRID_SIZE - 1][0] = true;
        grid[GRID_SIZE - 1][GRID_SIZE - 1] = true;
    }

    private static boolean[][] nextStep(boolean[][] grid) {
        boolean[][] next = new boolean[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                int onNeighbours = countNeighbours(grid, i, j);
                if (grid[i][j]) {
                    next[i][j] = (onNeighbours == 2 || onNeighbours == 3);
                } else {
                    next[i][j] = (onNeighbours == 3);
                }
            }
        }
        return next;
    }

    private static boolean[][] nextStepWithCornersOn(boolean[][] grid) {
        boolean[][] next = nextStep(grid);
        forceCornersOn(next);
        return next;
    }

    private static int countNeighbours(boolean[][] grid, int x, int y) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = x + dx, ny = y + dy;
                if (nx >= 0 && nx < GRID_SIZE && ny >= 0 && ny < GRID_SIZE) {
                    if (grid[nx][ny]) count++;
                }
            }
        }
        return count;
    }

    private static int countOn(boolean[][] grid) {
        int count = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j]) count++;
            }
        }
        return count;
    }

    private static boolean[][] copyGrid(boolean[][] grid) {
        boolean[][] copy = new boolean[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, GRID_SIZE);
        }
        return copy;
    }
}
