package odogwudozilla.year2015.day6;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2015 - Day 6: Probably a Fire Hazard
 * <p>
 * Puzzle URL: https://adventofcode.com/2015/day/6
 */
public final class ProbablyAFireHazardAOC2015Day6 {

    private static final String INPUT_FILE = "/2015/day6/day6_puzzle_data.txt";
    private static final int GRID_SIZE = 1000;

    /**
     * Entry point. Reads puzzle input and prints solutions for Part 1 and Part 2.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        List<String> input = readInput();
        System.out.println("Part 1: " + solvePartOne(input));
        System.out.println("Part 2: " + solvePartTwo(input));
    }

    /**
     * Solves Part 1 of the puzzle.
     * @param input list of input lines
     * @return the Part 1 answer
     */
    public static String solvePartOne(List<String> input) {
        boolean[][] grid = new boolean[GRID_SIZE][GRID_SIZE];
        for (String line : input) {
            String op;
            int x1, y1, x2, y2;
            if (line.startsWith("turn on")) {
                op = "on";
                line = line.substring(8);
            } else if (line.startsWith("turn off")) {
                op = "off";
                line = line.substring(9);
            } else if (line.startsWith("toggle")) {
                op = "toggle";
                line = line.substring(7);
            } else {
                continue;
            }
            String[] parts = line.split(" through ");
            String[] start = parts[0].split(",");
            String[] end = parts[1].split(",");
            x1 = Integer.parseInt(start[0].trim());
            y1 = Integer.parseInt(start[1].trim());
            x2 = Integer.parseInt(end[0].trim());
            y2 = Integer.parseInt(end[1].trim());
            for (int x = x1; x <= x2; x++) {
                for (int y = y1; y <= y2; y++) {
                    if (op.equals("on")) {
                        grid[x][y] = true;
                    } else if (op.equals("off")) {
                        grid[x][y] = false;
                    } else if (op.equals("toggle")) {
                        grid[x][y] = !grid[x][y];
                    }
                }
            }
        }
        int count = 0;
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                if (grid[x][y]) count++;
            }
        }
        return String.valueOf(count);
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    public static String solvePartTwo(List<String> input) {
        int[][] grid = new int[GRID_SIZE][GRID_SIZE];
        for (String line : input) {
            String op;
            int x1, y1, x2, y2;
            if (line.startsWith("turn on")) {
                op = "on";
                line = line.substring(8);
            } else if (line.startsWith("turn off")) {
                op = "off";
                line = line.substring(9);
            } else if (line.startsWith("toggle")) {
                op = "toggle";
                line = line.substring(7);
            } else {
                continue;
            }
            String[] parts = line.split(" through ");
            String[] start = parts[0].split(",");
            String[] end = parts[1].split(",");
            x1 = Integer.parseInt(start[0].trim());
            y1 = Integer.parseInt(start[1].trim());
            x2 = Integer.parseInt(end[0].trim());
            y2 = Integer.parseInt(end[1].trim());
            for (int x = x1; x <= x2; x++) {
                for (int y = y1; y <= y2; y++) {
                    if (op.equals("on")) {
                        grid[x][y] += 1;
                    } else if (op.equals("off")) {
                        grid[x][y] = Math.max(0, grid[x][y] - 1);
                    } else if (op.equals("toggle")) {
                        grid[x][y] += 2;
                    }
                }
            }
        }
        long total = 0;
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                total += grid[x][y];
            }
        }
        return String.valueOf(total);
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = ProbablyAFireHazardAOC2015Day6.class.getResourceAsStream(INPUT_FILE)) {
            if (stream == null) {
                throw new IllegalStateException("Input file not found: " + INPUT_FILE);
            }
            Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8);
            List<String> lines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            return lines;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read input file", e);
        }
    }
}
