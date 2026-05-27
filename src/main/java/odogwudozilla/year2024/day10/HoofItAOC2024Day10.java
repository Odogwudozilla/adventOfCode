package odogwudozilla.year2024.day10;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2024 - Day 10: Hoof It
 * <p>
 * Puzzle URL: https://adventofcode.com/2024/day/10
 */
public final class HoofItAOC2024Day10 {

    private static final String INPUT_FILE = "/2024/day10/day10_puzzle_data.txt";

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
    public static String solvePartOne(List<String> input) {
        int[][] grid = parseGrid(input);
        List<Point> trailheads = getTrailheads(grid);
        int sum = 0;
        for (Point th : trailheads) {
            sum += bfsTrailScore(grid, th);
        }
        return String.valueOf(sum);
    }

    /**
     * Solves Part 2 of the puzzle: count all distinct valid hiking trails from each trailhead to any 9.
     * A valid trail is a path from a 0 to a 9, only stepping to adjacent cells with height exactly one greater,
     * and never revisiting a position in the same path.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    public static String solvePartTwo(List<String> input) {
        int[][] grid = parseGrid(input);
        List<Point> trailheads = getTrailheads(grid);
        int total = 0;
        for (Point th : trailheads) {
            total += countAllTrails(grid, th);
        }
        return String.valueOf(total);
    }

    // Memoization for DFS: cache number of paths from (r,c) with given visited mask (for small grids)
    // For large grids, memoize only by (r,c) and height, since heights are strictly increasing
    private static int dfs(int[][] grid, int r, int c, int[][] memo) {
        int h = grid[r][c];
        if (h == 9) return 1;
        if (memo[r][c] != -1) return memo[r][c];
        int total = 0;
        int rows = grid.length, cols = grid[0].length;
        int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
            if (grid[nr][nc] == h + 1) {
                total += dfs(grid, nr, nc, memo);
            }
        }
        memo[r][c] = total;
        return total;
    }

    /**
     * Counts all distinct valid trails from a trailhead to any 9.
     * Uses DFS to enumerate all paths, never revisiting a cell in the same path.
     */
    private static int countAllTrails(int[][] grid, Point start) {
        int rows = grid.length, cols = grid[0].length;
        int[][] memo = new int[rows][cols];
        for (int i = 0; i < rows; i++) java.util.Arrays.fill(memo[i], -1);
        return dfs(grid, start.row, start.col, memo);
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = HoofItAOC2024Day10.class.getResourceAsStream(INPUT_FILE)) {
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

    private static class Point {
        final int row, col;
        Point(int row, int col) { this.row = row; this.col = col; }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point)) return false;
            Point p = (Point) o;
            return row == p.row && col == p.col;
        }
        @Override public int hashCode() { return 31 * row + col; }
    }

    private static int[][] parseGrid(List<String> input) {
        int rows = input.size();
        int cols = input.get(0).length();
        int[][] grid = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char ch = input.get(r).charAt(c);
                grid[r][c] = (ch >= '0' && ch <= '9') ? ch - '0' : -1;
            }
        }
        return grid;
    }

    private static List<Point> getTrailheads(int[][] grid) {
        List<Point> trailheads = new java.util.ArrayList<>();
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c] == 0) trailheads.add(new Point(r, c));
            }
        }
        return trailheads;
    }

    private static int bfsTrailScore(int[][] grid, Point start) {
        int rows = grid.length, cols = grid[0].length;
        java.util.Queue<Point> queue = new java.util.ArrayDeque<>();
        java.util.Set<Point> visited = new java.util.HashSet<>();
        java.util.Set<Point> foundNines = new java.util.HashSet<>();
        queue.add(start);
        visited.add(start);
        while (!queue.isEmpty()) {
            Point p = queue.poll();
            int h = grid[p.row][p.col];
            if (h == 9) {
                foundNines.add(p);
                continue;
            }
            int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
            for (int[] d : dirs) {
                int nr = p.row + d[0], nc = p.col + d[1];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
                if (grid[nr][nc] == h + 1) {
                    Point np = new Point(nr, nc);
                    if (!visited.contains(np)) {
                        visited.add(np);
                        queue.add(np);
                    }
                }
            }
        }
        return foundNines.size();
    }
}
