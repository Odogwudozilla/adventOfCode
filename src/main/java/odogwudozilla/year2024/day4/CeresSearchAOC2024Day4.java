package odogwudozilla.year2024.day4;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;

/**
 * Advent of Code 2024 Day 4: Ceres Search
 *
 * "Looks like the Chief's not here. Next!" One of The Historians pulls out a device and pushes the only button on it. After a brief flash, you recognise the interior of the Ceres monitoring station!
 *
 * As the search for the Chief continues, a small Elf who lives on the station tugs on your shirt; she'd like to know if you could help her with her word search (your puzzle input). She only has to find one word: XMAS.
 *
 * This word search allows words to be horizontal, vertical, diagonal, written backwards, or even overlapping other words. It's a little unusual, though, as you don't merely need to find one instance of XMAS - you need to find all of them.
 *
 * Official puzzle link: https://adventofcode.com/2024/day/4
 *
 * @param args Command line arguments (not used)
 */
public class CeresSearchAOC2024Day4 {
    private static final String PUZZLE_INPUT_PATH = "src/main/resources/2024/day4/day4_puzzle_data.txt";
    private static final String TARGET_WORD = "XMAS";

    /**
     * Main method to run the solution for Advent of Code 2024 Day 4.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            List<String> grid = Files.readAllLines(Paths.get(PUZZLE_INPUT_PATH));
            int partOneResult = solvePartOne(grid);
            System.out.println("Part 1: Number of times XMAS appears: " + partOneResult);
            int partTwoResult = solvePartTwo(grid);
            System.out.println("Part 2: Number of X-MAS patterns: " + partTwoResult);
        } catch (IOException e) {
            System.err.println("main - Error reading puzzle input: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1: Counts the number of times XMAS appears in the grid in any direction.
     * @param grid The word search grid as a list of strings
     * @return The total number of occurrences of XMAS
     */
    public static int solvePartOne(List<String> grid) {
        // Directions: right, left, down, up, diagonals
        final int[][] DIRECTIONS = {
            {0, 1},   // right
            {0, -1},  // left
            {1, 0},   // down
            {-1, 0},  // up
            {1, 1},   // down-right
            {-1, -1}, // up-left
            {1, -1},  // down-left
            {-1, 1}   // up-right
        };
        int count = 0;
        int numRows = grid.size();
        int numCols = grid.get(0).length();
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                for (int[] dir : DIRECTIONS) {
                    if (matchesWord(grid, row, col, dir[0], dir[1], TARGET_WORD)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Solves Part 2: Counts the number of X-MAS patterns in the grid as described in the puzzle.
     * An X-MAS is an X shape (3x3) centred at 'A', with both diagonals spelling MAS or SAM (forwards or backwards).
     * @param grid The word search grid as a list of strings
     * @return The total number of X-MAS patterns
     */
    public static int solvePartTwo(List<String> grid) {
        final char CENTRE_LETTER = 'A';
        final String[] DIAGONAL_WORDS = {"MAS", "SAM", "SAM", "MAS"}; // Forwards and backwards
        int count = 0;
        int numRows = grid.size();
        int numCols = grid.get(0).length();
        // The X shape is 3x3, so skip borders
        for (int row = 1; row < numRows - 1; row++) {
            for (int col = 1; col < numCols - 1; col++) {
                if (grid.get(row).charAt(col) != CENTRE_LETTER) {
                    continue; // Centre must be 'A'
                }
                boolean diag1 = false;
                boolean diag2 = false;
                // Diagonal 1: top-left to bottom-right
                String diag1Word = "" + grid.get(row - 1).charAt(col - 1) + grid.get(row).charAt(col) + grid.get(row + 1).charAt(col + 1);
                String diag1WordRev = "" + grid.get(row + 1).charAt(col + 1) + grid.get(row).charAt(col) + grid.get(row - 1).charAt(col - 1);
                for (String w : DIAGONAL_WORDS) {
                    if (diag1Word.equals(w) || diag1WordRev.equals(w)) {
                        diag1 = true;
                        break;
                    }
                }
                // Diagonal 2: top-right to bottom-left
                String diag2Word = "" + grid.get(row - 1).charAt(col + 1) + grid.get(row).charAt(col) + grid.get(row + 1).charAt(col - 1);
                String diag2WordRev = "" + grid.get(row + 1).charAt(col - 1) + grid.get(row).charAt(col) + grid.get(row - 1).charAt(col + 1);
                for (String w : DIAGONAL_WORDS) {
                    if (diag2Word.equals(w) || diag2WordRev.equals(w)) {
                        diag2 = true;
                        break;
                    }
                }
                if (diag1 && diag2) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Checks if the target word matches starting from (row, col) in the given direction.
     * @param grid The word search grid
     * @param row Starting row
     * @param col Starting column
     * @param dRow Row direction
     * @param dCol Column direction
     * @param word The word to match
     * @return True if the word matches, false otherwise
     */
    private static boolean matchesWord(List<String> grid, int row, int col, int dRow, int dCol, String word) {
        int numRows = grid.size();
        int numCols = grid.get(0).length();
        for (int i = 0; i < word.length(); i++) {
            int r = row + i * dRow;
            int c = col + i * dCol;
            if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
                return false;
            }
            if (grid.get(r).charAt(c) != word.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}
