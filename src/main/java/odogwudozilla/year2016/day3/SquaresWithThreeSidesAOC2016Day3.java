package odogwudozilla.year2016.day3;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;

/**
 * Day 3: Squares With Three Sides
 * Now that you can think clearly, you move deeper into the labyrinth of hallways and office furniture that makes up this part of Easter Bunny HQ. This must be a graphic design department; the walls are covered in specifications for triangles.
 *
 * Or are they?
 *
 * The design document gives the side lengths of each triangle it describes, but... 5 10 25? Some of these aren't triangles. You can't help but mark the impossible ones.
 *
 * In a valid triangle, the sum of any two sides must be larger than the remaining side. For example, the "triangle" given above is impossible, because 5 + 10 is not larger than 25.
 *
 * In your puzzle input, how many of the listed triangles are possible?
 *
 * Official puzzle URL: https://adventofcode.com/2016/day/3
 */
public class SquaresWithThreeSidesAOC2016Day3 {
    private static final String INPUT_PATH = "src/main/resources/2016/day3/day3_puzzle_data.txt";

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(INPUT_PATH));
            int possibleTriangles = solvePartOne(lines);
            System.out.println("Part 1: " + possibleTriangles);
            int possibleTrianglesCol = solvePartTwo(lines);
            System.out.println("Part 2: " + possibleTrianglesCol);
        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1: Counts the number of possible triangles.
     * @param lines the input lines
     * @return the count of valid triangles
     */
    public static int solvePartOne(List<String> lines) {
        int count = 0;
        for (String line : lines) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length != 3) continue;
            int a = Integer.parseInt(parts[0]);
            int b = Integer.parseInt(parts[1]);
            int c = Integer.parseInt(parts[2]);
            if (isValidTriangle(a, b, c)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Solves Part 2: Counts the number of possible triangles by columns.
     * @param lines the input lines
     * @return the count of valid triangles (column-wise)
     */
    public static int solvePartTwo(List<String> lines) {
        int count = 0;
        for (int i = 0; i + 2 < lines.size(); i += 3) {
            int[][] group = new int[3][3];
            for (int j = 0; j < 3; j++) {
                String[] parts = lines.get(i + j).trim().split("\\s+");
                for (int k = 0; k < 3; k++) {
                    group[j][k] = Integer.parseInt(parts[k]);
                }
            }
            for (int col = 0; col < 3; col++) {
                int a = group[0][col];
                int b = group[1][col];
                int c = group[2][col];
                if (isValidTriangle(a, b, c)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Checks if three sides can form a valid triangle.
     * @param a first side
     * @param b second side
     * @param c third side
     * @return true if valid triangle, false otherwise
     */
    private static boolean isValidTriangle(int a, int b, int c) {
        return a + b > c && a + c > b && b + c > a;
    }
}
