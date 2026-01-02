package odogwudozilla.year2017.day5;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;

/**
 * A Maze of Twisty Trampolines, All Alike (Advent of Code 2017 Day 5)
 * <p>
 * An urgent interrupt arrives from the CPU: it's trapped in a maze of jump instructions, and it would like assistance from any programs with spare cycles to help find the exit.
 * The message includes a list of the offsets for each jump. Jumps are relative: -1 moves to the previous instruction, and 2 skips the next one. Start at the first instruction in the list. The goal is to follow the jumps until one leads outside the list.
 * In addition, these instructions are a little strange; after each jump, the offset of that instruction increases by 1. So, if you come across an offset of 3, you would move three instructions forward, but change it to a 4 for the next time it is encountered.
 * <p>
 * Official puzzle link: https://adventofcode.com/2017/day/5
 */
public class AmazeOfTwistyTrampolinesAOC2017Day5 {
    private static final String INPUT_PATH = "src/main/resources/2017/day5/day5_puzzle_data.txt";

    /**
     * Entry point for the solution.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            List<Integer> input = Files.readAllLines(Paths.get(INPUT_PATH))
                    .stream()
                    .filter(line -> !line.trim().isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            int partOneResult = solvePartOne(input);
            System.out.println("solvePartOne - Steps to exit: " + partOneResult);
            int partTwoResult = solvePartTwo(input);
            System.out.println("solvePartTwo - Steps to exit: " + partTwoResult);
        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1: Calculates the number of steps required to exit the maze.
     * @param offsets List of jump offsets
     * @return Number of steps to exit
     */
    public static int solvePartOne(List<Integer> offsets) {
        // Copy the list to avoid mutating the original
        int[] instructions = offsets.stream().mapToInt(Integer::intValue).toArray();
        int steps = 0;
        int position = 0;
        while (position >= 0 && position < instructions.length) {
            int jump = instructions[position];
            instructions[position] = jump + 1;
            position += jump;
            steps++;
        }
        return steps;
    }

    /**
     * Solves Part 2: Calculates steps to exit with new jump rule.
     * If offset is 3 or more, decrease by 1; otherwise, increase by 1.
     * @param offsets List of jump offsets
     * @return Number of steps to exit
     */
    public static int solvePartTwo(List<Integer> offsets) {
        int[] instructions = offsets.stream().mapToInt(Integer::intValue).toArray();
        int steps = 0;
        int position = 0;
        while (position >= 0 && position < instructions.length) {
            int jump = instructions[position];
            if (jump >= 3) {
                instructions[position] = jump - 1;
            } else {
                instructions[position] = jump + 1;
            }
            position += jump;
            steps++;
        }
        return steps;
    }
}
