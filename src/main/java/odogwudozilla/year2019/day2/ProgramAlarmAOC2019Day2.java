package odogwudozilla.year2019.day2;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;

/**
 * 1202 Program Alarm (Advent of Code 2019 Day 2)
 * <p>
 * Simulates an Intcode computer to restore the gravity assist program. Replace position 1 with 12 and position 2 with 2 before running. Output the value at position 0 after the program halts.
 * <p>
 * Official puzzle link: https://adventofcode.com/2019/day/2
 *
 * @author GitHub Copilot
 */
public class ProgramAlarmAOC2019Day2 {
    /**
     * Entry point for the solution.
     * Reads input and prints the result for Part 1.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        String inputPath = "src/main/resources/2019/day2/day2_puzzle_data.txt";
        try {
            List<String> lines = Files.readAllLines(Paths.get(inputPath));
            int result = solvePartOne(lines.get(0));
            System.out.println("main - Part 1 result: " + result);
            int partTwoResult = solvePartTwo(lines.get(0), 19690720);
            System.out.println("main - Part 2 result: " + partTwoResult);
        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Runs the Intcode program for Part 1 after restoring the gravity assist state.
     *
     * @param input Comma-separated Intcode program
     * @return Value at position 0 after program halts
     */
    public static int solvePartOne(String input) {
        int[] memory = Arrays.stream(input.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
        memory[1] = 12;
        memory[2] = 2;
        int pointer = 0;
        while (memory[pointer] != 99) {
            int opcode = memory[pointer];
            int a = memory[memory[pointer + 1]];
            int b = memory[memory[pointer + 2]];
            int dest = memory[pointer + 3];
            if (opcode == 1) {
                memory[dest] = a + b;
            } else if (opcode == 2) {
                memory[dest] = a * b;
            } else {
                throw new IllegalStateException("Unknown opcode: " + opcode);
            }
            pointer += 4;
        }
        return memory[0];
    }

    /**
     * Runs the Intcode program with specified noun and verb.
     *
     * @param input Comma-separated Intcode program
     * @param noun Value to set at position 1
     * @param verb Value to set at position 2
     * @return Value at position 0 after program halts
     */
    public static int runIntcode(String input, int noun, int verb) {
        int[] memory = Arrays.stream(input.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
        memory[1] = noun;
        memory[2] = verb;
        int pointer = 0;
        while (memory[pointer] != 99) {
            int opcode = memory[pointer];
            int a = memory[memory[pointer + 1]];
            int b = memory[memory[pointer + 2]];
            int dest = memory[pointer + 3];
            if (opcode == 1) {
                memory[dest] = a + b;
            } else if (opcode == 2) {
                memory[dest] = a * b;
            } else {
                throw new IllegalStateException("Unknown opcode: " + opcode);
            }
            pointer += 4;
        }
        return memory[0];
    }

    /**
     * Finds the noun and verb that cause the program to produce the target output.
     * Returns 100 * noun + verb as required by the puzzle.
     *
     * @param input Comma-separated Intcode program
     * @param targetOutput Desired output at position 0
     * @return 100 * noun + verb for the correct input pair
     */
    public static int solvePartTwo(String input, int targetOutput) {
        for (int noun = 0; noun <= 99; noun++) {
            for (int verb = 0; verb <= 99; verb++) {
                int output = runIntcode(input, noun, verb);
                if (output == targetOutput) {
                    return 100 * noun + verb;
                }
            }
        }
        throw new IllegalStateException("No valid noun/verb found for target output: " + targetOutput);
    }
}
