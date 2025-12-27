package odogwudozilla.year2022.day3;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Rucksack Reorganization: Find the sum of priorities for misplaced items in rucksacks and badge items for groups of Elves.
 * Official puzzle link: https://adventofcode.com/2022/day/3
 */
public class RucksackReorganizationAOC2022Day3 {
    private static final String INPUT_PATH = "src/main/resources/2022/day3/day3_puzzle_data.txt";

    /**
     * Entry point for the solution.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        try {
            List<String> input = Files.readAllLines(Paths.get(INPUT_PATH));
            System.out.println("solvePartOne - Sum of priorities: " + solvePartOne(input));
            System.out.println("solvePartTwo - Sum of badge priorities: " + solvePartTwo(input));
        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1: Find the sum of priorities for items that appear in both compartments of each rucksack.
     *
     * @param rucksacks List of rucksack strings
     * @return Sum of priorities
     */
    public static int solvePartOne(List<String> rucksacks) {
        int sum = 0;
        for (String rucksack : rucksacks) {
            int len = rucksack.length();
            String first = rucksack.substring(0, len / 2);
            String second = rucksack.substring(len / 2);
            Set<Character> firstSet = new HashSet<>();
            for (char c : first.toCharArray()) firstSet.add(c);
            for (char c : second.toCharArray()) {
                if (firstSet.contains(c)) {
                    sum += getPriority(c);
                    break;
                }
            }
        }
        return sum;
    }

    /**
     * Solves Part 2: Find the sum of badge priorities for each group of three Elves.
     *
     * @param rucksacks List of rucksack strings
     * @return Sum of badge priorities
     */
    public static int solvePartTwo(List<String> rucksacks) {
        int sum = 0;
        for (int i = 0; i < rucksacks.size(); i += 3) {
            String a = rucksacks.get(i);
            String b = rucksacks.get(i + 1);
            String c = rucksacks.get(i + 2);
            Set<Character> setA = new HashSet<>();
            Set<Character> setB = new HashSet<>();
            for (char ch : a.toCharArray()) setA.add(ch);
            for (char ch : b.toCharArray()) setB.add(ch);
            for (char ch : c.toCharArray()) {
                if (setA.contains(ch) && setB.contains(ch)) {
                    sum += getPriority(ch);
                    break;
                }
            }
        }
        return sum;
    }

    /**
     * Returns the priority of an item type.
     *
     * @param c Item character
     * @return Priority value
     */
    private static int getPriority(char c) {
        if (c >= 'a' && c <= 'z') return c - 'a' + 1;
        if (c >= 'A' && c <= 'Z') return c - 'A' + 27;
        return 0;
    }
}

