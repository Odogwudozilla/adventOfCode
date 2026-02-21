package odogwudozilla.year2016.day19;

/**
 * An Elephant Named Joseph (Advent of Code 2016 Day 19)
 *
 * Summary: The Elves sit in a circle, each with a present. Starting with the first Elf, they take turns stealing all
 * presents from the Elf to their left. Elves with no presents are removed from the circle. The puzzle asks: with the
 * given number of Elves, which Elf gets all the presents?
 * Official puzzle URL: https://adventofcode.com/2016/day/19
 *
 */
public class ElephantNamedJosephAOC2016Day19 {
    public static void main(String[] args) throws Exception {
        // Read input file
        java.nio.file.Path inputPath = java.nio.file.Paths.get("src/main/resources/2016/day19/day19_puzzle_data.txt");
        int numElves = Integer.parseInt(java.nio.file.Files.readAllLines(inputPath).get(0).trim());

        System.out.println("solvePartOne - Winner Elf: " + solvePartOne(numElves));
        System.out.println("solvePartTwo - Winner Elf: " + solvePartTwo(numElves));
    }

    /**
     * Solves Part 1: Finds the winning Elf using the Josephus problem formula.
     * @param numElves The number of Elves in the circle
     * @return The position of the winning Elf
     */
    public static int solvePartOne(int numElves) {
        // Josephus problem for step size 2
        int highestPowerOfTwo = Integer.highestOneBit(numElves);
        return 2 * (numElves - highestPowerOfTwo) + 1;
    }

    /**
     * Solves Part 2 efficiently for large input using mathematical analysis.
     * @param numElves The number of Elves in the circle
     * @return The position of the winning Elf
     */
    public static int solvePartTwo(int numElves) {
        // Efficient solution: Find the largest power of 3 less than or equal to numElves
        int power = 1;
        while (power * 3 <= numElves) {
            power *= 3;
        }
        if (numElves == power) {
            return numElves;
        } else if (numElves <= 2 * power) {
            return numElves - power;
        } else {
            return 2 * numElves - 3 * power;
        }
    }
}
