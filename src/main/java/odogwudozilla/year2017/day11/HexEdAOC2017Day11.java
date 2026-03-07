package odogwudozilla.year2017.day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Arrays;

/**
 * Hex Ed (Advent of Code 2017 Day 11)
 *
 * Crossing the bridge, you've barely reached the other side of the stream when a program comes up to you, clearly in distress. "It's my child process," she says, "he's gotten lost in an infinite grid!"
 *
 * Fortunately for her, you have plenty of experience with infinite grids.
 *
 * Unfortunately for you, it's a hex grid.
 *
 * The hexagons ("hexes") in this grid are aligned such that adjacent hexes can be found to the north, northeast, southeast, south, southwest, and northwest.
 *
 * Official puzzle URL: https://adventofcode.com/2017/day/11
 */
public class HexEdAOC2017Day11 {
    private static final String INPUT_PATH = "src/main/resources/2017/day11/day11_puzzle_data.txt";

    /**
     * Main entry point for the solution.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(INPUT_PATH));
        String[] steps = lines.getFirst().trim().split(",");
        System.out.println("solvePartOne - Fewest steps to reach child: " + solvePartOne(steps));
        System.out.println("solvePartTwo - Furthest distance during walk: " + solvePartTwo(steps));
    }

    /**
     * Solves Part 1: Find the fewest steps required to reach the child process.
     * @param steps Array of movement directions
     * @return Fewest number of steps to reach the destination
     */
    public static int solvePartOne(String[] steps) {
        int[] pos = walkHexPath(steps);
        return hexDistance(0, 0, pos[0], pos[1], pos[2]);
    }

    /**
     * Solves Part 2: Find the furthest distance from the start at any point during the walk.
     * @param steps Array of movement directions
     * @return Maximum distance from the start during the walk
     */
    public static int solvePartTwo(String[] steps) {
        int x = 0, y = 0, z = 0;
        int maxDist = 0;
        for (String step : steps) {
            int[] delta = directionToDelta(step);
            x += delta[0];
            y += delta[1];
            z += delta[2];
            int dist = hexDistance(0, 0, x, y, z);
            if (dist > maxDist) {
                maxDist = dist;
            }
        }
        return maxDist;
    }

    /**
     * Walks the hex path and returns the final cube coordinates.
     * @param steps Array of movement directions
     * @return int[] {x, y, z} final position
     */
    private static int[] walkHexPath(String[] steps) {
        int x = 0, y = 0, z = 0;
        for (String step : steps) {
            int[] delta = directionToDelta(step);
            x += delta[0];
            y += delta[1];
            z += delta[2];
        }
        return new int[]{x, y, z};
    }

    /**
     * Converts a direction string to a cube coordinate delta.
     * @param dir Direction (n, ne, se, s, sw, nw)
     * @return int[] {dx, dy, dz}
     */
    private static int[] directionToDelta(String dir) {
        return switch (dir) {
            case "n" -> new int[]{0, 1, -1};
            case "ne" -> new int[]{1, 0, -1};
            case "se" -> new int[]{1, -1, 0};
            case "s" -> new int[]{0, -1, 1};
            case "sw" -> new int[]{-1, 0, 1};
            case "nw" -> new int[]{-1, 1, 0};
            default -> throw new IllegalArgumentException("Unknown direction: " + dir);
        };
    }

    /**
     * Calculates the hex distance between two cube coordinates.
     * @param x1 Start x
     * @param y1 Start y
     * @param x2 End x
     * @param y2 End y
     * @param z2 End z
     * @return Distance in steps
     */
    private static int hexDistance(int x1, int y1, int x2, int y2, int z2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int dz = Math.abs(z2 - (-x1 - y1));
        return Math.max(dx, Math.max(dy, dz));
    }
}

