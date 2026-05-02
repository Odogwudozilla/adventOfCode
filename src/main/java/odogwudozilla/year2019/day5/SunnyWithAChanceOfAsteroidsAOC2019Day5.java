package odogwudozilla.year2019.day5;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2019 - Day 5: Sunny with a Chance of Asteroids
 * <p>
 * Puzzle URL: https://adventofcode.com/2019/day/5
 */
public final class SunnyWithAChanceOfAsteroidsAOC2019Day5 {

    private static final String INPUT_FILE = "/2019/day5/day5_puzzle_data.txt";

    /**
     * Entry point. Reads puzzle input and prints solutions for Part 1 and Part 2.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        List<String> input = readInput();
        System.out.println("Part 1: " + solvePartOne(input));
        System.out.println("Part 2: " + solvePartTwo(input));
    }

    /**
     * Solves Part 1 of the puzzle.
     * @param input list of input lines
     * @return the Part 1 answer
     */
    private static String solvePartOne(List<String> input) {
        // Implements the Intcode computer for Part 1 (input = 1)
        int[] program = parseProgram(input.get(0));
        return String.valueOf(runIntcode(program, 1));
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        // Implements the Intcode computer for Part 2 (input = 5)
        int[] program = parseProgram(input.get(0));
        return String.valueOf(runIntcode(program, 5));
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = SunnyWithAChanceOfAsteroidsAOC2019Day5.class.getResourceAsStream(INPUT_FILE)) {
            if (stream == null) {
                throw new IllegalStateException("Input file not found: " + INPUT_FILE);
            }
            Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8);
            java.util.List<String> lines = new java.util.ArrayList<>();
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            return lines;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read input file", e);
        }
    }

    /**
     * Parses a comma-separated Intcode program into an int array.
     * @param line the input line
     * @return the program as an int array
     */
    private static int[] parseProgram(String line) {
        String[] parts = line.trim().split(",");
        int[] program = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            program[i] = Integer.parseInt(parts[i]);
        }
        return program;
    }

    /**
     * Runs the Intcode program with the given input value and returns the last output.
     * @param memory the program memory
     * @param inputValue the input value to provide
     * @return the last output produced by the program
     */
    private static int runIntcode(int[] memory, int inputValue) {
        int[] mem = java.util.Arrays.copyOf(memory, memory.length);
        int ip = 0;
        int output = -1;
        while (true) {
            int instr = mem[ip];
            int opcode = instr % 100;
            int mode1 = (instr / 100) % 10;
            int mode2 = (instr / 1000) % 10;
            int mode3 = (instr / 10000) % 10;
            if (opcode == 99) break;
            int p1, p2, p3;
            switch (opcode) {
                case 1: // add
                    p1 = getParam(mem, ip + 1, mode1);
                    p2 = getParam(mem, ip + 2, mode2);
                    p3 = mem[ip + 3];
                    mem[p3] = p1 + p2;
                    ip += 4;
                    break;
                case 2: // multiply
                    p1 = getParam(mem, ip + 1, mode1);
                    p2 = getParam(mem, ip + 2, mode2);
                    p3 = mem[ip + 3];
                    mem[p3] = p1 * p2;
                    ip += 4;
                    break;
                case 3: // input
                    p1 = mem[ip + 1];
                    mem[p1] = inputValue;
                    ip += 2;
                    break;
                case 4: // output
                    p1 = getParam(mem, ip + 1, mode1);
                    output = p1;
                    ip += 2;
                    break;
                case 5: // jump-if-true
                    p1 = getParam(mem, ip + 1, mode1);
                    p2 = getParam(mem, ip + 2, mode2);
                    if (p1 != 0) {
                        ip = p2;
                    } else {
                        ip += 3;
                    }
                    break;
                case 6: // jump-if-false
                    p1 = getParam(mem, ip + 1, mode1);
                    p2 = getParam(mem, ip + 2, mode2);
                    if (p1 == 0) {
                        ip = p2;
                    } else {
                        ip += 3;
                    }
                    break;
                case 7: // less than
                    p1 = getParam(mem, ip + 1, mode1);
                    p2 = getParam(mem, ip + 2, mode2);
                    p3 = mem[ip + 3];
                    mem[p3] = (p1 < p2) ? 1 : 0;
                    ip += 4;
                    break;
                case 8: // equals
                    p1 = getParam(mem, ip + 1, mode1);
                    p2 = getParam(mem, ip + 2, mode2);
                    p3 = mem[ip + 3];
                    mem[p3] = (p1 == p2) ? 1 : 0;
                    ip += 4;
                    break;
                default:
                    throw new IllegalStateException("Unknown opcode: " + opcode);
            }
        }
        return output;
    }

    /**
     * Gets the parameter value based on its mode.
     * @param mem the program memory
     * @param pos the parameter position
     * @param mode the parameter mode (0=position, 1=immediate)
     * @return the parameter value
     */
    private static int getParam(int[] mem, int pos, int mode) {
        return (mode == 0) ? mem[mem[pos]] : mem[pos];
    }
}
