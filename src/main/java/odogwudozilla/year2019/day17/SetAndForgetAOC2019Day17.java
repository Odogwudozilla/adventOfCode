package odogwudozilla.year2019.day17;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Advent of Code 2019 - Day 17: Set and Forget
 * <p>
 * Puzzle URL: https://adventofcode.com/2019/day/17
 */
public final class SetAndForgetAOC2019Day17 {

    private static final String INPUT_FILE = "/2019/day17/day17_puzzle_data.txt";
    private static final char SCAFFOLD = '#';
    private static final char ROBOT_UP = '^';
    private static final char ROBOT_DOWN = 'v';
    private static final char ROBOT_LEFT = '<';
    private static final char ROBOT_RIGHT = '>';
    private static final char ROBOT_TUMBLE = 'X';
    private static final long ASCII_NEWLINE = 10L;
    private static final long OPCODE_ADD = 1L;
    private static final long OPCODE_MULTIPLY = 2L;
    private static final long OPCODE_INPUT = 3L;
    private static final long OPCODE_OUTPUT = 4L;
    private static final long OPCODE_JUMP_IF_TRUE = 5L;
    private static final long OPCODE_JUMP_IF_FALSE = 6L;
    private static final long OPCODE_LESS_THAN = 7L;
    private static final long OPCODE_EQUALS = 8L;
    private static final long OPCODE_ADJUST_RELATIVE_BASE = 9L;
    private static final long OPCODE_HALT = 99L;
    private static final long MODE_POSITION = 0L;
    private static final long MODE_IMMEDIATE = 1L;
    private static final long MODE_RELATIVE = 2L;
    private static final int ROUTINE_MAX_LENGTH = 20;
    private static final long WAKE_UP_VALUE = 2L;
    private static final long ASCII_MAX_VALUE = 255L;
    private static final int DIRECTION_UP = 0;
    private static final int DIRECTION_RIGHT = 1;
    private static final int DIRECTION_DOWN = 2;
    private static final int DIRECTION_LEFT = 3;
    private static final int[] DIRECTION_X = {0, 1, 0, -1};
    private static final int[] DIRECTION_Y = {-1, 0, 1, 0};

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
        List<Long> program = parseProgram(input.get(0));
        List<Long> asciiOutput = runIntcodeToAscii(program);
        List<String> grid = buildGrid(asciiOutput);
        return Long.toString(sumAlignmentParameters(grid));
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        List<Long> program = parseProgram(input.get(0));
        List<String> grid = buildGrid(runIntcodeToAscii(program));
        RobotState robotState = findRobot(grid);
        List<String> tokens = buildFullPathTokens(grid, robotState);
        CompressionPlan compressionPlan = compressToRoutines(tokens);

        List<Long> movementProgram = new ArrayList<>(program);
        movementProgram.set(0, WAKE_UP_VALUE);
        List<Long> scriptInput = encodeAsciiScript(compressionPlan, false);
        List<Long> output = runIntcodeWithInput(movementProgram, scriptInput);

        long dustAmount = 0L;
        for (long value : output) {
            if (value > ASCII_MAX_VALUE) {
                dustAmount = value;
            }
        }
        return Long.toString(dustAmount);
    }

    private static List<Long> parseProgram(String line) {
        String[] tokens = line.split(",");
        List<Long> program = new ArrayList<>(tokens.length);
        for (String token : tokens) {
            program.add(Long.parseLong(token.trim()));
        }
        return program;
    }

    private static List<Long> runIntcodeToAscii(List<Long> program) {
        IntcodeComputer computer = new IntcodeComputer(program, Collections.emptyList());
        return computer.run();
    }

    private static List<Long> runIntcodeWithInput(List<Long> program, List<Long> input) {
        IntcodeComputer computer = new IntcodeComputer(program, input);
        return computer.run();
    }

    private static List<String> buildGrid(List<Long> output) {
        List<String> rows = new ArrayList<>();
        StringBuilder currentRow = new StringBuilder();

        for (long value : output) {
            if (value == ASCII_NEWLINE) {
                if (currentRow.length() > 0) {
                    rows.add(currentRow.toString());
                    currentRow.setLength(0);
                }
            } else {
                currentRow.append((char) value);
            }
        }

        if (currentRow.length() > 0) {
            rows.add(currentRow.toString());
        }
        return rows;
    }

    private static long sumAlignmentParameters(List<String> grid) {
        long alignmentSum = 0L;

        for (int y = 1; y < grid.size() - 1; y++) {
            for (int x = 1; x < grid.get(y).length() - 1; x++) {
                if (isScaffoldAt(grid, x, y)
                        && isScaffoldAt(grid, x, y - 1)
                        && isScaffoldAt(grid, x, y + 1)
                        && isScaffoldAt(grid, x - 1, y)
                        && isScaffoldAt(grid, x + 1, y)) {
                    alignmentSum += (long) x * y;
                }
            }
        }

        return alignmentSum;
    }

    private static boolean isScaffoldAt(List<String> grid, int x, int y) {
        if (y < 0 || y >= grid.size()) {
            return false;
        }
        String row = grid.get(y);
        if (x < 0 || x >= row.length()) {
            return false;
        }
        return isScaffold(row.charAt(x));
    }

    private static boolean isScaffold(char c) {
        return c == SCAFFOLD
                || c == ROBOT_UP
                || c == ROBOT_DOWN
                || c == ROBOT_LEFT
                || c == ROBOT_RIGHT
                || c == ROBOT_TUMBLE;
    }

    private static RobotState findRobot(List<String> grid) {
        for (int y = 0; y < grid.size(); y++) {
            String row = grid.get(y);
            for (int x = 0; x < row.length(); x++) {
                char c = row.charAt(x);
                int direction = directionFromRobotGlyph(c);
                if (direction >= 0) {
                    return new RobotState(new Point(x, y), direction);
                }
            }
        }
        throw new IllegalStateException("Robot not found in scaffold map");
    }

    private static int directionFromRobotGlyph(char glyph) {
        if (glyph == ROBOT_UP) {
            return DIRECTION_UP;
        }
        if (glyph == ROBOT_RIGHT) {
            return DIRECTION_RIGHT;
        }
        if (glyph == ROBOT_DOWN) {
            return DIRECTION_DOWN;
        }
        if (glyph == ROBOT_LEFT) {
            return DIRECTION_LEFT;
        }
        return -1;
    }

    private static List<String> buildFullPathTokens(List<String> grid, RobotState start) {
        List<String> tokens = new ArrayList<>();
        int x = start.position.x;
        int y = start.position.y;
        int direction = start.direction;

        while (true) {
            int leftDirection = (direction + 3) % 4;
            int rightDirection = (direction + 1) % 4;
            boolean canTurnLeft = isScaffoldAt(grid, x + DIRECTION_X[leftDirection], y + DIRECTION_Y[leftDirection]);
            boolean canTurnRight = isScaffoldAt(grid, x + DIRECTION_X[rightDirection], y + DIRECTION_Y[rightDirection]);

            if (!canTurnLeft && !canTurnRight) {
                break;
            }

            if (canTurnLeft) {
                tokens.add("L");
                direction = leftDirection;
            } else {
                tokens.add("R");
                direction = rightDirection;
            }

            int steps = 0;
            while (isScaffoldAt(grid, x + DIRECTION_X[direction], y + DIRECTION_Y[direction])) {
                x += DIRECTION_X[direction];
                y += DIRECTION_Y[direction];
                steps++;
            }
            tokens.add(Integer.toString(steps));
        }

        return tokens;
    }

    private static CompressionPlan compressToRoutines(List<String> tokens) {
        CompressionPlan plan = compressRecursive(tokens, 0, new ArrayList<>(), new LinkedHashMap<>());
        if (plan == null) {
            throw new IllegalStateException("Unable to compress movement path into A/B/C routines");
        }
        return plan;
    }

    private static CompressionPlan compressRecursive(List<String> tokens,
                                                    int index,
                                                    List<String> mainRoutine,
                                                    Map<Character, List<String>> functions) {
        if (encodedLength(mainRoutine) > ROUTINE_MAX_LENGTH) {
            return null;
        }

        if (index == tokens.size()) {
            return new CompressionPlan(new ArrayList<>(mainRoutine), new LinkedHashMap<>(functions));
        }

        CompressionPlan bestPlan = null;
        List<Map.Entry<Character, List<String>>> definedFunctions = new ArrayList<>(functions.entrySet());
        for (Map.Entry<Character, List<String>> entry : definedFunctions) {
            List<String> candidate = entry.getValue();
            if (matchesAt(tokens, index, candidate)) {
                mainRoutine.add(Character.toString(entry.getKey()));
                CompressionPlan resolved = compressRecursive(tokens, index + candidate.size(), mainRoutine, functions);
                bestPlan = chooseBetterPlan(bestPlan, resolved);
                mainRoutine.remove(mainRoutine.size() - 1);
            }
        }

        if (functions.size() == 3) {
            return bestPlan;
        }

        char nextFunctionName = (char) ('A' + functions.size());
        for (int end = tokens.size(); end >= index + 2; end -= 2) {
            List<String> candidate = new ArrayList<>(tokens.subList(index, end));
            if (encodedLength(candidate) > ROUTINE_MAX_LENGTH) {
                continue;
            }

            functions.put(nextFunctionName, candidate);
            mainRoutine.add(Character.toString(nextFunctionName));
            CompressionPlan resolved = compressRecursive(tokens, end, mainRoutine, functions);
            bestPlan = chooseBetterPlan(bestPlan, resolved);
            mainRoutine.remove(mainRoutine.size() - 1);
            functions.remove(nextFunctionName);
        }

        return bestPlan;
    }

    private static CompressionPlan chooseBetterPlan(CompressionPlan currentBest, CompressionPlan candidate) {
        if (candidate == null) {
            return currentBest;
        }
        if (currentBest == null) {
            return candidate;
        }

        int currentSingleUseCount = countSingleUseFunctions(currentBest);
        int candidateSingleUseCount = countSingleUseFunctions(candidate);
        if (candidateSingleUseCount < currentSingleUseCount) {
            return candidate;
        }
        if (candidateSingleUseCount > currentSingleUseCount) {
            return currentBest;
        }

        int currentMainLength = encodedLength(currentBest.mainRoutine);
        int candidateMainLength = encodedLength(candidate.mainRoutine);
        if (candidateMainLength < currentMainLength) {
            return candidate;
        }
        if (candidateMainLength > currentMainLength) {
            return currentBest;
        }

        if (candidate.mainRoutine.size() < currentBest.mainRoutine.size()) {
            return candidate;
        }
        if (candidate.mainRoutine.size() > currentBest.mainRoutine.size()) {
            return currentBest;
        }

        String currentMain = String.join(",", currentBest.mainRoutine);
        String candidateMain = String.join(",", candidate.mainRoutine);
        if (candidateMain.compareTo(currentMain) < 0) {
            return candidate;
        }

        return currentBest;
    }

    private static int countSingleUseFunctions(CompressionPlan plan) {
        Map<Character, Integer> usageCount = new HashMap<>();
        for (String token : plan.mainRoutine) {
            char functionName = token.charAt(0);
            usageCount.put(functionName, usageCount.getOrDefault(functionName, 0) + 1);
        }

        int singleUse = 0;
        for (Character functionName : plan.functions.keySet()) {
            if (usageCount.getOrDefault(functionName, 0) <= 1) {
                singleUse++;
            }
        }
        return singleUse;
    }

    private static boolean matchesAt(List<String> tokens, int index, List<String> pattern) {
        if (index + pattern.size() > tokens.size()) {
            return false;
        }
        for (int i = 0; i < pattern.size(); i++) {
            if (!tokens.get(index + i).equals(pattern.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static int encodedLength(List<String> tokens) {
        if (tokens.isEmpty()) {
            return 0;
        }
        int length = 0;
        for (String token : tokens) {
            length += token.length();
        }
        return length + (tokens.size() - 1);
    }

    private static List<Long> encodeAsciiScript(CompressionPlan plan, boolean videoFeed) {
        StringBuilder script = new StringBuilder();
        appendLine(script, String.join(",", plan.mainRoutine));
        appendLine(script, String.join(",", plan.functions.getOrDefault('A', Collections.emptyList())));
        appendLine(script, String.join(",", plan.functions.getOrDefault('B', Collections.emptyList())));
        appendLine(script, String.join(",", plan.functions.getOrDefault('C', Collections.emptyList())));
        appendLine(script, videoFeed ? "y" : "n");

        List<Long> encoded = new ArrayList<>(script.length());
        for (char c : script.toString().toCharArray()) {
            encoded.add((long) c);
        }
        return encoded;
    }

    private static void appendLine(StringBuilder builder, String line) {
        builder.append(line).append('\n');
    }

    private static final class Point {
        private final int x;
        private final int y;

        private Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static final class RobotState {
        private final Point position;
        private final int direction;

        private RobotState(Point position, int direction) {
            this.position = position;
            this.direction = direction;
        }
    }

    private static final class CompressionPlan {
        private final List<String> mainRoutine;
        private final Map<Character, List<String>> functions;

        private CompressionPlan(List<String> mainRoutine, Map<Character, List<String>> functions) {
            this.mainRoutine = mainRoutine;
            this.functions = functions;
        }
    }

    private static final class IntcodeComputer {
        private final Map<Long, Long> memory = new HashMap<>();
        private final List<Long> inputs;
        private final List<Long> outputs = new ArrayList<>();
        private int inputIndex;
        private long pointer;
        private long relativeBase;

        private IntcodeComputer(List<Long> program, List<Long> inputs) {
            for (int i = 0; i < program.size(); i++) {
                memory.put((long) i, program.get(i));
            }
            this.inputs = new ArrayList<>(inputs);
        }

        private List<Long> run() {
            while (true) {
                long instruction = read(pointer);
                long opcode = instruction % 100;
                long mode1 = (instruction / 100) % 10;
                long mode2 = (instruction / 1000) % 10;
                long mode3 = (instruction / 10000) % 10;

                if (opcode == OPCODE_HALT) {
                    return outputs;
                }

                if (opcode == OPCODE_ADD) {
                    write(3, mode3, readParam(1, mode1) + readParam(2, mode2));
                    pointer += 4;
                } else if (opcode == OPCODE_MULTIPLY) {
                    write(3, mode3, readParam(1, mode1) * readParam(2, mode2));
                    pointer += 4;
                } else if (opcode == OPCODE_INPUT) {
                    if (inputIndex >= inputs.size()) {
                        throw new IllegalStateException("Input opcode encountered without available input value");
                    }
                    write(1, mode1, inputs.get(inputIndex++));
                    pointer += 2;
                } else if (opcode == OPCODE_OUTPUT) {
                    outputs.add(readParam(1, mode1));
                    pointer += 2;
                } else if (opcode == OPCODE_JUMP_IF_TRUE) {
                    if (readParam(1, mode1) != 0) {
                        pointer = readParam(2, mode2);
                    } else {
                        pointer += 3;
                    }
                } else if (opcode == OPCODE_JUMP_IF_FALSE) {
                    if (readParam(1, mode1) == 0) {
                        pointer = readParam(2, mode2);
                    } else {
                        pointer += 3;
                    }
                } else if (opcode == OPCODE_LESS_THAN) {
                    write(3, mode3, readParam(1, mode1) < readParam(2, mode2) ? 1L : 0L);
                    pointer += 4;
                } else if (opcode == OPCODE_EQUALS) {
                    write(3, mode3, readParam(1, mode1) == readParam(2, mode2) ? 1L : 0L);
                    pointer += 4;
                } else if (opcode == OPCODE_ADJUST_RELATIVE_BASE) {
                    relativeBase += readParam(1, mode1);
                    pointer += 2;
                } else {
                    throw new IllegalStateException("Unknown opcode: " + opcode);
                }
            }
        }

        private long readParam(int offset, long mode) {
            long value = read(pointer + offset);
            if (mode == MODE_POSITION) {
                return read(value);
            }
            if (mode == MODE_IMMEDIATE) {
                return value;
            }
            if (mode == MODE_RELATIVE) {
                return read(relativeBase + value);
            }
            throw new IllegalStateException("Invalid read mode: " + mode);
        }

        private void write(int offset, long mode, long value) {
            long address = read(pointer + offset);
            if (mode == MODE_RELATIVE) {
                address += relativeBase;
            } else if (mode != MODE_POSITION) {
                throw new IllegalStateException("Invalid write mode: " + mode);
            }
            memory.put(address, value);
        }

        private long read(long address) {
            return memory.getOrDefault(address, 0L);
        }
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = SetAndForgetAOC2019Day17.class.getResourceAsStream(INPUT_FILE)) {
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
}
