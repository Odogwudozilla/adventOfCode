package odogwudozilla.year2021.day2;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

/**
 * Dive! (Advent of Code 2021 Day 2)
 *
 * Calculate the horizontal position and depth after following a series of movement commands. Multiply the final horizontal position by the final depth for the answer.
 * Official puzzle link: https://adventofcode.com/2021/day/2
 */
public class DiveAOC2021Day2 {
    private static final String INPUT_PATH = "src/main/resources/2021/day2/day2_puzzle_data.txt";
    private static final Logger LOGGER = Logger.getLogger(DiveAOC2021Day2.class.getName());

    /**
     * Entry point for solving Advent of Code 2021 Day 2.
     * Calls partOne and partTwo methods and prints/logs their results.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        LOGGER.info("main - Starting Dive! position calculation");
        try {
            List<String> commands = Files.readAllLines(Paths.get(INPUT_PATH));
            int resultPart1 = partOne(commands);
            LOGGER.info("main - Part 1 result (horizontal * depth): " + resultPart1);
            System.out.println("Part 1 result (horizontal * depth): " + resultPart1);

            int resultPart2 = partTwo(commands);
            LOGGER.info("main - Part 2 result (horizontal * depth): " + resultPart2);
            System.out.println("Part 2 result (horizontal * depth): " + resultPart2);
        } catch (Exception e) {
            LOGGER.severe("main - Error reading input or calculating position: " + e.getMessage());
            for (StackTraceElement ste : e.getStackTrace()) {
                LOGGER.severe("main - " + ste.toString());
            }
        }
    }

    /**
     * Solves Part 1: Calculate horizontal position and depth, then multiply.
     *
     * @param commands List of movement commands
     * @return Product of final horizontal position and depth
     */
    private static int partOne(List<String> commands) {
        int horizontal = 0;
        int depth = 0;
        for (String command : commands) {
            String[] parts = command.split(" ");
            String action = parts[0];
            int value = Integer.parseInt(parts[1]);
            switch (action) {
                case "forward":
                    horizontal += value;
                    break;
                case "down":
                    depth += value;
                    break;
                case "up":
                    depth -= value;
                    break;
                default:
                    LOGGER.warning("partOne - Unknown command: " + command);
            }
        }
        return horizontal * depth;
    }

    /**
     * Solves Part 2: Implements aim logic for movement commands.
     *
     * @param commands List of movement commands
     * @return Product of final horizontal position and depth using aim logic
     */
    private static int partTwo(List<String> commands) {
        int aim = 0;
        int horizontal = 0;
        int depth = 0;
        for (String command : commands) {
            String[] parts = command.split(" ");
            String action = parts[0];
            int value = Integer.parseInt(parts[1]);
            switch (action) {
                case "forward":
                    horizontal += value;
                    depth += aim * value;
                    break;
                case "down":
                    aim += value;
                    break;
                case "up":
                    aim -= value;
                    break;
                default:
                    LOGGER.warning("partTwo - Unknown command: " + command);
            }
        }
        return horizontal * depth;
    }
}
