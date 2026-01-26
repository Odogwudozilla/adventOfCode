package odogwudozilla.year2020.day8;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Solution for Advent of Code 2020 Day 8: Handheld Halting.
 *
 * This puzzle involves a boot code program with three instruction types:
 * - acc: increases/decreases the accumulator
 * - jmp: jumps to a new instruction relative to itself
 * - nop: no operation
 *
 * Part 1 finds the accumulator value immediately before an instruction would execute twice.
 * Part 2 identifies which single instruction must be changed (nop to jmp or jmp to nop)
 * to make the program terminate normally.
 *
 * Puzzle link: https://adventofcode.com/2020/day/8
 */
public class HandheldHaltingAOC2020Day8 {

    private static final String INPUT_FILE = "src/main/resources/2020/day8/day8_puzzle_data.txt";

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(INPUT_FILE));
        List<Instruction> instructions = parseInstructions(lines);

        int partOne = solvePartOne(instructions);
        System.out.println("main - Part 1: " + partOne);

        int partTwo = solvePartTwo(instructions);
        System.out.println("main - Part 2: " + partTwo);
    }

    /**
     * Solves Part 1: Find the accumulator value immediately before any instruction
     * is executed a second time.
     */
    private static int solvePartOne(List<Instruction> instructions) {
        ExecutionResult result = executeProgram(instructions);
        return result.accumulator;
    }

    /**
     * Solves Part 2: Find which single instruction to change (nop to jmp or vice versa)
     * to make the program terminate successfully.
     */
    private static int solvePartTwo(List<Instruction> originalInstructions) {
        for (int i = 0; i < originalInstructions.size(); i++) {
            Instruction original = originalInstructions.get(i);

            // Only try changing jmp or nop instructions
            if ("acc".equals(original.operation)) {
                continue;
            }

            // Create a copy of the instruction list for this test
            List<Instruction> testInstructions = new java.util.ArrayList<>(originalInstructions);

            // Flip the instruction
            String flippedOp = "jmp".equals(original.operation) ? "nop" : "jmp";
            testInstructions.set(i, new Instruction(flippedOp, original.argument));

            // Test if this fixes the program
            ExecutionResult result = executeProgram(testInstructions);
            if (result.terminated) {
                return result.accumulator;
            }
        }

        return -1; // Should not reach here if puzzle is solvable
    }

    /**
     * Executes the program and returns the result (accumulator value and termination status).
     */
    private static ExecutionResult executeProgram(List<Instruction> instructions) {
        int accumulator = 0;
        int instructionPointer = 0;
        Set<Integer> visitedInstructions = new HashSet<>();

        while (instructionPointer < instructions.size()) {
            // Check if we've seen this instruction before
            if (visitedInstructions.contains(instructionPointer)) {
                return new ExecutionResult(accumulator, false);
            }

            visitedInstructions.add(instructionPointer);
            Instruction instruction = instructions.get(instructionPointer);

            switch (instruction.operation) {
                case "acc":
                    accumulator += instruction.argument;
                    instructionPointer++;
                    break;
                case "jmp":
                    instructionPointer += instruction.argument;
                    break;
                case "nop":
                    instructionPointer++;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown operation: " + instruction.operation);
            }
        }

        return new ExecutionResult(accumulator, true);
    }

    /**
     * Parses the input lines into Instruction objects.
     */
    private static List<Instruction> parseInstructions(List<String> lines) {
        return lines.stream()
                .map(line -> {
                    String[] parts = line.split(" ");
                    String operation = parts[0];
                    int argument = Integer.parseInt(parts[1]);
                    return new Instruction(operation, argument);
                })
                .toList();
    }

    /**
     * Represents a single instruction in the boot code.
     */
    private static class Instruction {
        String operation;
        int argument;

        Instruction(String operation, int argument) {
            this.operation = operation;
            this.argument = argument;
        }
    }

    /**
     * Represents the result of program execution.
     */
    private static class ExecutionResult {
        int accumulator;
        boolean terminated;

        ExecutionResult(int accumulator, boolean terminated) {
            this.accumulator = accumulator;
            this.terminated = terminated;
        }
    }
}

