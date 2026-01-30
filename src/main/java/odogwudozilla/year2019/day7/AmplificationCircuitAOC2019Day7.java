package odogwudozilla.year2019.day7;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;

/**
 * Amplification Circuit (Advent of Code 2019 Day 7)
 * <p>
 * Configure a series of five amplifiers connected in series to maximise the thruster signal.
 * Each amplifier runs an Intcode program with a unique phase setting (0-4 for Part 1, 5-9 for Part 2).
 * The goal is to find the phase setting sequence that produces the highest output signal.
 * <p>
 * Official puzzle link: https://adventofcode.com/2019/day/7
 */
public class AmplificationCircuitAOC2019Day7 {
    private static final String INPUT_PATH = "src/main/resources/2019/day7/day7_puzzle_data.txt";
    private static final int AMPLIFIER_COUNT = 5;

    public static void main(String[] args) {
        try {
            List<Integer> program = readInput(INPUT_PATH);
            int partOneResult = solvePartOne(program);
            System.out.println("main - Part 1 result: " + partOneResult);
            int partTwoResult = solvePartTwo(program);
            System.out.println("main - Part 2 result: " + partTwoResult);
        } catch (IOException e) {
            System.err.println("main - Error reading input: " + e.getMessage());
        }
    }

    /**
     * Reads the puzzle input file and parses it into a list of integers.
     * @param path Path to the input file
     * @return List of program instructions as integers
     */
    private static List<Integer> readInput(String path) throws IOException {
        String line = Files.readAllLines(Paths.get(path)).get(0);
        String[] tokens = line.split(",");
        List<Integer> program = new ArrayList<>();
        for (String token : tokens) {
            program.add(Integer.parseInt(token.trim()));
        }
        return program;
    }

    /**
     * Solves Part 1 by finding the maximum thruster signal using phase settings 0-4.
     * @param program The Intcode program
     * @return The maximum thruster signal
     */
    public static int solvePartOne(List<Integer> program) {
        int maxSignal = Integer.MIN_VALUE;
        List<Integer> phases = Arrays.asList(0, 1, 2, 3, 4);
        List<List<Integer>> permutations = generatePermutations(phases);

        for (List<Integer> phaseSequence : permutations) {
            int signal = runAmplifierChain(program, phaseSequence);
            maxSignal = Math.max(maxSignal, signal);
        }

        return maxSignal;
    }

    /**
     * Solves Part 2 by finding the maximum thruster signal using feedback loop mode with phase settings 5-9.
     * @param program The Intcode program
     * @return The maximum thruster signal with feedback
     */
    public static int solvePartTwo(List<Integer> program) {
        int maxSignal = Integer.MIN_VALUE;
        List<Integer> phases = Arrays.asList(5, 6, 7, 8, 9);
        List<List<Integer>> permutations = generatePermutations(phases);

        for (List<Integer> phaseSequence : permutations) {
            int signal = runAmplifierChainWithFeedback(program, phaseSequence);
            maxSignal = Math.max(maxSignal, signal);
        }

        return maxSignal;
    }

    /**
     * Runs the amplifier chain for a given phase sequence (Part 1 - no feedback).
     * @param program The Intcode program
     * @param phaseSequence The phase settings for each amplifier
     * @return The output signal from the last amplifier
     */
    private static int runAmplifierChain(List<Integer> program, List<Integer> phaseSequence) {
        int signal = 0;

        for (int i = 0; i < AMPLIFIER_COUNT; i++) {
            IntcodeComputer computer = new IntcodeComputer(program);
            computer.addInput(phaseSequence.get(i));
            computer.addInput(signal);
            computer.run();
            signal = computer.getLastOutput();
        }

        return signal;
    }

    /**
     * Runs the amplifier chain with feedback loop (Part 2).
     * Amplifiers run in a loop, feeding their output back to the first amplifier until they all halt.
     * @param program The Intcode program
     * @param phaseSequence The phase settings for each amplifier
     * @return The final output signal from amplifier E
     */
    private static int runAmplifierChainWithFeedback(List<Integer> program, List<Integer> phaseSequence) {
        IntcodeComputer[] amplifiers = new IntcodeComputer[AMPLIFIER_COUNT];

        // Initialise all amplifiers with their phase settings
        for (int i = 0; i < AMPLIFIER_COUNT; i++) {
            amplifiers[i] = new IntcodeComputer(program);
            amplifiers[i].addInput(phaseSequence.get(i));
        }

        int signal = 0;
        int currentAmplifier = 0;

        // Run amplifiers in feedback loop until all halt
        while (!amplifiers[AMPLIFIER_COUNT - 1].isHalted()) {
            IntcodeComputer computer = amplifiers[currentAmplifier];
            computer.addInput(signal);
            computer.runUntilOutput();

            if (computer.hasOutput()) {
                signal = computer.getLastOutput();
            }

            currentAmplifier = (currentAmplifier + 1) % AMPLIFIER_COUNT;
        }

        return signal;
    }

    /**
     * Generates all permutations of a list of integers.
     * @param list The list to generate permutations from
     * @return List of all permutations
     */
    private static List<List<Integer>> generatePermutations(List<Integer> list) {
        List<List<Integer>> result = new ArrayList<>();
        permute(new ArrayList<>(list), 0, result);
        return result;
    }

    /**
     * Recursive helper for generating permutations.
     * @param list The current list being permuted
     * @param start The starting index
     * @param result The accumulated permutations
     */
    private static void permute(List<Integer> list, int start, List<List<Integer>> result) {
        if (start == list.size()) {
            result.add(new ArrayList<>(list));
            return;
        }

        for (int i = start; i < list.size(); i++) {
            swap(list, i, start);
            permute(list, start + 1, result);
            swap(list, i, start);
        }
    }

    /**
     * Swaps two elements in a list.
     * @param list The list
     * @param i First index
     * @param j Second index
     */
    private static void swap(List<Integer> list, int i, int j) {
        int temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    /**
     * Intcode computer implementation for Day 7.
     * Supports parameter modes and input/output operations needed for the amplifier circuit.
     */
    static class IntcodeComputer {
        private final List<Integer> memory;
        private int pointer = 0;
        private final List<Integer> inputs = new ArrayList<>();
        private final List<Integer> outputs = new ArrayList<>();
        private boolean halted = false;

        public IntcodeComputer(List<Integer> program) {
            this.memory = new ArrayList<>(program);
        }

        public void addInput(int value) {
            inputs.add(value);
        }

        public int getLastOutput() {
            return outputs.get(outputs.size() - 1);
        }

        public boolean hasOutput() {
            return !outputs.isEmpty();
        }

        public boolean isHalted() {
            return halted;
        }

        /**
         * Runs the program until it halts.
         */
        public void run() {
            while (!halted) {
                executeInstruction();
            }
        }

        /**
         * Runs the program until it produces an output or halts.
         */
        public void runUntilOutput() {
            int initialOutputCount = outputs.size();
            while (!halted && outputs.size() == initialOutputCount) {
                executeInstruction();
            }
        }

        /**
         * Executes a single instruction.
         */
        private void executeInstruction() {
            if (halted) return;

            int opcode = memory.get(pointer) % 100;
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
                    if (inputs.isEmpty()) {
                        throw new IllegalStateException("executeInstruction - No input available");
                    }
                    setParam(1, modes[0], inputs.remove(0));
                    pointer += 2;
                    break;
                case 4: // Output
                    outputs.add(getParam(1, modes[0]));
                    pointer += 2;
                    break;
                case 5: // Jump-if-true
                    if (getParam(1, modes[0]) != 0) {
                        pointer = getParam(2, modes[1]);
                    } else {
                        pointer += 3;
                    }
                    break;
                case 6: // Jump-if-false
                    if (getParam(1, modes[0]) == 0) {
                        pointer = getParam(2, modes[1]);
                    } else {
                        pointer += 3;
                    }
                    break;
                case 7: // Less than
                    setParam(3, modes[2], getParam(1, modes[0]) < getParam(2, modes[1]) ? 1 : 0);
                    pointer += 4;
                    break;
                case 8: // Equals
                    setParam(3, modes[2], getParam(1, modes[0]) == getParam(2, modes[1]) ? 1 : 0);
                    pointer += 4;
                    break;
                case 99: // Halt
                    halted = true;
                    break;
                default:
                    throw new IllegalStateException("executeInstruction - Unknown opcode: " + opcode);
            }
        }

        /**
         * Extracts parameter modes from an instruction.
         * @param instruction The full instruction value
         * @return Array of modes for up to 3 parameters
         */
        private int[] getParameterModes(int instruction) {
            int[] modes = new int[3];
            modes[0] = (instruction / 100) % 10;
            modes[1] = (instruction / 1000) % 10;
            modes[2] = (instruction / 10000) % 10;
            return modes;
        }

        /**
         * Gets a parameter value based on its mode.
         * @param paramIdx Parameter index (1-based)
         * @param mode 0 for position mode, 1 for immediate mode
         * @return The parameter value
         */
        private int getParam(int paramIdx, int mode) {
            int value;
            if (mode == 0) { // Position mode
                value = memory.get(memory.get(pointer + paramIdx));
            } else if (mode == 1) { // Immediate mode
                value = memory.get(pointer + paramIdx);
            } else {
                throw new IllegalStateException("getParam - Unknown mode: " + mode);
            }
            return value;
        }

        /**
         * Sets a parameter value (always in position mode for writing).
         * @param paramIdx Parameter index (1-based)
         * @param mode Parameter mode (unused for writing, always position)
         * @param value The value to write
         */
        private void setParam(int paramIdx, int mode, int value) {
            int address = memory.get(pointer + paramIdx);
            memory.set(address, value);
        }
    }
}
