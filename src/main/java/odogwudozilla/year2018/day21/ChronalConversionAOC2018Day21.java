package odogwudozilla.year2018.day21;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Advent of Code 2018 - Day 21: Chronal Conversion
 * <p>
 * Puzzle URL: https://adventofcode.com/2018/day/21
 */
public final class ChronalConversionAOC2018Day21 {

    private static final String INPUT_FILE = "/2018/day21/day21_puzzle_data.txt";

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
     * The key insight is that the program checks if register 0 equals register 3
     * at a specific point. Part 1 asks for the value that halts the program after
     * executing the fewest instructions, which is the first value register 3 reaches
     * at the comparison point.
     *
     * @param input list of input lines
     * @return the Part 1 answer
     */
    private static String solvePartOne(List<String> input) {
        Program program = Program.parse(input);

        // Execute the program and find the first value that would cause termination
        long[] registers = new long[6];
        Set<Long> seen = new HashSet<>();
        long firstValue = -1;

        while (!program.isHalted()) {
            // Check if we're at the instruction that compares registers 3 and 0
            if (program.checkpointReached(registers)) {
                long reg3Value = registers[3];
                if (firstValue == -1) {
                    firstValue = reg3Value;
                }
                // For Part 1, we just need the first value
                break;
            }
            program.step(registers);
        }

        return String.valueOf(firstValue);
    }

    /**
     * Solves Part 2 of the puzzle.
     * Part 2 asks for the value that halts the program after executing the most
     * instructions. This is the last unique value that register 3 reaches before
     * the sequence starts to repeat.
     *
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        Program program = Program.parse(input);

        // Execute the program and find the last unique value before the sequence repeats
        long[] registers = new long[6];
        Set<Long> seen = new HashSet<>();
        long lastValue = -1;

        while (!program.isHalted()) {
            // Check if we're at the instruction that compares registers 3 and 0
            if (program.checkpointReached(registers)) {
                long reg3Value = registers[3];
                if (seen.contains(reg3Value)) {
                    // We've seen this value before, so the sequence is about to repeat
                    break;
                }
                seen.add(reg3Value);
                lastValue = reg3Value;
            }
            program.step(registers);
        }

        return String.valueOf(lastValue);
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = ChronalConversionAOC2018Day21.class.getResourceAsStream(INPUT_FILE)) {
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

    // ==== Program class and instruction handling ====

    private static class Program {
        private final List<Instruction> instructions;
        private final int ipRegister;
        private int pc; // program counter / instruction pointer

        Program(List<Instruction> instructions, int ipRegister) {
            this.instructions = instructions;
            this.ipRegister = ipRegister;
            this.pc = 0;
        }

        static Program parse(List<String> lines) {
            int ipRegister = 0;
            List<Instruction> instructions = new ArrayList<>();

            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("#ip ")) {
                    ipRegister = Integer.parseInt(line.substring(4));
                } else {
                    instructions.add(Instruction.parse(line));
                }
            }

            return new Program(instructions, ipRegister);
        }

        boolean isHalted() {
            return pc < 0 || pc >= instructions.size();
        }

        boolean checkpointReached(long[] registers) {
            // The checkpoint is the instruction at index 28 (eqrr 3 0 1)
            // This is where the program checks if register 3 == register 0
            return pc == 28;
        }

        void step(long[] registers) {
            if (isHalted()) return;

            // Set the instruction pointer register to the current PC value
            registers[ipRegister] = pc;

            // Get and execute the current instruction
            Instruction instr = instructions.get(pc);
            instr.execute(registers);

            // Read the updated instruction pointer from the register
            pc = (int) registers[ipRegister];
            // Increment instruction pointer for next cycle
            pc++;
        }
    }

    private static class Instruction {
        final String opcode;
        final int a, b, c;

        Instruction(String opcode, int a, int b, int c) {
            this.opcode = opcode;
            this.a = a;
            this.b = b;
            this.c = c;
        }

        static Instruction parse(String line) {
            String[] parts = line.split(" ");
            return new Instruction(parts[0], Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }

        void execute(long[] registers) {
            switch (opcode) {
                case "addr": registers[c] = registers[a] + registers[b]; break;
                case "addi": registers[c] = registers[a] + b; break;
                case "mulr": registers[c] = registers[a] * registers[b]; break;
                case "muli": registers[c] = registers[a] * b; break;
                case "banr": registers[c] = registers[a] & registers[b]; break;
                case "bani": registers[c] = registers[a] & b; break;
                case "borr": registers[c] = registers[a] | registers[b]; break;
                case "bori": registers[c] = registers[a] | b; break;
                case "setr": registers[c] = registers[a]; break;
                case "seti": registers[c] = a; break;
                case "gtir": registers[c] = (a > registers[b]) ? 1 : 0; break;
                case "gtri": registers[c] = (registers[a] > b) ? 1 : 0; break;
                case "gtrr": registers[c] = (registers[a] > registers[b]) ? 1 : 0; break;
                case "eqir": registers[c] = (a == registers[b]) ? 1 : 0; break;
                case "eqri": registers[c] = (registers[a] == b) ? 1 : 0; break;
                case "eqrr": registers[c] = (registers[a] == registers[b]) ? 1 : 0; break;
                default:
                    throw new IllegalArgumentException("Unknown opcode: " + opcode);
            }
        }
    }
}
