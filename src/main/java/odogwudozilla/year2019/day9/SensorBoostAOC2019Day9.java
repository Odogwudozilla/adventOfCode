package odogwudozilla.year2019.day9;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;

/**
 * Sensor Boost (Advent of Code 2019 Day 9)
 *
 * You must implement an Intcode computer with support for relative mode parameters and large memory.
 * The BOOST program will ask for a single input; run it in test mode by providing it the value 1.
 * It will output a BOOST keycode if the computer is fully functional.
 *
 * Official puzzle link: https://adventofcode.com/2019/day/9
 *
 * @param args Command line arguments (not used)
 * @return None
 */
public class SensorBoostAOC2019Day9 {
    private static final String INPUT_PATH = "src/main/resources/2019/day9/day9_puzzle_data.txt";
    private static final long TEST_INPUT = 1L;

    public static void main(String[] args) {
        try {
            List<Long> program = readInput(INPUT_PATH);
            long partOneResult = solvePartOne(program);
            System.out.println("solvePartOne - BOOST keycode: " + partOneResult);
            long partTwoResult = solvePartTwo(program);
            System.out.println("solvePartTwo - Distress signal coordinates: " + partTwoResult);
        } catch (IOException e) {
            System.err.println("main - Error reading input: " + e.getMessage());
        }
    }

    /**
     * Reads the puzzle input file and parses it into a list of longs.
     * @param path Path to the input file
     * @return List of program instructions as longs
     */
    private static List<Long> readInput(String path) throws IOException {
        String line = Files.readAllLines(Paths.get(path)).get(0);
        String[] tokens = line.split(",");
        List<Long> program = new ArrayList<>();
        for (String token : tokens) {
            program.add(Long.parseLong(token.trim()));
        }
        return program;
    }

    /**
     * Solves Part 1 of the puzzle by running the BOOST program in test mode (input = 1).
     * @param program The Intcode program
     * @return The BOOST keycode output
     */
    public static long solvePartOne(List<Long> program) {
        IntcodeComputer computer = new IntcodeComputer(program);
        computer.addInput(TEST_INPUT);
        computer.run();
        List<Long> outputs = computer.getOutputs();
        return outputs.get(outputs.size() - 1); // Only output should be the BOOST keycode
    }

    /**
     * Solves Part 2 of the puzzle (if available).
     * @param program The Intcode program
     * @return The output for Part 2
     */
    public static long solvePartTwo(List<Long> program) {
        IntcodeComputer computer = new IntcodeComputer(program);
        computer.addInput(2L); // Part 2 input is 2
        computer.run();
        List<Long> outputs = computer.getOutputs();
        return outputs.get(outputs.size() - 1);
    }

    /**
     * Intcode computer implementation supporting relative mode and large memory.
     */
    static class IntcodeComputer {
        private final List<Long> memory;
        private long relativeBase = 0;
        private int pointer = 0;
        private final List<Long> inputs = new ArrayList<>();
        private final List<Long> outputs = new ArrayList<>();

        public IntcodeComputer(List<Long> program) {
            this.memory = new ArrayList<>(program);
            // Extend memory to a large size
            for (int i = 0; i < 10000; i++) {
                this.memory.add(0L);
            }
        }

        public void addInput(long value) {
            inputs.add(value);
        }

        public List<Long> getOutputs() {
            return outputs;
        }

        public void run() {
            while (true) {
                int opcode = (int) (memory.get(pointer) % 100);
                int[] modes = getParameterModes(memory.get(pointer));
                switch (opcode) {
                    case 1: // Add
                        setParam(3, modes[2], getParam(1, modes[0]) + getParam(2, modes[1]));
                        pointer += 4;
                        break;
                    case 2: // Multiply
                        setParam(3, modes[2], getParam(1, modes[0]) * getParam(2, modes[1]));
                        pointer += 4;
                        break;
                    case 3: // Input
                        if (inputs.isEmpty()) throw new IllegalStateException("run - No input available");
                        setParam(1, modes[0], inputs.remove(0));
                        pointer += 2;
                        break;
                    case 4: // Output
                        outputs.add(getParam(1, modes[0]));
                        pointer += 2;
                        break;
                    case 5: // Jump-if-true
                        if (getParam(1, modes[0]) != 0) pointer = (int) getParam(2, modes[1]);
                        else pointer += 3;
                        break;
                    case 6: // Jump-if-false
                        if (getParam(1, modes[0]) == 0) pointer = (int) getParam(2, modes[1]);
                        else pointer += 3;
                        break;
                    case 7: // Less than
                        setParam(3, modes[2], getParam(1, modes[0]) < getParam(2, modes[1]) ? 1L : 0L);
                        pointer += 4;
                        break;
                    case 8: // Equals
                        setParam(3, modes[2], getParam(1, modes[0]) == getParam(2, modes[1]) ? 1L : 0L);
                        pointer += 4;
                        break;
                    case 9: // Adjust relative base
                        relativeBase += getParam(1, modes[0]);
                        pointer += 2;
                        break;
                    case 99: // Halt
                        return;
                    default:
                        throw new IllegalStateException("run - Unknown opcode: " + opcode);
                }
            }
        }

        private int[] getParameterModes(long instruction) {
            int[] modes = new int[3];
            modes[0] = (int) ((instruction / 100) % 10);
            modes[1] = (int) ((instruction / 1000) % 10);
            modes[2] = (int) ((instruction / 10000) % 10);
            return modes;
        }

        private long getParam(int paramIdx, int mode) {
            long value;
            switch (mode) {
                case 0: // Position mode
                    value = getMemory(memory.get(pointer + paramIdx).intValue());
                    break;
                case 1: // Immediate mode
                    value = memory.get(pointer + paramIdx);
                    break;
                case 2: // Relative mode
                    value = getMemory((int) (memory.get(pointer + paramIdx) + relativeBase));
                    break;
                default:
                    throw new IllegalStateException("getParam - Unknown mode: " + mode);
            }
            return value;
        }

        private void setParam(int paramIdx, int mode, long value) {
            int address;
            switch (mode) {
                case 0:
                    address = memory.get(pointer + paramIdx).intValue();
                    break;
                case 2:
                    address = (int) (memory.get(pointer + paramIdx) + relativeBase);
                    break;
                default:
                    throw new IllegalStateException("setParam - Invalid mode for writing: " + mode);
            }
            setMemory(address, value);
        }

        private long getMemory(int address) {
            if (address < 0 || address >= memory.size()) throw new IllegalStateException("getMemory - Invalid address: " + address);
            return memory.get(address);
        }

        private void setMemory(int address, long value) {
            if (address < 0 || address >= memory.size()) throw new IllegalStateException("setMemory - Invalid address: " + address);
            memory.set(address, value);
        }
    }
}
