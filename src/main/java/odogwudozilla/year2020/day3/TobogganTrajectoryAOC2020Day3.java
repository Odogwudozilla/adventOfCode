/**
 * Day 3: Toboggan Trajectory (Advent of Code 2020)
 * With the toboggan login problems resolved, you set off toward the airport. While travel by toboggan might be easy, it's certainly not safe: there's very minimal steering and the area is covered in trees. You'll need to see which angles will take you near the fewest trees.
 *
 * Official puzzle URL: https://adventofcode.com/2020/day/3
 */
package odogwudozilla.year2020.day3;

import org.jetbrains.annotations.NotNull;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

public class TobogganTrajectoryAOC2020Day3 {
    private static final String INPUT_PATH = "src/main/resources/2020/day3/day3_puzzle_data.txt";

    public static void main(String[] args) {
        try {
            List<String> map = Files.readAllLines(Paths.get(INPUT_PATH));
            int partOneResult = solvePartOne(map);
            System.out.println("Part 1: Trees encountered = " + partOneResult);
            long partTwoResult = solvePartTwo(map);
            System.out.println("Part 2: Product of trees encountered on all slopes = " + partTwoResult);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1 of the Toboggan Trajectory puzzle.
     * @param map the map of the area as a list of strings
     * @return the number of trees encountered
     */
    public static int solvePartOne(@NotNull List<String> map) {
        final int RIGHT = 3;
        final int DOWN = 1;
        int width = map.get(0).length();
        int height = map.size();
        int trees = 0;
        int x = 0;
        for (int y = 0; y < height; y += DOWN) {
            if (map.get(y).charAt(x % width) == '#') {
                trees++;
            }
            x += RIGHT;
        }
        return trees;
    }

    /**
     * Solves Part 2 of the Toboggan Trajectory puzzle.
     * @param map the map of the area as a list of strings
     * @return the product of trees encountered on all specified slopes
     */
    public static long solvePartTwo(@NotNull List<String> map) {
        int[][] slopes = {
            {1, 1},
            {3, 1},
            {5, 1},
            {7, 1},
            {1, 2}
        };
        long product = 1;
        for (int[] slope : slopes) {
            product *= countTrees(map, slope[0], slope[1]);
        }
        return product;
    }

    /**
     * Counts the number of trees encountered for a given slope.
     * @param map the map of the area as a list of strings
     * @param right number of steps right
     * @param down number of steps down
     * @return the number of trees encountered
     */
    private static int countTrees(@NotNull List<String> map, int right, int down) {
        int width = map.get(0).length();
        int height = map.size();
        int trees = 0;
        int x = 0;
        for (int y = 0; y < height; y += down) {
            if (map.get(y).charAt(x % width) == '#') {
                trees++;
            }
            x += right;
        }
        return trees;
    }
}
