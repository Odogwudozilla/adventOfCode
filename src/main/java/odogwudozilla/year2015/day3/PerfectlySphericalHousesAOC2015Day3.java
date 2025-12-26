package odogwudozilla.year2015.day3;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Santa delivers presents to houses on an infinite two-dimensional grid, moving according to instructions.
 * The goal is to determine how many houses receive at least one present.
 * Official puzzle link: https://adventofcode.com/2015/day/3
 */
public class PerfectlySphericalHousesAOC2015Day3 {
    private static final String INPUT_PATH = "src/main/resources/2015/day3/day3_puzzle_data.txt";

    /**
     * Entry point for the solution.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        try {
            String input = Files.readAllLines(Paths.get(INPUT_PATH)).get(0).trim();
            System.out.println("solvePartOne - Houses with at least one present: " + solvePartOne(input));
            System.out.println("solvePartTwo - Houses with at least one present (with Robo-Santa): " + solvePartTwo(input));
        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1: Santa alone delivers presents following the instructions.
     *
     * @param directions The movement instructions
     * @return Number of unique houses that receive at least one present
     */
    public static int solvePartOne(String directions) {
        Set<String> visited = new HashSet<>();
        int x = 0, y = 0;
        visited.add(x + "," + y);
        for (char dir : directions.toCharArray()) {
            switch (dir) {
                case '^': y++; break;
                case 'v': y--; break;
                case '>': x++; break;
                case '<': x--; break;
                default: break; // Ignore invalid characters
            }
            visited.add(x + "," + y);
        }
        return visited.size();
    }

    /**
     * Solves Part 2: Santa and Robo-Santa alternate moves, delivering presents together.
     *
     * @param directions The movement instructions
     * @return Number of unique houses that receive at least one present
     */
    public static int solvePartTwo(String directions) {
        Set<String> visited = new HashSet<>();
        int santaX = 0, santaY = 0;
        int roboX = 0, roboY = 0;
        visited.add("0,0");
        for (int i = 0; i < directions.length(); i++) {
            char dir = directions.charAt(i);
            if (i % 2 == 0) {
                switch (dir) {
                    case '^': santaY++; break;
                    case 'v': santaY--; break;
                    case '>': santaX++; break;
                    case '<': santaX--; break;
                    default: break;
                }
                visited.add(santaX + "," + santaY);
            } else {
                switch (dir) {
                    case '^': roboY++; break;
                    case 'v': roboY--; break;
                    case '>': roboX++; break;
                    case '<': roboX--; break;
                    default: break;
                }
                visited.add(roboX + "," + roboY);
            }
        }
        return visited.size();
    }
}

