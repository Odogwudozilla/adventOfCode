package odogwudozilla.year2018.day16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;

/**
 * Advent of Code 2018 - Day 16: Chronal Classification
 *
 * This puzzle involves reverse-engineering opcodes for a device with four registers.
 * The device has 16 different opcodes that perform various operations (addition, multiplication,
 * bitwise operations, comparisons, and assignments). Given sample observations showing register
 * states before and after instruction execution, we need to determine which samples behave
 * like three or more opcodes.
 *
 * Puzzle URL: https://adventofcode.com/2018/day/16
 */
public class ChronalClassificationAOC2018Day16 {

    private static final int REGISTER_COUNT = 4;
    private static final int OPCODE_COUNT = 16;

    /**
     * Represents a sample observation with before state, instruction, and after state
     */
    static class Sample {
        int[] before;
        int[] instruction;
        int[] after;

        Sample(int[] before, int[] instruction, int[] after) {
            this.before = before;
            this.instruction = instruction;
            this.after = after;
        }
    }

    /**
     * Enumeration of all possible opcodes with their operation implementations
     */
    enum Opcode {
        ADDR((regs, args) -> regs[args[0]] + regs[args[1]]),
        ADDI((regs, args) -> regs[args[0]] + args[1]),
        MULR((regs, args) -> regs[args[0]] * regs[args[1]]),
        MULI((regs, args) -> regs[args[0]] * args[1]),
        BANR((regs, args) -> regs[args[0]] & regs[args[1]]),
        BANI((regs, args) -> regs[args[0]] & args[1]),
        BORR((regs, args) -> regs[args[0]] | regs[args[1]]),
        BORI((regs, args) -> regs[args[0]] | args[1]),
        SETR((regs, args) -> regs[args[0]]),
        SETI((regs, args) -> args[0]),
        GTIR((regs, args) -> args[0] > regs[args[1]] ? 1 : 0),
        GTRI((regs, args) -> regs[args[0]] > args[1] ? 1 : 0),
        GTRR((regs, args) -> regs[args[0]] > regs[args[1]] ? 1 : 0),
        EQIR((regs, args) -> args[0] == regs[args[1]] ? 1 : 0),
        EQRI((regs, args) -> regs[args[0]] == args[1] ? 1 : 0),
        EQRR((regs, args) -> regs[args[0]] == regs[args[1]] ? 1 : 0);

        private final BiFunction<int[], int[], Integer> operation;

        Opcode(BiFunction<int[], int[], Integer> operation) {
            this.operation = operation;
        }

        /**
         * Execute this opcode on the given registers
         * @param registers the current register state
         * @param a input A
         * @param b input B
         * @param c output register C
         * @return updated register state
         */
        int[] execute(int[] registers, int a, int b, int c) {
            int[] result = Arrays.copyOf(registers, registers.length);
            result[c] = operation.apply(registers, new int[]{a, b});
            return result;
        }
    }

    public static void main(String[] args) {
        try {
            String filePath = "src/main/resources/2018/day16/day16_puzzle_data.txt";
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            // Parse the input file
            List<Sample> samples = new ArrayList<>();
            List<int[]> testProgram = new ArrayList<>();
            parsePuzzleInput(lines, samples, testProgram);

            // Solve Part 1
            int part1Result = solvePartOne(samples);
            System.out.println("Part 1 - Samples behaving like 3+ opcodes: " + part1Result);

            // Solve Part 2
            int part2Result = solvePartTwo(samples, testProgram);
            System.out.println("Part 2 - Value in register 0 after test program: " + part2Result);

        } catch (IOException e) {
            System.err.println("Error reading puzzle input: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Parse the puzzle input into samples and test program
     * @param lines input lines
     * @param samples output list for samples
     * @param testProgram output list for test program instructions
     */
    private static void parsePuzzleInput(List<String> lines, List<Sample> samples, List<int[]> testProgram) {
        int i = 0;
        boolean inSampleSection = true;

        while (i < lines.size()) {
            String line = lines.get(i).trim();

            // Check for transition to test program (multiple blank lines)
            if (line.isEmpty()) {
                if (i + 1 < lines.size() && lines.get(i + 1).trim().isEmpty()) {
                    inSampleSection = false;
                    i += 2; // Skip blank lines
                    continue;
                }
                i++;
                continue;
            }

            if (inSampleSection && line.startsWith("Before:")) {
                // Parse a sample
                int[] before = parseRegisterState(line);
                int[] instruction = parseInstruction(lines.get(i + 1));
                int[] after = parseRegisterState(lines.get(i + 2));
                samples.add(new Sample(before, instruction, after));
                i += 3;
            } else if (!inSampleSection) {
                // Parse test program instruction
                testProgram.add(parseInstruction(line));
                i++;
            } else {
                i++;
            }
        }
    }

    /**
     * Parse register state from a line like "Before: [3, 2, 1, 1]"
     * @param line the line to parse
     * @return array of register values
     */
    private static int[] parseRegisterState(String line) {
        String numbersOnly = line.substring(line.indexOf('[') + 1, line.indexOf(']'));
        String[] parts = numbersOnly.split(",\\s*");
        int[] registers = new int[REGISTER_COUNT];
        for (int i = 0; i < REGISTER_COUNT; i++) {
            registers[i] = Integer.parseInt(parts[i]);
        }
        return registers;
    }

    /**
     * Parse an instruction line like "9 2 1 2"
     * @param line the line to parse
     * @return array of [opcode, A, B, C]
     */
    private static int[] parseInstruction(String line) {
        String[] parts = line.trim().split("\\s+");
        int[] instruction = new int[4];
        for (int i = 0; i < 4; i++) {
            instruction[i] = Integer.parseInt(parts[i]);
        }
        return instruction;
    }

    /**
     * Solve Part 1: Count samples that behave like three or more opcodes
     * @param samples list of sample observations
     * @return count of samples matching 3+ opcodes
     */
    private static int solvePartOne(List<Sample> samples) {
        int count = 0;

        for (Sample sample : samples) {
            int matchingOpcodes = countMatchingOpcodes(sample);
            if (matchingOpcodes >= 3) {
                count++;
            }
        }

        return count;
    }

    /**
     * Count how many opcodes could produce the observed result for a sample
     * @param sample the sample to test
     * @return number of matching opcodes
     */
    private static int countMatchingOpcodes(Sample sample) {
        int count = 0;
        int a = sample.instruction[1];
        int b = sample.instruction[2];
        int c = sample.instruction[3];

        for (Opcode opcode : Opcode.values()) {
            int[] result = opcode.execute(sample.before, a, b, c);
            if (Arrays.equals(result, sample.after)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Solve Part 2: Deduce opcode mappings and execute the test program
     * @param samples list of sample observations
     * @param testProgram the test program to execute
     * @return value in register 0 after execution
     */
    private static int solvePartTwo(List<Sample> samples, List<int[]> testProgram) {
        // Deduce opcode mappings
        Map<Integer, Opcode> opcodeMapping = deduceOpcodeMappings(samples);

        // Execute test program with the deduced mappings
        int[] registers = new int[REGISTER_COUNT];

        for (int[] instruction : testProgram) {
            int opcodeNumber = instruction[0];
            int a = instruction[1];
            int b = instruction[2];
            int c = instruction[3];

            Opcode opcode = opcodeMapping.get(opcodeNumber);
            registers = opcode.execute(registers, a, b, c);
        }

        return registers[0];
    }

    /**
     * Deduce which opcode number corresponds to which Opcode using constraint satisfaction
     * @param samples list of sample observations
     * @return mapping from opcode number to Opcode
     */
    private static Map<Integer, Opcode> deduceOpcodeMappings(List<Sample> samples) {
        // Build possible mappings for each opcode number
        Map<Integer, Set<Opcode>> possibleMappings = new HashMap<>();
        for (int i = 0; i < OPCODE_COUNT; i++) {
            possibleMappings.put(i, new HashSet<>(Arrays.asList(Opcode.values())));
        }

        // Process samples to eliminate impossible mappings
        for (Sample sample : samples) {
            int opcodeNumber = sample.instruction[0];
            int a = sample.instruction[1];
            int b = sample.instruction[2];
            int c = sample.instruction[3];

            Set<Opcode> matchingOpcodes = new HashSet<>();
            for (Opcode opcode : Opcode.values()) {
                int[] result = opcode.execute(sample.before, a, b, c);
                if (Arrays.equals(result, sample.after)) {
                    matchingOpcodes.add(opcode);
                }
            }

            // Keep only opcodes that match this sample
            possibleMappings.get(opcodeNumber).retainAll(matchingOpcodes);
        }

        // Use constraint propagation to resolve ambiguities
        Map<Integer, Opcode> finalMapping = new HashMap<>();
        while (finalMapping.size() < OPCODE_COUNT) {
            // Find an opcode number with only one possible mapping
            for (Map.Entry<Integer, Set<Opcode>> entry : possibleMappings.entrySet()) {
                if (entry.getValue().size() == 1) {
                    int opcodeNumber = entry.getKey();
                    Opcode resolvedOpcode = entry.getValue().iterator().next();
                    finalMapping.put(opcodeNumber, resolvedOpcode);

                    // Remove this opcode from all other possibilities
                    for (Set<Opcode> possibilities : possibleMappings.values()) {
                        possibilities.remove(resolvedOpcode);
                    }
                    break;
                }
            }
        }

        return finalMapping;
    }
}

