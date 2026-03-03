/**
 * Infinite Elves and Infinite Houses (Advent of Code 2015 Day 20)
 *
 * Santa sends infinite elves to deliver presents to infinite houses. Each elf delivers presents to houses that are multiples of their elf number. Each elf delivers presents equal to ten times their number at each house. The goal is to find the lowest house number that receives at least as many presents as the puzzle input.
 *
 * Puzzle link: https://adventofcode.com/2015/day/20
 *
 * @author Advent of Code
 */
package odogwudozilla.year2015.day20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class InfiniteElvesAndInfiniteHousesAOC2015Day20 {
    private static final String INPUT_PATH = "src/main/resources/2015/day20/day20_puzzle_data.txt";
    private static final int PRESENTS_PER_ELF = 10;

    /**
     * Entry point for the solution.
     * @param args Command line arguments
     */
    public static void main(String[] args) throws IOException {
        int targetPresents = readInput();
        int result = solvePartOne(targetPresents);
        System.out.println("Part 1: Lowest house number with at least " + targetPresents + " presents: " + result);
        int result2 = solvePartTwo(targetPresents);
        System.out.println("Part 2: Lowest house number with at least " + targetPresents + " presents (Elf visits 50 houses, 11x presents): " + result2);
    }

    /**
     * Solves Part 1: Finds the lowest house number to get at least the target number of presents.
     * @param targetPresents The minimum number of presents required
     * @return The lowest house number meeting the requirement
     */
    public static int solvePartOne(int targetPresents) {
        // Reasonable upper bound: targetPresents / 10 (since each house gets at least 10 presents)
        int maxHouse = targetPresents / PRESENTS_PER_ELF;
        int[] presents = new int[maxHouse + 1];
        for (int elf = 1; elf <= maxHouse; elf++) {
            for (int house = elf; house <= maxHouse; house += elf) {
                presents[house] += elf * PRESENTS_PER_ELF;
            }
        }
        for (int house = 1; house <= maxHouse; house++) {
            if (presents[house] >= targetPresents) {
                return house;
            }
        }
        return -1; // Not found
    }

    /**
     * Solves Part 2: Each Elf only visits 50 houses, and delivers presents equal to eleven times their number at each house.
     * @param targetPresents The minimum number of presents required
     * @return The lowest house number meeting the requirement
     */
    public static int solvePartTwo(int targetPresents) {
        final int presentsPerElf = 11;
        int maxHouse = targetPresents / presentsPerElf;
        int[] presents = new int[maxHouse + 1];
        for (int elf = 1; elf <= maxHouse; elf++) {
            int housesDelivered = 0;
            for (int house = elf; house <= maxHouse && housesDelivered < 50; house += elf, housesDelivered++) {
                presents[house] += elf * presentsPerElf;
            }
        }
        for (int house = 1; house <= maxHouse; house++) {
            if (presents[house] >= targetPresents) {
                return house;
            }
        }
        return -1; // Not found
    }

    /**
     * Reads the puzzle input from the resource file.
     * @return The target number of presents
     * @throws IOException If file reading fails
     */
    private static int readInput() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(INPUT_PATH));
        return Integer.parseInt(lines.get(0).trim());
    }
}
