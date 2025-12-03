package odogwudozilla.year2025.day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Advent of Code 2025 - Day 1: Secret Entrance
 * <p>
 * The Elves need help opening a safe to get the password for the North Pole secret entrance.
 * The safe has a dial with numbers 0-99, and rotations are specified as L (left/lower) or R (right/higher).
 * The dial starts at 50, and wraps around (0 to 99 and 99 to 0).
 * The password is the number of times the dial points at 0 after any rotation in the sequence.
 * <p>
 * URL: https://adventofcode.com/2025/day/1
 */
public class SecretEntranceAOC2025Day1 {

    private static final int DIAL_SIZE = 100;
    private static final int STARTING_POSITION = 50;
    private static final int TARGET_POSITION = 0;

    public static void main(String[] args) {
        try {
            List<String> rotations = readInputFile();
            int passwordPart1 = calculatePassword(rotations);
            System.out.println("Part 1 - The password is: " + passwordPart1);

            int passwordPart2 = calculatePasswordWithClicks(rotations);
            System.out.println("Part 2 - The password (method 0x434C49434B) is: " + passwordPart2);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Reads the puzzle input file containing rotation instructions.
     * @return list of rotation instructions
     * @throws IOException if the file cannot be read
     */
    private static List<String> readInputFile() throws IOException {
        String filePath = "src/main/resources/2025/day1/day1_puzzle_data.txt";
        return Files.readAllLines(Paths.get(filePath));
    }

    /**
     * Calculates the password by counting how many times the dial points at 0 after rotations.
     * @param rotations list of rotation instructions (e.g., "L68", "R48")
     * @return the number of times the dial points at 0
     */
    private static int calculatePassword(List<String> rotations) {
        int currentPosition = STARTING_POSITION;
        int zeroCount = 0;

        for (String rotation : rotations) {
            if (rotation.trim().isEmpty()) {
                continue;
            }

            char direction = rotation.charAt(0);
            int distance = Integer.parseInt(rotation.substring(1));

            currentPosition = performRotation(currentPosition, direction, distance);

            if (TARGET_POSITION == currentPosition) {
                zeroCount++;
            }
        }

        return zeroCount;
    }

    /**
     * Performs a rotation on the dial and returns the new position.
     * @param currentPosition the current position of the dial
     * @param direction 'L' for left (toward lower numbers) or 'R' for right (toward higher numbers)
     * @param distance the number of clicks to rotate
     * @return the new position after rotation
     */
    private static int performRotation(int currentPosition, char direction, int distance) {
        int newPosition;

        if ('L' == direction) {
            // Left rotation: subtract distance and handle wrap-around
            newPosition = (currentPosition - distance) % DIAL_SIZE;
            if (newPosition < 0) {
                newPosition += DIAL_SIZE;
            }
        } else {
            // Right rotation: add distance and handle wrap-around
            newPosition = (currentPosition + distance) % DIAL_SIZE;
        }

        return newPosition;
    }

    /**
     * Calculates the password by counting how many times the dial points at 0 during any click.
     * This includes both passing through 0 during rotations and ending at 0.
     * @param rotations list of rotation instructions (e.g., "L68", "R48")
     * @return the total number of times the dial points at 0
     */
    private static int calculatePasswordWithClicks(List<String> rotations) {
        int currentPosition = STARTING_POSITION;
        int zeroCount = 0;

        for (String rotation : rotations) {
            if (rotation.trim().isEmpty()) {
                continue;
            }

            char direction = rotation.charAt(0);
            int distance = Integer.parseInt(rotation.substring(1));

            zeroCount += countZerosDuringRotation(currentPosition, direction, distance);
            currentPosition = performRotation(currentPosition, direction, distance);
        }

        return zeroCount;
    }

    /**
     * Counts how many times the dial points at 0 during a rotation, including the final position.
     * @param startPosition the starting position of the dial
     * @param direction 'L' for left or 'R' for right
     * @param distance the number of clicks to rotate
     * @return the number of times the dial points at 0 during this rotation
     */
    private static int countZerosDuringRotation(int startPosition, char direction, int distance) {
        int count = 0;

        if ('L' == direction) {
            // Left rotation: moving towards lower numbers
            for (int i = 1; i <= distance; i++) {
                int position = (startPosition - i) % DIAL_SIZE;
                if (position < 0) {
                    position += DIAL_SIZE;
                }
                if (TARGET_POSITION == position) {
                    count++;
                }
            }
        } else {
            // Right rotation: moving towards higher numbers
            for (int i = 1; i <= distance; i++) {
                int position = (startPosition + i) % DIAL_SIZE;
                if (TARGET_POSITION == position) {
                    count++;
                }
            }
        }

        return count;
    }
}

