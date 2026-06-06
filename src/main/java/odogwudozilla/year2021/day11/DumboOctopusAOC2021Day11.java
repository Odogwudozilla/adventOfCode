package odogwudozilla.year2021.day11;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2021 - Day 11: Dumbo Octopus
 * <p>
 * Simulates a 10×10 grid of bioluminescent dumbo octopuses. During each step,
 * every octopus's energy level rises by 1; any octopus whose energy exceeds 9
 * flashes and increments all eight neighbours (chain reaction). Each octopus
 * flashes at most once per step and resets to 0 afterwards.
 * <ul>
 *   <li>Part 1: total number of flashes after 100 steps.</li>
 *   <li>Part 2: first step on which all 100 octopuses flash simultaneously.</li>
 * </ul>
 * <p>
 * Puzzle URL: https://adventofcode.com/2021/day/11
 */
public final class DumboOctopusAOC2021Day11 {

    private static final String INPUT_FILE = "/2021/day11/day11_puzzle_data.txt";
    private static final int GRID_SIZE = 10;
    private static final int FLASH_THRESHOLD = 9;
    private static final int STEPS_PART_ONE = 100;
    private static final int TOTAL_OCTOPUSES = GRID_SIZE * GRID_SIZE;

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
     * Solves Part 1: returns the total number of flashes after 100 steps.
     * @param input list of input lines
     * @return total flashes after 100 steps as a string
     */
    private static String solvePartOne(List<String> input) {
        int[][] grid = parseGrid(input);
        int totalFlashes = 0;
        for (int step = 0; step < STEPS_PART_ONE; step++) {
            totalFlashes += simulateStep(grid);
        }
        return String.valueOf(totalFlashes);
    }

    /**
     * Solves Part 2: returns the first step number on which all 100 octopuses
     * flash simultaneously (a synchronised flash).
     * @param input list of input lines
     * @return first synchronised-flash step number as a string
     */
    private static String solvePartTwo(List<String> input) {
        int[][] grid = parseGrid(input);
        int step = 0;
        while (true) {
            step++;
            if (simulateStep(grid) == TOTAL_OCTOPUSES) {
                return String.valueOf(step);
            }
        }
    }

    /**
     * Parses the puzzle input lines into a {@code GRID_SIZE × GRID_SIZE} integer grid.
     * @param lines puzzle input lines
     * @return 2D array of initial energy levels
     */
    private static int[][] parseGrid(List<String> lines) {
        int[][] grid = new int[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            String line = lines.get(row);
            for (int col = 0; col < GRID_SIZE; col++) {
                grid[row][col] = line.charAt(col) - '0';
            }
        }
        return grid;
    }

    /**
     * Simulates one step of the octopus energy cycle, mutating {@code grid} in place.
     * <ol>
     *   <li>Increments every energy level by 1.</li>
     *   <li>Propagates flashes via BFS – an octopus flashes at most once per step.</li>
     *   <li>Resets all flashed octopuses to 0.</li>
     * </ol>
     * @param grid the current energy-level grid (mutated in place)
     * @return the number of flashes that occurred during this step
     */
    private static int simulateStep(int[][] grid) {
        // Stage 1 – increment every octopus's energy
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                grid[row][col]++;
            }
        }

        // Stage 2 – propagate flashes using BFS
        boolean[][] flashed = new boolean[GRID_SIZE][GRID_SIZE];
        Deque<int[]> queue = new ArrayDeque<>();

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (grid[row][col] > FLASH_THRESHOLD) {
                    flashed[row][col] = true;
                    queue.add(new int[]{row, col});
                }
            }
        }

        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int r = pos[0];
            int c = pos[1];
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) {
                        continue;
                    }
                    int nr = r + dr;
                    int nc = c + dc;
                    if (nr >= 0 && nr < GRID_SIZE && nc >= 0 && nc < GRID_SIZE) {
                        grid[nr][nc]++;
                        if (grid[nr][nc] > FLASH_THRESHOLD && !flashed[nr][nc]) {
                            flashed[nr][nc] = true;
                            queue.add(new int[]{nr, nc});
                        }
                    }
                }
            }
        }

        // Stage 3 – reset flashed octopuses to 0 and return flash count
        int flashCount = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (flashed[row][col]) {
                    grid[row][col] = 0;
                    flashCount++;
                }
            }
        }
        return flashCount;
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = DumboOctopusAOC2021Day11.class.getResourceAsStream(INPUT_FILE)) {
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
