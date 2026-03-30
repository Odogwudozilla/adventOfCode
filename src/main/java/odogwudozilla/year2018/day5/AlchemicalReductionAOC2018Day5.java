package odogwudozilla.year2018.day5;

import org.jetbrains.annotations.NotNull;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.Deque;
import java.util.ArrayDeque;

/**
 * Alchemical Reduction (Advent of Code 2018 Day 5)
 * <p>
 * You've managed to sneak in to the prototype suit manufacturing lab. The Elves are making decent progress, but are still struggling with the suit's size reduction capabilities.
 * <p>
 * While the very latest in 1518 alchemical technology might have solved their problem eventually, you can do better. You scan the chemical composition of the suit's material and discover that it is formed by extremely long polymers (one of which is available as your puzzle input).
 * <p>
 * The polymer is formed by smaller units which, when triggered, react with each other such that two adjacent units of the same type and opposite polarity are destroyed. Units' types are represented by letters; units' polarity is represented by capitalisation. For instance, r and R are units with the same type but opposite polarity, whereas r and s are entirely different types and do not react.
 * <p>
 * Official puzzle URL: https://adventofcode.com/2018/day/5
 */
public class AlchemicalReductionAOC2018Day5 {
    /**
     * Entry point for the solution.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        String inputPath = "src/main/resources/2018/day5/day5_puzzle_data.txt";
        String polymer;
        try {
            polymer = Files.readAllLines(Paths.get(inputPath)).get(0).trim();
        } catch (IOException e) {
            System.err.println("main - Failed to read input file: " + e.getMessage());
            return;
        }
        int partOneResult = solvePartOne(polymer);
        System.out.println("Part 1: Remaining units after full reaction: " + partOneResult);
        int partTwoResult = solvePartTwo(polymer);
        System.out.println("Part 2: Shortest polymer after removing one unit type: " + partTwoResult);
    }

    /**
     * Reduces the polymer by reacting adjacent units of the same type and opposite polarity.
     * @param polymer the input polymer string
     * @return the length of the fully reacted polymer
     */
    public static int solvePartOne(@NotNull String polymer) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char unit : polymer.toCharArray()) {
            if (!stack.isEmpty() && reacts(stack.peek(), unit)) {
                stack.pop();
            } else {
                stack.push(unit);
            }
        }
        return stack.size();
    }

    /**
     * Finds the shortest polymer by removing all units of one type and fully reacting.
     * @param polymer the input polymer string
     * @return the length of the shortest possible polymer
     */
    public static int solvePartTwo(@NotNull String polymer) {
        int minLength = Integer.MAX_VALUE;
        for (char unitType = 'a'; unitType <= 'z'; unitType++) {
            String filtered = polymer.replaceAll("[" + unitType + Character.toUpperCase(unitType) + "]", "");
            int reactedLength = solvePartOne(filtered);
            if (reactedLength < minLength) {
                minLength = reactedLength;
            }
        }
        return minLength;
    }

    /**
     * Checks if two units react (same type, opposite polarity).
     * @param a first unit
     * @param b second unit
     * @return true if they react, false otherwise
     */
    private static boolean reacts(char a, char b) {
        return Math.abs(a - b) == 32;
    }
}

