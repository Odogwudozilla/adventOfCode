package odogwudozilla.year2020.day11;

import org.jetbrains.annotations.NotNull;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Simulates the seating system as described in Advent of Code 2020 Day 11.
 * The goal is to repeatedly apply seating rules until the system stabilises and count the number of occupied seats.
 *
 * Official puzzle URL: https://adventofcode.com/2020/day/11
 *
 * Summary: By modelling the process people use to choose (or abandon) their seat in the waiting area, you can predict the best place to sit. The seat layout fits neatly on a grid. Each position is either floor (.), an empty seat (L), or an occupied seat (#). The rules are:
 * - If a seat is empty (L) and there are no occupied seats adjacent to it, the seat becomes occupied.
 * - If a seat is occupied (#) and four or more seats adjacent to it are also occupied, the seat becomes empty.
 * - Otherwise, the seat's state does not change. Floor (.) never changes.
 *
 * @author Advent of Code
 */
public class SeatingSystemAOC2020Day11 {
    public static void main(String[] args) throws Exception {
        List<String> input = Files.readAllLines(Paths.get("src/main/resources/2020/day11/day11_puzzle_data.txt"));
        char[][] grid = input.stream().map(String::toCharArray).toArray(char[][]::new);
        System.out.println("Part 1: " + solvePartOne(grid));
        System.out.println("Part 2: " + solvePartTwo(grid));
    }

    /**
     * Solves Part 1 of the puzzle: Simulate until stable and count occupied seats.
     * @param initialGrid the seating layout
     * @return the number of occupied seats when stable
     */
    public static int solvePartOne(@NotNull char[][] initialGrid) {
        char[][] grid = copyGrid(initialGrid);
        boolean changed;
        do {
            char[][] next = new char[grid.length][grid[0].length];
            changed = false;
            for (int r = 0; r < grid.length; r++) {
                for (int c = 0; c < grid[r].length; c++) {
                    if (grid[r][c] == '.') {
                        next[r][c] = '.';
                        continue;
                    }
                    int occupied = countAdjacentOccupied(grid, r, c);
                    if (grid[r][c] == 'L' && occupied == 0) {
                        next[r][c] = '#';
                        changed = true;
                    } else if (grid[r][c] == '#' && occupied >= 4) {
                        next[r][c] = 'L';
                        changed = true;
                    } else {
                        next[r][c] = grid[r][c];
                    }
                }
            }
            grid = next;
        } while (changed);
        return countOccupied(grid);
    }

    /**
     * Solves Part 2 of the puzzle: Simulate with visibility rules until stable and count occupied seats.
     * @param initialGrid the seating layout
     * @return the number of occupied seats when stable (visibility rules)
     */
    public static int solvePartTwo(@NotNull char[][] initialGrid) {
        char[][] grid = copyGrid(initialGrid);
        boolean changed;
        do {
            char[][] next = new char[grid.length][grid[0].length];
            changed = false;
            for (int r = 0; r < grid.length; r++) {
                for (int c = 0; c < grid[r].length; c++) {
                    if (grid[r][c] == '.') {
                        next[r][c] = '.';
                        continue;
                    }
                    int occupied = countVisibleOccupied(grid, r, c);
                    if (grid[r][c] == 'L' && occupied == 0) {
                        next[r][c] = '#';
                        changed = true;
                    } else if (grid[r][c] == '#' && occupied >= 5) {
                        next[r][c] = 'L';
                        changed = true;
                    } else {
                        next[r][c] = grid[r][c];
                    }
                }
            }
            grid = next;
        } while (changed);
        return countOccupied(grid);
    }

    private static int countAdjacentOccupied(char[][] grid, int r, int c) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int nr = r + dr, nc = c + dc;
                if (nr >= 0 && nr < grid.length && nc >= 0 && nc < grid[0].length) {
                    if (grid[nr][nc] == '#') count++;
                }
            }
        }
        return count;
    }

    private static int countVisibleOccupied(char[][] grid, int r, int c) {
        int count = 0;
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};
        for (int d = 0; d < 8; d++) {
            int nr = r + dr[d], nc = c + dc[d];
            while (nr >= 0 && nr < grid.length && nc >= 0 && nc < grid[0].length) {
                if (grid[nr][nc] == 'L') break;
                if (grid[nr][nc] == '#') {
                    count++;
                    break;
                }
                nr += dr[d];
                nc += dc[d];
            }
        }
        return count;
    }

    private static int countOccupied(char[][] grid) {
        int count = 0;
        for (char[] row : grid) {
            for (char seat : row) {
                if (seat == '#') count++;
            }
        }
        return count;
    }

    private static char[][] copyGrid(char[][] grid) {
        char[][] copy = new char[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            copy[i] = grid[i].clone();
        }
        return copy;
    }
}
