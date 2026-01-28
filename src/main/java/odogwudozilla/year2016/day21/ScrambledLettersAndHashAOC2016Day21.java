package odogwudozilla.year2016.day21;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Advent of Code 2016 - Day 21: Scrambled Letters and Hash
 *
 * The computer system uses a weird scrambling function to store its passwords.
 * This solution implements the scrambler that applies a series of operations:
 * - swap position/letter
 * - rotate left/right
 * - rotate based on position of letter
 * - reverse positions
 * - move position
 *
 * Part 1: What is the result of scrambling "abcdefgh"?
 * Part 2: What is the unscrambled password that produces the scrambled result?
 *
 * Puzzle URL: https://adventofcode.com/2016/day/21
 */
public class ScrambledLettersAndHashAOC2016Day21 {

    private static final String INPUT_FILE = "src/main/resources/2016/day21/day21_puzzle_data.txt";
    private static final String INITIAL_PASSWORD = "abcdefgh";
    private static final String SCRAMBLED_TARGET = "fbgdceah";

    public static void main(String[] args) {
        try {
            List<String> operations = Files.readAllLines(Paths.get(INPUT_FILE));

            // Part 1: Scramble the password
            String scrambledPassword = solvePartOne(operations, INITIAL_PASSWORD);
            System.out.println("Part 1 - Scrambled password: " + scrambledPassword);

            // Part 2: Unscramble to find original password that produces "fbgdceah"
            String unscrambledPassword = solvePartTwo(operations, SCRAMBLED_TARGET);
            System.out.println("Part 2 - Unscrambled password: " + unscrambledPassword);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1 by applying all scrambling operations to the initial password.
     * @param operations the list of scrambling operations
     * @param password the initial password to scramble
     * @return the scrambled password
     */
    public static String solvePartOne(List<String> operations, String password) {
        char[] result = password.toCharArray();

        for (String operation : operations) {
            result = applyOperation(result, operation);
        }

        return new String(result);
    }

    /**
     * Solves Part 2 by reversing all scrambling operations.
     * @param operations the list of scrambling operations
     * @param scrambled the scrambled password
     * @return the original unscrambled password
     */
    public static String solvePartTwo(List<String> operations, String scrambled) {
        char[] result = scrambled.toCharArray();

        // Apply operations in reverse order with inverse operations
        for (int i = operations.size() - 1; i >= 0; i--) {
            result = applyReverseOperation(result, operations.get(i));
        }

        return new String(result);
    }

    /**
     * Applies a single scrambling operation to the password.
     * @param password the current password state
     * @param operation the operation to apply
     * @return the password after applying the operation
     */
    private static char[] applyOperation(char[] password, String operation) {
        String[] parts = operation.split(" ");

        if (operation.startsWith("swap position")) {
            int x = Integer.parseInt(parts[2]);
            int y = Integer.parseInt(parts[5]);
            return swapPosition(password, x, y);

        } else if (operation.startsWith("swap letter")) {
            char x = parts[2].charAt(0);
            char y = parts[5].charAt(0);
            return swapLetter(password, x, y);

        } else if (operation.startsWith("rotate left")) {
            int steps = Integer.parseInt(parts[2]);
            return rotateLeft(password, steps);

        } else if (operation.startsWith("rotate right")) {
            int steps = Integer.parseInt(parts[2]);
            return rotateRight(password, steps);

        } else if (operation.startsWith("rotate based")) {
            char letter = parts[6].charAt(0);
            return rotateBasedOnPosition(password, letter);

        } else if (operation.startsWith("reverse")) {
            int x = Integer.parseInt(parts[2]);
            int y = Integer.parseInt(parts[4]);
            return reversePositions(password, x, y);

        } else if (operation.startsWith("move")) {
            int x = Integer.parseInt(parts[2]);
            int y = Integer.parseInt(parts[5]);
            return movePosition(password, x, y);
        }

        return password;
    }

    /**
     * Applies the reverse of a scrambling operation.
     * @param password the current password state
     * @param operation the original operation to reverse
     * @return the password after applying the reverse operation
     */
    private static char[] applyReverseOperation(char[] password, String operation) {
        String[] parts = operation.split(" ");

        if (operation.startsWith("swap position")) {
            // Swap is its own inverse
            int x = Integer.parseInt(parts[2]);
            int y = Integer.parseInt(parts[5]);
            return swapPosition(password, x, y);

        } else if (operation.startsWith("swap letter")) {
            // Swap is its own inverse
            char x = parts[2].charAt(0);
            char y = parts[5].charAt(0);
            return swapLetter(password, x, y);

        } else if (operation.startsWith("rotate left")) {
            // Reverse of rotate left is rotate right
            int steps = Integer.parseInt(parts[2]);
            return rotateRight(password, steps);

        } else if (operation.startsWith("rotate right")) {
            // Reverse of rotate right is rotate left
            int steps = Integer.parseInt(parts[2]);
            return rotateLeft(password, steps);

        } else if (operation.startsWith("rotate based")) {
            // This requires special handling - need to find which left rotation undoes it
            char letter = parts[6].charAt(0);
            return reverseRotateBasedOnPosition(password, letter);

        } else if (operation.startsWith("reverse")) {
            // Reverse is its own inverse
            int x = Integer.parseInt(parts[2]);
            int y = Integer.parseInt(parts[4]);
            return reversePositions(password, x, y);

        } else if (operation.startsWith("move")) {
            // Reverse of move x to y is move y to x
            int x = Integer.parseInt(parts[2]);
            int y = Integer.parseInt(parts[5]);
            return movePosition(password, y, x);
        }

        return password;
    }

    private static char[] swapPosition(char[] password, int x, int y) {
        char[] result = password.clone();
        char temp = result[x];
        result[x] = result[y];
        result[y] = temp;
        return result;
    }

    private static char[] swapLetter(char[] password, char x, char y) {
        char[] result = password.clone();
        for (int i = 0; i < result.length; i++) {
            if (result[i] == x) {
                result[i] = y;
            } else if (result[i] == y) {
                result[i] = x;
            }
        }
        return result;
    }

    private static char[] rotateLeft(char[] password, int steps) {
        int len = password.length;
        steps = steps % len;
        char[] result = new char[len];
        for (int i = 0; i < len; i++) {
            result[i] = password[(i + steps) % len];
        }
        return result;
    }

    private static char[] rotateRight(char[] password, int steps) {
        int len = password.length;
        steps = steps % len;
        char[] result = new char[len];
        for (int i = 0; i < len; i++) {
            result[i] = password[(i - steps + len) % len];
        }
        return result;
    }

    private static char[] rotateBasedOnPosition(char[] password, char letter) {
        int index = -1;
        for (int i = 0; i < password.length; i++) {
            if (password[i] == letter) {
                index = i;
                break;
            }
        }

        int rotations = 1 + index;
        if (index >= 4) {
            rotations++;
        }

        return rotateRight(password, rotations);
    }

    private static char[] reverseRotateBasedOnPosition(char[] password, char letter) {
        // Find current position of the letter
        int currentIndex = -1;
        for (int i = 0; i < password.length; i++) {
            if (password[i] == letter) {
                currentIndex = i;
                break;
            }
        }

        // Try each possible left rotation until we find one that would produce current state
        for (int leftRotations = 0; leftRotations < password.length; leftRotations++) {
            char[] test = rotateLeft(password, leftRotations);

            // Find where letter would be after rotating left
            int testIndex = -1;
            for (int i = 0; i < test.length; i++) {
                if (test[i] == letter) {
                    testIndex = i;
                    break;
                }
            }

            // Calculate how many right rotations would be applied based on this position
            int rightRotations = 1 + testIndex;
            if (testIndex >= 4) {
                rightRotations++;
            }

            // If rotating right by that amount gives us back the original, we found it
            char[] checkResult = rotateRight(test, rightRotations);
            boolean matches = true;
            for (int i = 0; i < password.length; i++) {
                if (checkResult[i] != password[i]) {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                return test;
            }
        }

        return password;
    }

    private static char[] reversePositions(char[] password, int x, int y) {
        char[] result = password.clone();
        while (x < y) {
            char temp = result[x];
            result[x] = result[y];
            result[y] = temp;
            x++;
            y--;
        }
        return result;
    }

    private static char[] movePosition(char[] password, int x, int y) {
        char[] result = new char[password.length];
        char letterToMove = password[x];

        int resultIndex = 0;
        for (int i = 0; i < password.length; i++) {
            if (i == x) {
                continue; // Skip the letter to move
            }
            if (resultIndex == y) {
                result[resultIndex++] = letterToMove;
            }
            result[resultIndex++] = password[i];
        }

        // If we haven't placed the letter yet, it goes at the end
        if (resultIndex == y) {
            result[resultIndex] = letterToMove;
        }

        return result;
    }
}
