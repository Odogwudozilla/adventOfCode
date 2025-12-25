package odogwudozilla.year2025.day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Advent of Code 2025 - Day 7: Laboratories
 * <p>
 * Simulates a tachyon beam travelling through a manifold with splitters.
 * The beam starts at 'S' and moves downward. When it encounters a splitter ('^'),
 * it stops and creates two new beams moving left and right from the splitter.
 * The goal is to count how many times the beam is split.
 * <p>
 * Puzzle URL: https://adventofcode.com/2025/day/7
 */
public class LaboratoriesAOC2025Day7 {

    private static final char SPLITTER = '^';
    private static final char START = 'S';
    private static final char EMPTY = '.';
    private static final int DOWNWARD = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;

    private static class Beam {
        int row;
        int col;
        int direction;

        Beam(int row, int col, int direction) {
            this.row = row;
            this.col = col;
            this.direction = direction;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Beam)) return false;
            Beam beam = (Beam) o;
            return row == beam.row && col == beam.col && direction == beam.direction;
        }

        @Override
        public int hashCode() {
            int result = row;
            result = 31 * result + col;
            result = 31 * result + direction;
            return result;
        }
    }

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(
                Paths.get("src/main/resources/2025/day7/day7_puzzle_data.txt")
            );

            char[][] grid = parseGrid(lines);
            int splitCount = solvePartOne(grid);
            long timelineCount = solvePartTwo(grid);
            System.out.println("Part 1 - Total beam splits: " + splitCount);
            System.out.println("Part 2 - Total quantum timelines: " + timelineCount);

        } catch (IOException e) {
            System.err.println("main - Error reading puzzle input: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Parses the input lines into a 2D character grid.
     * @param lines the input lines from the puzzle data file
     * @return a 2D character array representing the manifold grid
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
     * Solve Part 1: Simulates the tachyon beam and returns the split count.
     * @param grid the manifold grid
     * @return the number of times the beam is split
     */
    private static int solvePartOne(char[][] grid) {
        return simulateTachyonBeam(grid);
    }

    /**
     * Solve Part 2: Counts quantum timelines in the manifold.
     * @param grid the manifold grid
     * @return the total quantum timelines
     */
    private static long solvePartTwo(char[][] grid) {
        return countQuantumTimelines(grid);
    }

    /**
     * Simulates the tachyon beam travelling through the manifold.
     * @param grid the manifold grid
     * @return the number of times the beam is split
     */
    private static int simulateTachyonBeam(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int splitCount = 0;

        // Find the starting position (S)
        int startRow = -1;
        int startCol = -1;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (grid[row][col] == START) {
                    startRow = row;
                    startCol = col;
                    break;
                }
            }
            if (startRow != -1) break;
        }

        // BFS to simulate beam propagation
        Queue<Beam> queue = new LinkedList<>();
        Set<Beam> visited = new HashSet<>();

        // Initial beam moving downward from S
        queue.offer(new Beam(startRow + 1, startCol, DOWNWARD));

        while (!queue.isEmpty()) {
            Beam current = queue.poll();

            // Check if beam exits the manifold
            if (current.row < 0 || current.row >= rows || current.col < 0 || current.col >= cols) {
                continue;
            }

            // Skip if already visited this beam state
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            char cell = grid[current.row][current.col];

            if (cell == SPLITTER && current.direction == DOWNWARD) {
                // Beam encounters a splitter while moving down - it splits!
                splitCount++;

                // Create two new beams: one going left, one going right
                // These beams move horizontally one step, then continue downward
                queue.offer(new Beam(current.row, current.col - 1, LEFT));
                queue.offer(new Beam(current.row, current.col + 1, RIGHT));
            } else if (cell == EMPTY || cell == SPLITTER) {
                // For horizontal beams or downward beams on empty space, continue moving
                if (current.direction == DOWNWARD) {
                    queue.offer(new Beam(current.row + 1, current.col, DOWNWARD));
                } else if (current.direction == LEFT) {
                    // After moving left, beam continues downward from new position
                    queue.offer(new Beam(current.row + 1, current.col, DOWNWARD));
                } else if (current.direction == RIGHT) {
                    // After moving right, beam continues downward from new position
                    queue.offer(new Beam(current.row + 1, current.col, DOWNWARD));
                }
            }
        }

        return splitCount;
    }

    /**
     * Counts the number of quantum timelines using many-worlds interpretation.
     * In quantum mode, a particle takes both paths at each splitter, creating
     * multiple timelines. We count the total number of distinct timelines.
     * @param grid the manifold grid
     * @return the total number of quantum timelines
     */
    private static long countQuantumTimelines(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Find the starting position (S)
        int startRow = -1;
        int startCol = -1;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (grid[row][col] == START) {
                    startRow = row;
                    startCol = col;
                    break;
                }
            }
            if (startRow != -1) break;
        }

        // Use dynamic programming to count paths
        // paths[row][col] = number of timelines reaching this position moving downward
        long[][] paths = new long[rows][cols];
        paths[startRow][startCol] = 1;

        // Process row by row
        for (int row = startRow; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (paths[row][col] == 0) continue;

                long currentPaths = paths[row][col];
                char cell = grid[row][col];

                if (cell == START || cell == EMPTY) {
                    // Continue downward
                    if (row + 1 < rows) {
                        paths[row + 1][col] += currentPaths;
                    }
                } else if (cell == SPLITTER) {
                    // Split into left and right, then continue downward
                    // Left path
                    if (col - 1 >= 0 && row + 1 < rows) {
                        paths[row + 1][col - 1] += currentPaths;
                    }
                    // Right path
                    if (col + 1 < cols && row + 1 < rows) {
                        paths[row + 1][col + 1] += currentPaths;
                    }
                }
            }
        }

        // Count total timelines that exit the manifold (reach the bottom row or exit sides)
        long totalTimelines = 0;

        // Timelines exiting through the bottom
        for (int col = 0; col < cols; col++) {
            totalTimelines += paths[rows - 1][col];
        }

        // Also need to count timelines that exit through sides during the journey
        // Actually, re-reading the problem: we need to count distinct END POSITIONS
        // Let me reconsider: we count distinct paths, which means distinct (row, col) endpoints

        // Actually, looking at the example more carefully:
        // The particle can exit anywhere - bottom or sides
        // We need to track where particles can exit and count those timeline endpoints

        // Let me revise the approach: track all paths and count unique exit points
        return countUniqueExitPaths(grid, startRow, startCol);
    }

    /**
     * Counts unique paths through the manifold by tracking timeline branches.
     * @param grid the manifold grid
     * @param startRow starting row position
     * @param startCol starting column position
     * @return the number of unique timelines (paths to exit points)
     */
    private static long countUniqueExitPaths(char[][] grid, int startRow, int startCol) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Track number of timelines at each (row, col) moving downward
        long[][] downwardPaths = new long[rows][cols];
        downwardPaths[startRow][startCol] = 1;

        long totalExitTimelines = 0;

        // Process each row
        for (int row = startRow; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (downwardPaths[row][col] == 0) continue;

                long currentTimelines = downwardPaths[row][col];

                // Try to move down one step
                if (row + 1 < rows) {
                    char nextCell = grid[row + 1][col];

                    if (nextCell == EMPTY) {
                        // Continue downward
                        downwardPaths[row + 1][col] += currentTimelines;
                    } else if (nextCell == SPLITTER) {
                        // Particle takes both paths - timeline splits
                        // Left path: move to (row+1, col-1) then continue down
                        if (col - 1 >= 0) {
                            if (row + 2 < rows) {
                                downwardPaths[row + 2][col - 1] += currentTimelines;
                            } else {
                                // Exits at bottom after going left
                                totalExitTimelines += currentTimelines;
                            }
                        } else {
                            // Left path exits through side
                            totalExitTimelines += currentTimelines;
                        }

                        // Right path: move to (row+1, col+1) then continue down
                        if (col + 1 < cols) {
                            if (row + 2 < rows) {
                                downwardPaths[row + 2][col + 1] += currentTimelines;
                            } else {
                                // Exits at bottom after going right
                                totalExitTimelines += currentTimelines;
                            }
                        } else {
                            // Right path exits through side
                            totalExitTimelines += currentTimelines;
                        }
                    }
                } else {
                    // Reached bottom row, exits manifold
                    totalExitTimelines += currentTimelines;
                }
            }
        }

        return totalExitTimelines;
    }
}

