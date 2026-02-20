package odogwudozilla.year2022.day24;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Set;

/**
 * Blizzard Basin (Advent of Code 2022 Day 24)
 *
 * With everything replanted for next year (and with elephants and monkeys to tend the grove), you and the Elves leave for the extraction point.
 * Partway up the mountain that shields the grove is a flat, open area that serves as the extraction point. It's a bit of a climb, but nothing the expedition can't handle.
 * At least, that would normally be true; now that the mountain is covered in snow, things have become more difficult than the Elves are used to.
 * ... (see puzzle description for full details)
 *
 * Official puzzle link: https://adventofcode.com/2022/day/24
 *
 * @author Advent of Code participant
 */
public class BlizzardBasinAOC2022Day24 {
    private static final String INPUT_PATH = "src/main/resources/2022/day24/day24_puzzle_data.txt";

    public static void main(String[] args) {
        try {
            List<String> input = Files.readAllLines(Paths.get(INPUT_PATH));
            System.out.println("Part 1: " + solvePartOne(input));
            System.out.println("Part 2: " + solvePartTwo(input));
        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1 of the Blizzard Basin puzzle.
     * @param input The puzzle input as a list of strings.
     * @return The fewest number of minutes required to avoid the blizzards and reach the goal.
     */
    public static int solvePartOne(List<String> input) {
        final int height = input.size();
        final int width = input.get(0).length();
        final char[][] grid = new char[height][width];
        for (int i = 0; i < height; i++) {
            grid[i] = input.get(i).toCharArray();
        }

        // Directions: up, down, left, right
        final int[][] DIRS = { {-1,0}, {1,0}, {0,-1}, {0,1} };
        final char[] DIR_CHARS = {'^','v','<','>'};

        // Blizzard positions by direction
        List<int[]> up = new LinkedList<>();
        List<int[]> down = new LinkedList<>();
        List<int[]> left = new LinkedList<>();
        List<int[]> right = new LinkedList<>();
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                char ch = grid[r][c];
                if (ch == '^') up.add(new int[]{r, c});
                if (ch == 'v') down.add(new int[]{r, c});
                if (ch == '<') left.add(new int[]{r, c});
                if (ch == '>') right.add(new int[]{r, c});
            }
        }

        // Find entrance and exit
        int startCol = -1, endCol = -1;
        for (int c = 0; c < width; c++) {
            if (grid[0][c] == '.') startCol = c;
            if (grid[height-1][c] == '.') endCol = c;
        }
        final int startRow = 0, endRow = height-1;

        // Precompute blizzard positions for each minute (cycle length = lcm(height-2, width-2))
        int cycle = lcm(height-2, width-2);
        boolean[][][] blizzard = new boolean[cycle][height][width];
        List<int[]>[] blizzards = new List[]{up, down, left, right};
        for (int t = 0; t < cycle; t++) {
            boolean[][] b = new boolean[height][width];
            for (int d = 0; d < 4; d++) {
                for (int[] pos : blizzards[d]) {
                    int r = pos[0], c = pos[1];
                    int nr = r, nc = c;
                    if (d == 0) nr = 1 + mod(r-1-t, height-2); // up
                    if (d == 1) nr = 1 + mod(r-1+t, height-2); // down
                    if (d == 2) nc = 1 + mod(c-1-t, width-2); // left
                    if (d == 3) nc = 1 + mod(c-1+t, width-2); // right
                    b[nr][nc] = true;
                }
            }
            blizzard[t] = b;
        }

        // BFS: state = (row, col, minute)
        Queue<int[]> queue = new LinkedList<>();
        Set<String> seen = new HashSet<>();
        queue.add(new int[]{startRow, startCol, 0});
        seen.add(startRow+","+startCol+",0");
        while (!queue.isEmpty()) {
            int[] state = queue.poll();
            int r = state[0], c = state[1], t = state[2];
            if (r == endRow && c == endCol) return t;
            int nextT = (t+1)%cycle;
            for (int[] dir : DIRS) {
                int nr = r + dir[0], nc = c + dir[1];
                if (nr < 0 || nr >= height || nc < 0 || nc >= width) continue;
                if (grid[nr][nc] == '#') continue;
                if (blizzard[nextT][nr][nc]) continue;
                String key = nr+","+nc+","+nextT;
                if (seen.contains(key)) continue;
                seen.add(key);
                queue.add(new int[]{nr, nc, t+1});
            }
            // Wait in place
            if (!blizzard[nextT][r][c]) {
                String key = r+","+c+","+nextT;
                if (!seen.contains(key)) {
                    seen.add(key);
                    queue.add(new int[]{r, c, t+1});
                }
            }
        }
        return -1; // No path found
    }

    /**
     * Solves Part 2 of the Blizzard Basin puzzle.
     * @param input The puzzle input as a list of strings.
     * @return The fewest number of minutes required to reach the goal, return to start, then reach the goal again.
     */
    public static int solvePartTwo(List<String> input) {
        final int height = input.size();
        final int width = input.get(0).length();
        final char[][] grid = new char[height][width];
        for (int i = 0; i < height; i++) {
            grid[i] = input.get(i).toCharArray();
        }
        // Find entrance and exit
        int startCol = -1, endCol = -1;
        for (int c = 0; c < width; c++) {
            if (grid[0][c] == '.') startCol = c;
            if (grid[height-1][c] == '.') endCol = c;
        }
        final int startRow = 0, endRow = height-1;
        int cycle = lcm(height-2, width-2);
        boolean[][][] blizzard = new boolean[cycle][height][width];
        // Blizzard positions by direction
        List<int[]> up = new LinkedList<>();
        List<int[]> down = new LinkedList<>();
        List<int[]> left = new LinkedList<>();
        List<int[]> right = new LinkedList<>();
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                char ch = grid[r][c];
                if (ch == '^') up.add(new int[]{r, c});
                if (ch == 'v') down.add(new int[]{r, c});
                if (ch == '<') left.add(new int[]{r, c});
                if (ch == '>') right.add(new int[]{r, c});
            }
        }
        List<int[]>[] blizzards = new List[]{up, down, left, right};
        for (int t = 0; t < cycle; t++) {
            boolean[][] b = new boolean[height][width];
            for (int d = 0; d < 4; d++) {
                for (int[] pos : blizzards[d]) {
                    int r = pos[0], c = pos[1];
                    int nr = r, nc = c;
                    if (d == 0) nr = 1 + mod(r-1-t, height-2); // up
                    if (d == 1) nr = 1 + mod(r-1+t, height-2); // down
                    if (d == 2) nc = 1 + mod(c-1-t, width-2); // left
                    if (d == 3) nc = 1 + mod(c-1+t, width-2); // right
                    b[nr][nc] = true;
                }
            }
            blizzard[t] = b;
        }
        // Three trips: start->goal, goal->start, start->goal
        int t1 = bfsWithBlizzards(grid, blizzard, startRow, startCol, endRow, endCol, 0);
        int t2 = bfsWithBlizzards(grid, blizzard, endRow, endCol, startRow, startCol, t1);
        int t3 = bfsWithBlizzards(grid, blizzard, startRow, startCol, endRow, endCol, t2);
        return t3;
    }

    /**
     * BFS helper for Part 2, starting at a given time offset.
     */
    private static int bfsWithBlizzards(char[][] grid, boolean[][][] blizzard, int startRow, int startCol, int endRow, int endCol, int startTime) {
        int height = grid.length, width = grid[0].length;
        int cycle = blizzard.length;
        final int[][] DIRS = { {-1,0}, {1,0}, {0,-1}, {0,1} };
        Queue<int[]> queue = new LinkedList<>();
        Set<String> seen = new HashSet<>();
        queue.add(new int[]{startRow, startCol, startTime});
        seen.add(startRow+","+startCol+","+(startTime%cycle));
        while (!queue.isEmpty()) {
            int[] state = queue.poll();
            int r = state[0], c = state[1], t = state[2];
            if (r == endRow && c == endCol) return t;
            int nextT = (t+1)%cycle;
            for (int[] dir : DIRS) {
                int nr = r + dir[0], nc = c + dir[1];
                if (nr < 0 || nr >= height || nc < 0 || nc >= width) continue;
                if (grid[nr][nc] == '#') continue;
                if (blizzard[nextT][nr][nc]) continue;
                String key = nr+","+nc+","+nextT;
                if (seen.contains(key)) continue;
                seen.add(key);
                queue.add(new int[]{nr, nc, t+1});
            }
            // Wait in place
            if (!blizzard[nextT][r][c]) {
                String key = r+","+c+","+nextT;
                if (!seen.contains(key)) {
                    seen.add(key);
                    queue.add(new int[]{r, c, t+1});
                }
            }
        }
        return -1;
    }

    private static int lcm(int a, int b) {
        return a * b / gcd(a, b);
    }
    private static int gcd(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return a;
    }
    private static int mod(int x, int m) {
        int r = x % m;
        return r < 0 ? r + m : r;
    }
}
