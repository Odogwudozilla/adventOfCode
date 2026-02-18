package odogwudozilla.year2016.day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Leonardo's Monorail (Advent of Code 2016 Day 12)
 * <p>
 * You must execute a set of assembunny instructions to determine the value left in register 'a'.
 * <p>
 * Official puzzle URL: https://adventofcode.com/2016/day/12
 *
 * @author Advent of Code Assistant
 */
public class LeonardosMonorailAOC2016Day12 {
    private static final String INPUT_PATH = "src/main/resources/2016/day12/day12_puzzle_data.txt";

    public static void main(String[] args) throws IOException {
        List<String> instructions = Files.readAllLines(Paths.get(INPUT_PATH));
        System.out.println("Part 1: " + solvePartOne(instructions));
        System.out.println("Part 2: " + solvePartTwo(instructions));
    }

    /**
     * Executes the assembunny instructions with all registers starting at 0.
     * @param instructions The list of assembunny instructions
     * @return The value left in register 'a' after execution
     */
    public static int solvePartOne(List<String> instructions) {
        Map<String, Integer> registers = new HashMap<>();
        registers.put("a", 0);
        registers.put("b", 0);
        registers.put("c", 0);
        registers.put("d", 0);
        return runAssembunny(instructions, registers);
    }

    /**
     * Executes the assembunny instructions with register 'c' starting at 1 (per Part 2).
     * @param instructions The list of assembunny instructions
     * @return The value left in register 'a' after execution
     */
    public static int solvePartTwo(List<String> instructions) {
        Map<String, Integer> registers = new HashMap<>();
        registers.put("a", 0);
        registers.put("b", 0);
        registers.put("c", 1);
        registers.put("d", 0);
        return runAssembunny(instructions, registers);
    }

    /**
     * Runs the assembunny program with the given initial register values.
     * @param instructions The list of assembunny instructions
     * @param registers The initial register values
     * @return The value in register 'a' after execution
     */
    private static int runAssembunny(List<String> instructions, Map<String, Integer> registers) {
        int ip = 0;
        while (ip < instructions.size()) {
            String[] parts = instructions.get(ip).split(" ");
            switch (parts[0]) {
                case "cpy":
                    int value = getValue(parts[1], registers);
                    if (registers.containsKey(parts[2])) {
                        registers.put(parts[2], value);
                    }
                    ip++;
                    break;
                case "inc":
                    if (registers.containsKey(parts[1])) {
                        registers.put(parts[1], registers.get(parts[1]) + 1);
                    }
                    ip++;
                    break;
                case "dec":
                    if (registers.containsKey(parts[1])) {
                        registers.put(parts[1], registers.get(parts[1]) - 1);
                    }
                    ip++;
                    break;
                case "jnz":
                    int test = getValue(parts[1], registers);
                    int offset = getValue(parts[2], registers);
                    if (test != 0) {
                        ip += offset;
                    } else {
                        ip++;
                    }
                    break;
                default:
                    ip++;
            }
        }
        return registers.get("a");
    }

    /**
     * Gets the value of a register or parses an integer.
     * @param x The register name or integer string
     * @param registers The register map
     * @return The integer value
     */
    private static int getValue(String x, Map<String, Integer> registers) {
        if (registers.containsKey(x)) {
            return registers.get(x);
        } else {
            return Integer.parseInt(x);
        }
    }
}

