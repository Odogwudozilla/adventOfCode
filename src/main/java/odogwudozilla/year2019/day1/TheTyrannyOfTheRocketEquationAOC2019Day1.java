package odogwudozilla.year2019.day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * The Tyranny of the Rocket Equation (Advent of Code 2019 Day 1)
 *
 * Santa has become stranded at the edge of the Solar System while delivering presents to other planets! To accurately calculate his position in space, safely align his warp drive, and return to Earth in time to save Christmas, he needs you to bring him measurements from fifty stars.
 *
 * Fuel required to launch a given module is based on its mass. Specifically, to find the fuel required for a module, take its mass, divide by three, round down, and subtract 2. The Fuel Counter-Upper needs to know the total fuel requirement. To find it, individually calculate the fuel needed for the mass of each module (your puzzle input), then add together all the fuel values.
 *
 * Official puzzle URL: https://adventofcode.com/2019/day/1
 */
public class TheTyrannyOfTheRocketEquationAOC2019Day1 {
    private static final String INPUT_PATH = "src/main/resources/2019/day1/day1_puzzle_data.txt";
    private static final int DIVISOR = 3; // Used for fuel calculation
    private static final int SUBTRACTOR = 2; // Used for fuel calculation

    /**
     * Main method to solve Part 1 of the puzzle.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        String methodName = "main";
        try {
            List<String> lines = Files.readAllLines(Paths.get(INPUT_PATH));
            int totalFuelPart1 = solvePartOne(lines);
            int totalFuelPart2 = solvePartTwo(lines);
            System.out.println("Part 1: Total fuel required = " + totalFuelPart1);
            System.out.println("Part 2: Total fuel required (including fuel for fuel) = " + totalFuelPart2);
            // main - Successfully calculated total fuel required for Part 1 and Part 2
            System.out.println(methodName + " - Successfully calculated total fuel required for Part 1 and Part 2");
        } catch (IOException e) {
            System.err.println(methodName + " - Error reading input file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println(methodName + " - Invalid number in input: " + e.getMessage());
        }
    }

    /**
     * Calculates the total fuel required for Part 1.
     * @param lines List of module masses
     * @return Total fuel required
     */
    private static int solvePartOne(List<String> lines) {
        int totalFuel = 0;
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            int mass = Integer.parseInt(line.trim());
            totalFuel += calculateFuel(mass);
        }
        return totalFuel;
    }

    /**
     * Calculates the total fuel required for Part 2 (including fuel for fuel).
     * @param lines List of module masses
     * @return Total fuel required
     */
    private static int solvePartTwo(List<String> lines) {
        int totalFuel = 0;
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            int mass = Integer.parseInt(line.trim());
            totalFuel += calculateFuelWithFuel(mass);
        }
        return totalFuel;
    }

    /**
     * Calculates the fuel required for a given module mass.
     * @param mass The mass of the module
     * @return The required fuel
     */
    private static int calculateFuel(int mass) {
        return Math.max(0, (mass / DIVISOR) - SUBTRACTOR);
    }

    /**
     * Calculates the fuel required for a given module mass, including the fuel for the fuel itself.
     * @param mass The mass of the module
     * @return The total required fuel including fuel for the fuel
     */
    private static int calculateFuelWithFuel(int mass) {
        int totalFuel = 0;
        int additionalFuel = calculateFuel(mass);
        while (additionalFuel > 0) {
            totalFuel += additionalFuel;
            additionalFuel = calculateFuel(additionalFuel);
        }
        return totalFuel;
    }
}
