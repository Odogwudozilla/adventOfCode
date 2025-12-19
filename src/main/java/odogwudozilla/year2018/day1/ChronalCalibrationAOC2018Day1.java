package odogwudozilla.year2018.day1;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 * Chronal Calibration (Advent of Code 2018 Day 1)
 *
 * Santa's device must be calibrated by applying a sequence of frequency changes. Each line in the input is a signed
 * integer representing a change. Starting from zero, sum all changes to get the resulting frequency.
 *
 * Official puzzle link: https://adventofcode.com/2018/day/1
 *
 * @param args Command line arguments (not used)
 * @return void
 */
public class ChronalCalibrationAOC2018Day1 {
    private static final String INPUT_PATH = "src/main/resources/2018/day1/day1_puzzle_data.txt";

    /**
     * Main method to solve Part 1 and Part 2 of the puzzle.
     * Reads frequency changes and computes the resulting frequency and first frequency reached twice.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // main - Read input and solve Part 1 and Part 2
        try {
            List<String> lines = Files.readAllLines(Paths.get(INPUT_PATH));
            int frequency = 0;
            for (String line : lines) {
                frequency += Integer.parseInt(line.trim());
            }
            System.out.println("main - Resulting frequency after all changes: " + frequency);

            // Part 2: Find the first frequency reached twice
            Set<Integer> seen = new HashSet<>();
            seen.add(0);
            frequency = 0;
            boolean found = false;
            while (!found) {
                for (String line : lines) {
                    frequency += Integer.parseInt(line.trim());
                    if (seen.contains(frequency)) {
                        System.out.println("main - First frequency reached twice: " + frequency);
                        found = true;
                        break;
                    }
                    seen.add(frequency);
                }
            }
        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }
}
