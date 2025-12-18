package odogwudozilla.year2016.day1;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 * No Time for a Taxicab (Advent of Code 2016 Day 1)
 *
 * Santa's sleigh uses a high-precision clock regulated by stars, which have been stolen by the Easter Bunny. To save Christmas, you must follow a sequence of instructions to determine how far you are from Easter Bunny HQ, starting at the origin and facing North. Each instruction is a turn (L or R) followed by a number of blocks to walk. The shortest path is the Manhattan distance from the start to the destination.
 *
 * Official puzzle link: https://adventofcode.com/2016/day/1
 *
 * @param args Command line arguments (not used)
 * @return void
 */
public class NoTimeForATaxicabAOC2016Day1 {
    private static final String INPUT_PATH = "src/main/resources/2016/day1/day1_puzzle_data.txt";
    private static final int NORTH = 0;
    private static final int EAST = 1;
    private static final int SOUTH = 2;
    private static final int WEST = 3;

    /**
     * Main method to solve Part 1 and Part 2 of the puzzle.
     * Reads instructions and computes Manhattan distance to destination and first location visited twice.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // main - Read input and solve Part 1 and Part 2
        try {
            List<String> lines = Files.readAllLines(Paths.get(INPUT_PATH));
            String[] instructions = lines.get(0).split(", ");
            int x = 0;
            int y = 0;
            int direction = NORTH;
            Set<String> visited = new HashSet<>();
            visited.add("0,0");
            boolean foundTwice = false;
            int part2Distance = -1;
            outer:
            for (String instr : instructions) {
                char turn = instr.charAt(0);
                int blocks = Integer.parseInt(instr.substring(1));
                direction = turnDirection(direction, turn);
                for (int i = 0; i < blocks; i++) {
                    switch (direction) {
                        case NORTH: y++; break;
                        case EAST:  x++; break;
                        case SOUTH: y--; break;
                        case WEST:  x--; break;
                        default: break;
                    }
                    String coord = x + "," + y;
                    if (!foundTwice) {
                        if (visited.contains(coord)) {
                            part2Distance = Math.abs(x) + Math.abs(y);
                            foundTwice = true;
                            // main - First location visited twice found
                        } else {
                            visited.add(coord);
                        }
                    }
                }
            }
            int distance = Math.abs(x) + Math.abs(y);
            System.out.println("main - Easter Bunny HQ is " + distance + " blocks away.");
            if (foundTwice) {
                System.out.println("main - First location visited twice is " + part2Distance + " blocks away.");
            } else {
                System.out.println("main - No location visited twice.");
            }
        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Determines new direction after turning left or right.
     * @param currentDir Current direction (NORTH, EAST, SOUTH, WEST)
     * @param turn 'L' for left, 'R' for right
     * @return New direction constant
     */
    private static int turnDirection(int currentDir, char turn) {
        if ('L' == turn) {
            return (currentDir + 3) % 4;
        } else if ('R' == turn) {
            return (currentDir + 1) % 4;
        } else {
            throw new IllegalArgumentException("turnDirection - Invalid turn: " + turn);
        }
    }
}
