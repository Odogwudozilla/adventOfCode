package odogwudozilla.year2015.day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Advent of Code 2015 - Day 1: Not Quite Lisp
 * Santa is trying to deliver presents in a large apartment building, but he can't find the right floor.
 * He starts on the ground floor (floor 0) and follows instructions one character at a time.
 * An opening parenthesis '(' means go up one floor, and a closing parenthesis ')' means go down one floor.
 * <p>
 * Puzzle URL: https://adventofcode.com/2015/day/1
 */
public class NotQuiteLispAOC2015Day1 {

    private static final String RESOURCE_PATH = "src/main/resources/2015/day1/day1_puzzle_data.txt";
    private static final char UP = '(';
    private static final char DOWN = ')';
    private static final int STARTING_FLOOR = 0;

    public static void main(String[] args) {
        try {
            String instructions = readPuzzleInput();

            // Part 1: Find the final floor
            int finalFloor = solvePartOne(instructions);
            System.out.println("Part 1 - Final floor: " + finalFloor);

            // Part 2: Find the position of the first character that causes Santa to enter the basement
            int basementPosition = solvePartTwo(instructions);
            System.out.println("Part 2 - First basement position: " + basementPosition);

        } catch (IOException e) {
            System.err.println("Error reading puzzle input: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Read the puzzle input from the text file.
     * @return the puzzle input as a string
     */
    private static String readPuzzleInput() throws IOException {
        Path path = Paths.get(RESOURCE_PATH);
        return Files.readString(path).trim();
    }

    /**
     * Calculate the final floor Santa ends up on after following all instructions.
     * @param instructions the string of parentheses instructions
     * @return the final floor number
     */
    private static int solvePartOne(String instructions) {
        int floor = STARTING_FLOOR;

        for (char instruction : instructions.toCharArray()) {
            if (instruction == UP) {
                floor++;
            } else if (instruction == DOWN) {
                floor--;
            }
        }

        return floor;
    }

    /**
     * Find the position of the first character that causes Santa to enter the basement (floor -1).
     * Position counting starts at 1.
     * @param instructions the string of parentheses instructions
     * @return the position (1-indexed) of the character that first causes floor -1
     */
    private static int solvePartTwo(String instructions) {
        int floor = STARTING_FLOOR;
        int position = 0;

        for (char instruction : instructions.toCharArray()) {
            position++;

            if (instruction == UP) {
                floor++;
            } else if (instruction == DOWN) {
                floor--;
            }

            // Check if Santa has entered the basement for the first time
            if (floor == -1) {
                return position;
            }
        }

        // If Santa never enters the basement, return -1
        return -1;
    }
}
