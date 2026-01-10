package odogwudozilla.year2019.day24;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Planet of Discord (Advent of Code 2019 Day 24)
 *
 * You land on Eris, your last stop before reaching Santa. As soon as you do, your sensors start picking up strange life forms moving around: Eris is infested with bugs! With an over 24-hour roundtrip for messages between you and Earth, you'll have to deal with this problem on your own.
 *
 * Official puzzle link: https://adventofcode.com/2019/day/24
 *
 * @param args Command line arguments
 * @return None
 */
public class PlanetOfDiscordAOC2019Day24 {
    private static final int GRID_SIZE = 5; // Size of the grid (5x5)
    private static final String INPUT_PATH = "src/main/resources/2019/day24/day24_puzzle_data.txt";

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(INPUT_PATH));
        char[][] grid = new char[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            grid[i] = lines.get(i).toCharArray();
        }
        int biodiversity = solvePartOne(grid);
        System.out.println("solvePartOne - Biodiversity rating for first repeated layout: " + biodiversity);
        int bugCount = solvePartTwo(grid, 200);
        System.out.println("solvePartTwo - Total bugs after 200 minutes: " + bugCount);
    }

    /**
     * Solves Part 1: Finds the biodiversity rating for the first repeated layout.
     * @param initialGrid The initial bug grid
     * @return Biodiversity rating
     */
    public static int solvePartOne(char[][] initialGrid) {
        Set<String> seenLayouts = new HashSet<>();
        char[][] grid = copyGrid(initialGrid);
        while (true) {
            String layout = gridToString(grid);
            if (seenLayouts.contains(layout)) {
                return calculateBiodiversity(grid);
            }
            seenLayouts.add(layout);
            grid = nextMinute(grid);
        }
    }

    /**
     * Generates the next minute's grid based on bug rules.
     * @param grid Current grid
     * @return Next grid
     */
    private static char[][] nextMinute(char[][] grid) {
        char[][] newGrid = new char[GRID_SIZE][GRID_SIZE];
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                int adjacentBugs = countAdjacentBugs(grid, x, y);
                if (grid[y][x] == '#') {
                    newGrid[y][x] = (adjacentBugs == 1) ? '#' : '.';
                } else {
                    newGrid[y][x] = (adjacentBugs == 1 || adjacentBugs == 2) ? '#' : '.';
                }
            }
        }
        return newGrid;
    }

    /**
     * Counts adjacent bugs for a given cell.
     * @param grid The grid
     * @param x X coordinate
     * @param y Y coordinate
     * @return Number of adjacent bugs
     */
    private static int countAdjacentBugs(char[][] grid, int x, int y) {
        int count = 0;
        int[][] directions = { {0, -1}, {0, 1}, {-1, 0}, {1, 0} };
        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (nx >= 0 && nx < GRID_SIZE && ny >= 0 && ny < GRID_SIZE) {
                if (grid[ny][nx] == '#') {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Converts grid to a string for layout comparison.
     * @param grid The grid
     * @return String representation
     */
    private static String gridToString(char[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                sb.append(grid[y][x]);
            }
        }
        return sb.toString();
    }

    /**
     * Calculates biodiversity rating for a grid.
     * @param grid The grid
     * @return Biodiversity rating
     */
    private static int calculateBiodiversity(char[][] grid) {
        int rating = 0;
        int power = 1;
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                if (grid[y][x] == '#') {
                    rating += power;
                }
                power <<= 1;
            }
        }
        return rating;
    }

    /**
     * Copies a grid.
     * @param grid The grid
     * @return Copy of the grid
     */
    private static char[][] copyGrid(char[][] grid) {
        char[][] copy = new char[GRID_SIZE][GRID_SIZE];
        for (int y = 0; y < GRID_SIZE; y++) {
            System.arraycopy(grid[y], 0, copy[y], 0, GRID_SIZE);
        }
        return copy;
    }

    /**
     * Solves Part 2: Simulates recursive grids for 200 minutes and counts bugs.
     * @param initialGrid The initial bug grid
     * @param minutes Number of minutes to simulate
     * @return Total bug count after simulation
     */
    public static int solvePartTwo(char[][] initialGrid, int minutes) {
        // Each level is a 5x5 grid, middle cell is always '?', not used
        // Use a map: level -> grid
        java.util.Map<Integer, char[][]> levels = new java.util.HashMap<>();
        levels.put(0, clearMiddle(initialGrid));
        for (int m = 0; m < minutes; m++) {
            // Expand levels if needed
            if (hasBugs(levels.get(getMinLevel(levels)))) {
                levels.put(getMinLevel(levels) - 1, emptyGrid());
            }
            if (hasBugs(levels.get(getMaxLevel(levels)))) {
                levels.put(getMaxLevel(levels) + 1, emptyGrid());
            }
            java.util.Map<Integer, char[][]> newLevels = new java.util.HashMap<>();
            for (int level : levels.keySet()) {
                newLevels.put(level, nextRecursiveMinute(levels, level));
            }
            levels = newLevels;
        }
        int totalBugs = 0;
        for (char[][] grid : levels.values()) {
            totalBugs += countBugs(grid);
        }
        return totalBugs;
    }

    // Helper for Part 2: Clear middle cell
    private static char[][] clearMiddle(char[][] grid) {
        char[][] copy = copyGrid(grid);
        copy[2][2] = '?';
        return copy;
    }

    // Helper for Part 2: Create empty grid with middle cell '?'
    private static char[][] emptyGrid() {
        char[][] grid = new char[GRID_SIZE][GRID_SIZE];
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                grid[y][x] = '.';
            }
        }
        grid[2][2] = '?';
        return grid;
    }

    // Helper for Part 2: Count bugs in a grid
    private static int countBugs(char[][] grid) {
        int count = 0;
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                if (grid[y][x] == '#') count++;
            }
        }
        return count;
    }

    // Helper for Part 2: Get min level
    private static int getMinLevel(java.util.Map<Integer, char[][]> levels) {
        return levels.keySet().stream().min(Integer::compareTo).orElse(0);
    }

    // Helper for Part 2: Get max level
    private static int getMaxLevel(java.util.Map<Integer, char[][]> levels) {
        return levels.keySet().stream().max(Integer::compareTo).orElse(0);
    }

    // Helper for Part 2: Check if grid has bugs
    private static boolean hasBugs(char[][] grid) {
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                if (grid[y][x] == '#') return true;
            }
        }
        return false;
    }

    // Helper for Part 2: Compute next minute for a level
    private static char[][] nextRecursiveMinute(java.util.Map<Integer, char[][]> levels, int level) {
        char[][] grid = levels.get(level);
        char[][] newGrid = new char[GRID_SIZE][GRID_SIZE];
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                if (y == 2 && x == 2) {
                    newGrid[y][x] = '?';
                    continue;
                }
                int adjacentBugs = countRecursiveAdjacent(levels, level, x, y);
                if (grid[y][x] == '#') {
                    newGrid[y][x] = (adjacentBugs == 1) ? '#' : '.';
                } else if (grid[y][x] == '.') {
                    newGrid[y][x] = (adjacentBugs == 1 || adjacentBugs == 2) ? '#' : '.';
                } else {
                    newGrid[y][x] = grid[y][x];
                }
            }
        }
        return newGrid;
    }

    // Helper for Part 2: Count adjacent bugs recursively
    private static int countRecursiveAdjacent(java.util.Map<Integer, char[][]> levels, int level, int x, int y) {
        int count = 0;
        int[][] directions = { {0, -1}, {0, 1}, {-1, 0}, {1, 0} };
        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (nx == 2 && ny == 2) {
                // Into inner grid (level+1)
                char[][] inner = levels.getOrDefault(level + 1, emptyGrid());
                if (x == 2 && y == 1) { // Up into inner
                    for (int ix = 0; ix < GRID_SIZE; ix++) if (inner[0][ix] == '#') count++;
                } else if (x == 2 && y == 3) { // Down into inner
                    for (int ix = 0; ix < GRID_SIZE; ix++) if (inner[4][ix] == '#') count++;
                } else if (x == 1 && y == 2) { // Left into inner
                    for (int iy = 0; iy < GRID_SIZE; iy++) if (inner[iy][0] == '#') count++;
                } else if (x == 3 && y == 2) { // Right into inner
                    for (int iy = 0; iy < GRID_SIZE; iy++) if (inner[iy][4] == '#') count++;
                }
            } else if (nx < 0 || nx >= GRID_SIZE || ny < 0 || ny >= GRID_SIZE) {
                // Out to outer grid (level-1)
                char[][] outer = levels.getOrDefault(level - 1, emptyGrid());
                if (nx < 0) { if (outer[2][1] == '#') count++; }
                if (nx >= GRID_SIZE) { if (outer[2][3] == '#') count++; }
                if (ny < 0) { if (outer[1][2] == '#') count++; }
                if (ny >= GRID_SIZE) { if (outer[3][2] == '#') count++; }
            } else {
                // Normal adjacent
                char[][] current = levels.get(level);
                if (current[ny][nx] == '#') count++;
            }
        }
        return count;
    }
}
