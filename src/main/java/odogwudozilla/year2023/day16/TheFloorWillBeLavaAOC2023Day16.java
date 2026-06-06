package odogwudozilla.year2023.day16;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Advent of Code 2023 - Day 16: The Floor Will Be Lava
 * <p>
 * A beam of light travels through a 2-D grid of mirrors ({@code /} and {@code \})
 * and splitters ({@code |} and {@code -}). Mirrors reflect the beam 90 degrees;
 * splitters either pass the beam through or split it into two perpendicular beams.
 * A tile is "energised" once any beam passes through, reflects in, or splits at it.
 * <p>
 * Part 1: Count energised tiles with the beam entering the top-left corner heading right.
 * Part 2: Find the edge entry point and direction that maximises the number of energised tiles.
 * <p>
 * Puzzle URL: https://adventofcode.com/2023/day/16
 */
public final class TheFloorWillBeLavaAOC2023Day16 {

    private static final String INPUT_FILE = "/2023/day16/day16_puzzle_data.txt";

    /** Direction constants. */
    private static final int UP    = 0;
    private static final int RIGHT = 1;
    private static final int DOWN  = 2;
    private static final int LEFT  = 3;

    /** Row delta for each direction: UP, RIGHT, DOWN, LEFT. */
    private static final int[] DR = {-1, 0, 1, 0};
    /** Column delta for each direction: UP, RIGHT, DOWN, LEFT. */
    private static final int[] DC = {0, 1, 0, -1};

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
     * Solves Part 1: counts energised tiles with the beam entering the top-left
     * corner of the grid heading right.
     *
     * @param input list of input lines
     * @return the Part 1 answer
     */
    private static String solvePartOne(List<String> input) {
        char[][] grid = toGrid(input);
        return String.valueOf(countEnergised(grid, 0, 0, RIGHT));
    }

    /**
     * Solves Part 2: tries every edge entry point and direction, returning the
     * maximum number of energised tiles achievable.
     *
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        char[][] grid = toGrid(input);
        int rows = grid.length;
        int cols = grid[0].length;
        int maxEnergised = 0;

        // Top and bottom rows – beam enters heading down or up
        for (int c = 0; c < cols; c++) {
            maxEnergised = Math.max(maxEnergised, countEnergised(grid, 0,        c, DOWN));
            maxEnergised = Math.max(maxEnergised, countEnergised(grid, rows - 1, c, UP));
        }
        // Left and right columns – beam enters heading right or left
        for (int r = 0; r < rows; r++) {
            maxEnergised = Math.max(maxEnergised, countEnergised(grid, r, 0,        RIGHT));
            maxEnergised = Math.max(maxEnergised, countEnergised(grid, r, cols - 1, LEFT));
        }

        return String.valueOf(maxEnergised);
    }

    /**
     * Counts the number of energised tiles when a beam starts at
     * ({@code startRow}, {@code startCol}) heading in {@code startDir}.
     * Uses BFS and tracks visited (row, col, direction) states to break cycles.
     *
     * @param grid     the 2-D tile grid
     * @param startRow starting row index (0-based)
     * @param startCol starting column index (0-based)
     * @param startDir starting direction – one of {@link #UP}, {@link #RIGHT},
     *                 {@link #DOWN}, {@link #LEFT}
     * @return number of distinct energised tiles
     */
    private static int countEnergised(char[][] grid, int startRow, int startCol, int startDir) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Encode (row, col, direction) as a single integer for fast hashing
        Set<Integer> visited = new HashSet<>();
        Deque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{startRow, startCol, startDir});

        while (!queue.isEmpty()) {
            int[] state = queue.poll();
            int r = state[0];
            int c = state[1];
            int dir = state[2];

            if (r < 0 || r >= rows || c < 0 || c >= cols) {
                continue; // Beam has left the grid
            }

            int encoded = r * cols * 4 + c * 4 + dir;
            if (!visited.add(encoded)) {
                continue; // Cycle detected – already processed this state
            }

            char tile = grid[r][c];
            switch (tile) {
                case '.':
                    queue.add(new int[]{r + DR[dir], c + DC[dir], dir});
                    break;
                case '/': {
                    int nd = reflectSlash(dir);
                    queue.add(new int[]{r + DR[nd], c + DC[nd], nd});
                    break;
                }
                case '\\': {
                    int nd = reflectBackslash(dir);
                    queue.add(new int[]{r + DR[nd], c + DC[nd], nd});
                    break;
                }
                case '|':
                    if (dir == UP || dir == DOWN) {
                        queue.add(new int[]{r + DR[dir], c + DC[dir], dir});
                    } else {
                        queue.add(new int[]{r + DR[UP],   c + DC[UP],   UP});
                        queue.add(new int[]{r + DR[DOWN], c + DC[DOWN], DOWN});
                    }
                    break;
                case '-':
                    if (dir == LEFT || dir == RIGHT) {
                        queue.add(new int[]{r + DR[dir], c + DC[dir], dir});
                    } else {
                        queue.add(new int[]{r + DR[LEFT],  c + DC[LEFT],  LEFT});
                        queue.add(new int[]{r + DR[RIGHT], c + DC[RIGHT], RIGHT});
                    }
                    break;
                default:
                    queue.add(new int[]{r + DR[dir], c + DC[dir], dir});
            }
        }

        // Collect unique (row, col) positions from all visited states
        Set<Integer> energisedTiles = new HashSet<>();
        for (int encoded : visited) {
            int r = encoded / (cols * 4);
            int c = (encoded % (cols * 4)) / 4;
            energisedTiles.add(r * cols + c);
        }
        return energisedTiles.size();
    }

    /**
     * Reflects a beam direction off a {@code /} mirror.
     * RIGHT→UP, UP→RIGHT, LEFT→DOWN, DOWN→LEFT.
     */
    private static int reflectSlash(int dir) {
        switch (dir) {
            case RIGHT: return UP;
            case UP:    return RIGHT;
            case LEFT:  return DOWN;
            case DOWN:  return LEFT;
            default:    return dir;
        }
    }

    /**
     * Reflects a beam direction off a {@code \} mirror.
     * RIGHT→DOWN, DOWN→RIGHT, LEFT→UP, UP→LEFT.
     */
    private static int reflectBackslash(int dir) {
        switch (dir) {
            case RIGHT: return DOWN;
            case DOWN:  return RIGHT;
            case LEFT:  return UP;
            case UP:    return LEFT;
            default:    return dir;
        }
    }

    /**
     * Converts the list of input lines into a 2-D character array grid.
     */
    private static char[][] toGrid(List<String> lines) {
        char[][] grid = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }
        return grid;
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = TheFloorWillBeLavaAOC2023Day16.class.getResourceAsStream(INPUT_FILE)) {
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
