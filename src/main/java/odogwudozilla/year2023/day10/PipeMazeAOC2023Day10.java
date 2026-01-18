package odogwudozilla.year2023.day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Advent of Code 2023 - Day 10: Pipe Maze
 * <p>
 * This puzzle involves navigating through a two-dimensional grid of pipes to find a continuous loop.
 * The goal is to determine the farthest point from the starting position along the loop.
 * <p>
 * Puzzle URL: https://adventofcode.com/2023/day/10
 */
public class PipeMazeAOC2023Day10 {

    private static final String INPUT_FILE = "src/main/resources/2023/day10/day10_puzzle_data.txt";

    // Direction vectors: North, South, East, West
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
    private static final int NORTH = 0;
    private static final int SOUTH = 1;
    private static final int EAST = 2;
    private static final int WEST = 3;

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(INPUT_FILE));

            System.out.println("=== Advent of Code 2023 - Day 10: Pipe Maze ===\n");

            long part1Result = solvePartOne(lines);
            System.out.println("Part 1 - Farthest distance from start: " + part1Result);

            long part2Result = solvePartTwo(lines);
            System.out.println("Part 2 - Tiles enclosed by loop: " + part2Result);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Solves Part 1: Find the farthest point from the starting position in the pipe loop.
     * @param lines the puzzle input as a list of strings
     * @return the maximum distance from the starting position
     */
    public static long solvePartOne(List<String> lines) {
        char[][] grid = parseGrid(lines);
        int[] start = findStart(grid);

        // Find the loop and calculate distances
        Map<String, Integer> distances = traverseLoop(grid, start);

        // The farthest point is the maximum distance in the map
        return distances.values().stream().mapToInt(Integer::intValue).max().orElse(0);
    }

    /**
     * Solves Part 2: Count the number of tiles enclosed by the loop.
     * Uses a scan-line algorithm with ray-casting to determine inside/outside status.
     * @param lines the puzzle input as a list of strings
     * @return the number of tiles enclosed by the loop
     */
    public static long solvePartTwo(List<String> lines) {
        char[][] grid = parseGrid(lines);
        int[] start = findStart(grid);

        // Find all tiles that are part of the main loop
        Set<String> loopTiles = findLoopTiles(grid, start);

        // Replace 'S' with its actual pipe character
        replaceStartWithActualPipe(grid, start, loopTiles);

        // Count tiles enclosed by the loop using scan-line algorithm
        return countEnclosedTiles(grid, loopTiles);
    }

    /**
     * Finds all tiles that are part of the main loop.
     * @param grid the 2D character grid
     * @param start the starting position [row, col]
     * @return set of position keys for tiles in the loop
     */
    private static Set<String> findLoopTiles(char[][] grid, int[] start) {
        Set<String> loopTiles = new HashSet<>();
        Queue<int[]> queue = new LinkedList<>();

        queue.offer(start);
        loopTiles.add(start[0] + "," + start[1]);

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];

            char pipe = grid[row][col];
            List<Integer> validDirections = getValidDirections(pipe);

            for (int dir : validDirections) {
                int newRow = row + DIRECTIONS[dir][0];
                int newCol = col + DIRECTIONS[dir][1];

                if (newRow < 0 || newRow >= grid.length || newCol < 0 || newCol >= grid[0].length) {
                    continue;
                }

                String key = newRow + "," + newCol;
                char nextPipe = grid[newRow][newCol];

                if ('.' == nextPipe || loopTiles.contains(key)) {
                    continue;
                }

                if (canConnect(dir, nextPipe)) {
                    loopTiles.add(key);
                    queue.offer(new int[]{newRow, newCol});
                }
            }
        }

        return loopTiles;
    }

    /**
     * Replaces the 'S' starting position with its actual pipe character.
     * @param grid the 2D character grid
     * @param start the starting position [row, col]
     * @param loopTiles set of tiles in the loop
     */
    private static void replaceStartWithActualPipe(char[][] grid, int[] start, Set<String> loopTiles) {
        int row = start[0];
        int col = start[1];

        boolean north = false;
        boolean south = false;
        boolean east = false;
        boolean west = false;

        // Check which directions connect to S
        if (row > 0 && loopTiles.contains((row - 1) + "," + col)) {
            char pipe = grid[row - 1][col];
            if ('|' == pipe || '7' == pipe || 'F' == pipe) {
                north = true;
            }
        }

        if (row < grid.length - 1 && loopTiles.contains((row + 1) + "," + col)) {
            char pipe = grid[row + 1][col];
            if ('|' == pipe || 'L' == pipe || 'J' == pipe) {
                south = true;
            }
        }

        if (col < grid[0].length - 1 && loopTiles.contains(row + "," + (col + 1))) {
            char pipe = grid[row][col + 1];
            if ('-' == pipe || 'J' == pipe || '7' == pipe) {
                east = true;
            }
        }

        if (col > 0 && loopTiles.contains(row + "," + (col - 1))) {
            char pipe = grid[row][col - 1];
            if ('-' == pipe || 'L' == pipe || 'F' == pipe) {
                west = true;
            }
        }

        // Determine the actual pipe character
        if (north && south) {
            grid[row][col] = '|';
        } else if (east && west) {
            grid[row][col] = '-';
        } else if (north && east) {
            grid[row][col] = 'L';
        } else if (north && west) {
            grid[row][col] = 'J';
        } else if (south && west) {
            grid[row][col] = '7';
        } else if (south && east) {
            grid[row][col] = 'F';
        }
    }

    /**
     * Counts the number of tiles enclosed by the loop using scan-line algorithm.
     * For each row, we scan left to right and track when we cross the boundary.
     * @param grid the 2D character grid
     * @param loopTiles set of tiles in the loop
     * @return the count of enclosed tiles
     */
    private static int countEnclosedTiles(char[][] grid, Set<String> loopTiles) {
        int count = 0;

        for (int row = 0; row < grid.length; row++) {
            boolean inside = false;
            char enterPipe = ' ';

            for (int col = 0; col < grid[0].length; col++) {
                String key = row + "," + col;

                if (loopTiles.contains(key)) {
                    char pipe = grid[row][col];

                    // Vertical pipe always toggles inside/outside
                    if ('|' == pipe) {
                        inside = !inside;
                    }
                    // F and L are entry points (bottom and top corners)
                    else if ('F' == pipe || 'L' == pipe) {
                        enterPipe = pipe;
                    }
                    // J and 7 are exit points
                    else if ('J' == pipe) {
                        // F...J is a U-turn that crosses the boundary
                        // L...J is a straight line that doesn't cross
                        if ('F' == enterPipe) {
                            inside = !inside;
                        }
                        enterPipe = ' ';
                    } else if ('7' == pipe) {
                        // L...7 is a U-turn that crosses the boundary
                        // F...7 is a straight line that doesn't cross
                        if ('L' == enterPipe) {
                            inside = !inside;
                        }
                        enterPipe = ' ';
                    }
                    // Horizontal pipes don't change inside/outside status
                } else if (inside) {
                    // Not part of loop and we're inside
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * Parses the input lines into a 2D character grid.
     * @param lines the puzzle input
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
     * Finds the starting position 'S' in the grid.
     * @param grid the 2D character grid
     * @return array containing [row, col] of the starting position
     */
    private static int[] findStart(char[][] grid) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if ('S' == grid[row][col]) {
                    return new int[]{row, col};
                }
            }
        }
        throw new IllegalStateException("Starting position 'S' not found in grid");
    }

    /**
     * Traverses the pipe loop starting from the starting position and calculates distances.
     * @param grid the 2D character grid
     * @param start the starting position [row, col]
     * @return map of position keys to distances from start
     */
    private static Map<String, Integer> traverseLoop(char[][] grid, int[] start) {
        Map<String, Integer> distances = new HashMap<>();
        Queue<int[]> queue = new LinkedList<>();

        // Start position has distance 0
        queue.offer(new int[]{start[0], start[1], 0});
        distances.put(start[0] + "," + start[1], 0);

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];
            int distance = current[2];

            char pipe = grid[row][col];

            // Get valid next directions based on current pipe type
            List<Integer> validDirections = getValidDirections(pipe);

            for (int dir : validDirections) {
                int newRow = row + DIRECTIONS[dir][0];
                int newCol = col + DIRECTIONS[dir][1];

                // Check bounds
                if (newRow < 0 || newRow >= grid.length || newCol < 0 || newCol >= grid[0].length) {
                    continue;
                }

                String key = newRow + "," + newCol;
                char nextPipe = grid[newRow][newCol];

                // Skip ground tiles
                if ('.' == nextPipe) {
                    continue;
                }

                // Check if the next pipe can connect back to current direction
                if (!canConnect(dir, nextPipe)) {
                    continue;
                }

                // If not visited or found shorter path
                if (!distances.containsKey(key)) {
                    distances.put(key, distance + 1);
                    queue.offer(new int[]{newRow, newCol, distance + 1});
                }
            }
        }

        return distances;
    }

    /**
     * Gets the valid directions a pipe can connect to.
     * @param pipe the pipe character
     * @return list of valid direction indices
     */
    private static List<Integer> getValidDirections(char pipe) {
        List<Integer> directions = new ArrayList<>();

        switch (pipe) {
            case '|': // Vertical pipe: North and South
                directions.add(NORTH);
                directions.add(SOUTH);
                break;
            case '-': // Horizontal pipe: East and West
                directions.add(EAST);
                directions.add(WEST);
                break;
            case 'L': // 90-degree bend: North and East
                directions.add(NORTH);
                directions.add(EAST);
                break;
            case 'J': // 90-degree bend: North and West
                directions.add(NORTH);
                directions.add(WEST);
                break;
            case '7': // 90-degree bend: South and West
                directions.add(SOUTH);
                directions.add(WEST);
                break;
            case 'F': // 90-degree bend: South and East
                directions.add(SOUTH);
                directions.add(EAST);
                break;
            case 'S': // Starting position: try all directions
                directions.add(NORTH);
                directions.add(SOUTH);
                directions.add(EAST);
                directions.add(WEST);
                break;
        }

        return directions;
    }

    /**
     * Checks if a pipe can connect from a given direction.
     * @param fromDirection the direction we're coming from
     * @param pipe the pipe character to check
     * @return true if the connection is valid
     */
    private static boolean canConnect(int fromDirection, char pipe) {
        switch (pipe) {
            case '|':
                return NORTH == fromDirection || SOUTH == fromDirection;
            case '-':
                return EAST == fromDirection || WEST == fromDirection;
            case 'L':
                return SOUTH == fromDirection || WEST == fromDirection;
            case 'J':
                return SOUTH == fromDirection || EAST == fromDirection;
            case '7':
                return NORTH == fromDirection || EAST == fromDirection;
            case 'F':
                return NORTH == fromDirection || WEST == fromDirection;
            case 'S':
                return true;
            default:
                return false;
        }
    }
}

