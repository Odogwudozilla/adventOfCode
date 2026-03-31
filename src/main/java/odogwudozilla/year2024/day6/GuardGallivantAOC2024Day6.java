package odogwudozilla.year2024.day6;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;

/**
 * Guard Gallivant (Advent of Code 2024 Day 6)
 *
 * The Historians use their fancy device again, this time to whisk you all away to the North Pole prototype suit manufacturing lab... in the year 1518! It turns out that having direct access to history is very convenient for a group of historians.
 *
 * You still have to be careful of time paradoxes, and so it will be important to avoid anyone from 1518 while The Historians search for the Chief. Unfortunately, a single guard is patrolling this part of the lab.
 *
 * Maybe you can work out where the guard will go ahead of time so that The Historians can search safely?
 *
 * Official puzzle URL: https://adventofcode.com/2024/day/6
 */
public class GuardGallivantAOC2024Day6 {
    public static void main(String[] args) {
        String inputPath = "src/main/resources/2024/day6/day6_puzzle_data.txt";
        try {
            List<String> input = Files.readAllLines(Paths.get(inputPath));
            int result = solvePartOne(input);
            System.out.println("Part 1: Distinct positions visited = " + result);
            int result2 = solvePartTwo(input);
            System.out.println("Part 2: Number of possible obstruction positions = " + result2);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1 of the puzzle: How many distinct positions will the guard visit before leaving the mapped area?
     * @param input the puzzle input as a list of strings
     * @return the number of distinct positions visited
     */
    public static int solvePartOne(java.util.List<String> input) {
        final int[] dRow = {-1, 0, 1, 0}; // up, right, down, left
        final int[] dCol = {0, 1, 0, -1};
        final char[] dirChar = {'^', '>', 'v', '<'};
        int rows = input.size();
        int cols = input.get(0).length();
        int guardRow = -1, guardCol = -1, dir = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char ch = input.get(r).charAt(c);
                for (int d = 0; d < 4; d++) {
                    if (ch == dirChar[d]) {
                        guardRow = r;
                        guardCol = c;
                        dir = d;
                    }
                }
            }
        }
        java.util.Set<String> visited = new java.util.HashSet<>();
        visited.add(guardRow + "," + guardCol);
        while (true) {
            int nextRow = guardRow + dRow[dir];
            int nextCol = guardCol + dCol[dir];
            if (nextRow < 0 || nextRow >= rows || nextCol < 0 || nextCol >= cols) {
                break;
            }
            char nextCell = input.get(nextRow).charAt(nextCol);
            if (nextCell == '#') {
                dir = (dir + 1) % 4; // turn right
            } else {
                guardRow = nextRow;
                guardCol = nextCol;
                visited.add(guardRow + "," + guardCol);
            }
        }
        return visited.size();
    }

    /**
     * Solves Part 2 of the puzzle: How many different positions could you choose for the new obstruction to get the guard stuck in a loop?
     * @param input the puzzle input as a list of strings
     * @return the number of possible positions for the obstruction
     */
    public static int solvePartTwo(java.util.List<String> input) {
        // Try placing an obstruction at every empty cell (not # or guard start),
        // simulate the guard's movement, and count positions that cause a loop.
        final int[] dRow = {-1, 0, 1, 0};
        final int[] dCol = {0, 1, 0, -1};
        final char[] dirChar = {'^', '>', 'v', '<'};
        int rows = input.size();
        int cols = input.get(0).length();
        int guardRow = -1, guardCol = -1, dir = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char ch = input.get(r).charAt(c);
                for (int d = 0; d < 4; d++) {
                    if (ch == dirChar[d]) {
                        guardRow = r;
                        guardCol = c;
                        dir = d;
                    }
                }
            }
        }
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if ((r == guardRow && c == guardCol) || input.get(r).charAt(c) == '#') continue;
                // Place obstruction at (r, c)
                java.util.Set<String> seen = new java.util.HashSet<>();
                int curRow = guardRow, curCol = guardCol, curDir = dir;
                boolean loop = false;
                while (true) {
                    String state = curRow + "," + curCol + "," + curDir;
                    if (seen.contains(state)) {
                        loop = true;
                        break;
                    }
                    seen.add(state);
                    int nextRow = curRow + dRow[curDir];
                    int nextCol = curCol + dCol[curDir];
                    if (nextRow < 0 || nextRow >= rows || nextCol < 0 || nextCol >= cols) {
                        break;
                    }
                    char nextCell = (nextRow == r && nextCol == c) ? '#' : input.get(nextRow).charAt(nextCol);
                    if (nextCell == '#') {
                        curDir = (curDir + 1) % 4;
                    } else {
                        curRow = nextRow;
                        curCol = nextCol;
                    }
                }
                if (loop) count++;
            }
        }
        return count;
    }
}
