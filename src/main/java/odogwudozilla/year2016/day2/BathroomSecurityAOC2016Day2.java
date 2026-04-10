package odogwudozilla.year2016.day2;

import org.jetbrains.annotations.NotNull;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;

/**
 * --- Day 2: Bathroom Security ---
 * You arrive at Easter Bunny Headquarters under cover of darkness. However, you left in such a rush that you forgot to use the bathroom! Fancy office buildings like this one usually have keypad locks on their bathrooms, so you search the front desk for the code.
 *
 * The document explains that each button to be pressed can be found by starting on the previous button and moving to adjacent buttons on the keypad: U moves up, D moves down, L moves left, and R moves right. Each line of instructions corresponds to one button, starting at the previous button (or, for the first line, the "5" button); press whatever button you're on at the end of each line. If a move doesn't lead to a button, ignore it.
 *
 * Your puzzle input is the instructions from the document you found at the front desk. What is the bathroom code?
 *
 *
 * --- Part Two ---
 * The keypad is now shaped as follows:
 *     1
 *   2 3 4
 * 5 6 7 8 9
 *   A B C
 *     D
 *
 * Official puzzle URL: https://adventofcode.com/2016/day/2
 */
public class BathroomSecurityAOC2016Day2 {
    private static final String INPUT_PATH = "src/main/resources/2016/day2/day2_puzzle_data.txt";

    public static void main(String[] args) throws IOException {
        List<String> instructions = Files.readAllLines(Paths.get(INPUT_PATH));
        String code1 = solvePartOne(instructions);
        System.out.println("Part 1 - Bathroom code: " + code1);
        String code2 = solvePartTwo(instructions);
        System.out.println("Part 2 - Bathroom code: " + code2);
    }
    /**
     * Solves Part 2 of the puzzle: computes the bathroom code using the diamond-shaped keypad.
     * @param instructions the list of movement instruction strings
     * @return the bathroom code as a String
     */
    @NotNull
    public static String solvePartTwo(@NotNull List<String> instructions) {
        // Keypad layout (row, col):
        //    0 1 2 3 4
        // 0    1
        // 1  2 3 4
        // 2 5 6 7 8 9
        // 3  A B C
        // 4    D
        char[][] keypad = {
            {' ', ' ', '1', ' ', ' '},
            {' ', '2', '3', '4', ' '},
            {'5', '6', '7', '8', '9'},
            {' ', 'A', 'B', 'C', ' '},
            {' ', ' ', 'D', ' ', ' '}
        };
        int row = 2, col = 0; // Start at '5'
        StringBuilder code = new StringBuilder();
        for (String line : instructions) {
            for (char move : line.toCharArray()) {
                int nextRow = row, nextCol = col;
                switch (move) {
                    case 'U': nextRow = row - 1; break;
                    case 'D': nextRow = row + 1; break;
                    case 'L': nextCol = col - 1; break;
                    case 'R': nextCol = col + 1; break;
                }
                if (nextRow >= 0 && nextRow < 5 && nextCol >= 0 && nextCol < 5 && keypad[nextRow][nextCol] != ' ') {
                    row = nextRow;
                    col = nextCol;
                }
            }
            code.append(keypad[row][col]);
        }
        return code.toString();
    }

    /**
     * Solves Part 1 of the puzzle: computes the bathroom code using the standard 3x3 keypad.
     * @param instructions the list of movement instruction strings
     * @return the bathroom code as a String
     */
    @NotNull
    public static String solvePartOne(@NotNull List<String> instructions) {
        int[][] keypad = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        int row = 1, col = 1; // Start at '5'
        StringBuilder code = new StringBuilder();
        for (String line : instructions) {
            for (char move : line.toCharArray()) {
                switch (move) {
                    case 'U': if (row > 0) row--; break;
                    case 'D': if (row < 2) row++; break;
                    case 'L': if (col > 0) col--; break;
                    case 'R': if (col < 2) col++; break;
                }
            }
            code.append(keypad[row][col]);
        }
        return code.toString();
    }
}


