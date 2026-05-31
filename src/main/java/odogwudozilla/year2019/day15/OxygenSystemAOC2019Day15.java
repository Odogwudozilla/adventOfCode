package odogwudozilla.year2019.day15;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2019 - Day 15: Oxygen System
 * <p>
 * Puzzle URL: https://adventofcode.com/2019/day/15
 */
public final class OxygenSystemAOC2019Day15 {

    private static final String INPUT_FILE = "/2019/day15/day15_puzzle_data.txt";

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
     * Intcode computer implementation for simulating the droid's program.
     */
    private static class IntcodeComputer implements Cloneable {
        private long[] memory;
        private int pointer;
        private int relativeBase;
        private boolean halted;
        private int inputPointer;
        private long[] inputBuffer;
        private long output;
        private boolean hasOutput;

        public IntcodeComputer(long[] program) {
            this.memory = new long[4096];
            System.arraycopy(program, 0, memory, 0, program.length);
            this.pointer = 0;
            this.relativeBase = 0;
            this.halted = false;
            this.inputPointer = 0;
            this.inputBuffer = new long[0];
            this.hasOutput = false;
        }

        public void setInput(long[] input) {
            this.inputBuffer = input;
            this.inputPointer = 0;
        }

        public boolean isHalted() {
            return halted;
        }

        public long getOutput() {
            hasOutput = false;
            return output;
        }

        public boolean hasOutput() {
            return hasOutput;
        }

        public IntcodeComputer clone() {
            try {
                IntcodeComputer copy = (IntcodeComputer) super.clone();
                copy.memory = this.memory.clone();
                copy.inputBuffer = this.inputBuffer.clone();
                return copy;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }

        public void runUntilOutput() {
            while (!halted) {
                int opcode = (int) (memory[pointer] % 100);
                int[] modes = new int[3];
                long instr = memory[pointer];
                for (int i = 0; i < 3; i++) {
                    modes[i] = (int) (instr / (int) Math.pow(10, i + 2) % 10);
                }
                if (opcode == 99) {
                    halted = true;
                    return;
                }
                long a = getParam(pointer + 1, modes[0]);
                long b = getParam(pointer + 2, modes[1]);
                int dest = getDest(pointer + 3, modes[2]);
                switch (opcode) {
                    case 1: // add
                        memory[dest] = a + b;
                        pointer += 4;
                        break;
                    case 2: // multiply
                        memory[dest] = a * b;
                        pointer += 4;
                        break;
                    case 3: // input
                        int inDest = getDest(pointer + 1, modes[0]);
                        if (inputPointer >= inputBuffer.length) return; // wait for input
                        memory[inDest] = inputBuffer[inputPointer++];
                        pointer += 2;
                        break;
                    case 4: // output
                        output = a;
                        hasOutput = true;
                        pointer += 2;
                        return;
                    case 5: // jump-if-true
                        pointer = (a != 0) ? (int) b : pointer + 3;
                        break;
                    case 6: // jump-if-false
                        pointer = (a == 0) ? (int) b : pointer + 3;
                        break;
                    case 7: // less than
                        memory[dest] = (a < b) ? 1 : 0;
                        pointer += 4;
                        break;
                    case 8: // equals
                        memory[dest] = (a == b) ? 1 : 0;
                        pointer += 4;
                        break;
                    case 9: // adjust relative base
                        relativeBase += (int) a;
                        pointer += 2;
                        break;
                    default:
                        throw new IllegalStateException("Unknown opcode: " + opcode);
                }
            }
        }

        private long getParam(int pos, int mode) {
            if (mode == 0) return memory[(int) memory[pos]];
            if (mode == 1) return memory[pos];
            if (mode == 2) return memory[(int) memory[pos] + relativeBase];
            throw new IllegalArgumentException("Unknown mode: " + mode);
        }

        private int getDest(int pos, int mode) {
            if (mode == 0) return (int) memory[pos];
            if (mode == 2) return (int) memory[pos] + relativeBase;
            throw new IllegalArgumentException("Unknown dest mode: " + mode);
        }
    }

    private static class Point {
        final int x, y;
        Point(int x, int y) { this.x = x; this.y = y; }
        public boolean equals(Object o) {
            if (!(o instanceof Point)) return false;
            Point p = (Point) o;
            return x == p.x && y == p.y;
        }
        public int hashCode() { return 31 * x + y; }
    }

    private enum TileType { WALL, EMPTY, OXYGEN }

    private static final int[] DX = {0, 0, -1, 1}; // N, S, W, E
    private static final int[] DY = {-1, 1, 0, 0};
    private static final int[] DIRS = {1, 2, 3, 4}; // N, S, W, E

    private static class State {
        final Point pos;
        final IntcodeComputer comp;
        final int steps;
        State(Point pos, IntcodeComputer comp, int steps) {
            this.pos = pos;
            this.comp = comp;
            this.steps = steps;
        }
    }

    /**
     * Parses a single line of Intcode program into a long array.
     */
    private static long[] parseIntcode(String line) {
        String[] parts = line.split(",");
        long[] code = new long[parts.length];
        for (int i = 0; i < parts.length; i++) code[i] = Long.parseLong(parts[i]);
        return code;
    }

    /**
     * Solves Part 1 of the puzzle.
     * @param input list of input lines
     * @return the Part 1 answer
     */
    public static String solvePartOne(List<String> input) {
        long[] program = parseIntcode(input.get(0));
        java.util.Map<Point, TileType> map = new java.util.HashMap<>();
        java.util.Set<Point> visited = new java.util.HashSet<>();
        java.util.Queue<State> queue = new java.util.ArrayDeque<>();
        Point start = new Point(0, 0);
        queue.add(new State(start, new IntcodeComputer(program), 0));
        visited.add(start);
        while (!queue.isEmpty()) {
            State state = queue.poll();
            for (int d = 0; d < 4; d++) {
                int nx = state.pos.x + DX[d];
                int ny = state.pos.y + DY[d];
                Point np = new Point(nx, ny);
                if (visited.contains(np)) continue;
                IntcodeComputer comp = state.comp.clone();
                comp.setInput(new long[]{DIRS[d]});
                comp.runUntilOutput();
                long status = comp.getOutput();
                if (status == 0) {
                    map.put(np, TileType.WALL);
                    continue;
                }
                if (status == 1) {
                    map.put(np, TileType.EMPTY);
                    visited.add(np);
                    queue.add(new State(np, comp, state.steps + 1));
                }
                if (status == 2) {
                    map.put(np, TileType.OXYGEN);
                    return String.valueOf(state.steps + 1);
                }
            }
        }
        return "not found";
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    public static String solvePartTwo(List<String> input) {
        long[] program = parseIntcode(input.get(0));
        // First, map the entire maze and find the oxygen system location
        java.util.Map<Point, TileType> map = new java.util.HashMap<>();
        java.util.Set<Point> visited = new java.util.HashSet<>();
        java.util.Queue<State> queue = new java.util.ArrayDeque<>();
        Point start = new Point(0, 0);
        queue.add(new State(start, new IntcodeComputer(program), 0));
        visited.add(start);
        Point oxygen = null;
        while (!queue.isEmpty()) {
            State state = queue.poll();
            for (int d = 0; d < 4; d++) {
                int nx = state.pos.x + DX[d];
                int ny = state.pos.y + DY[d];
                Point np = new Point(nx, ny);
                if (visited.contains(np)) continue;
                IntcodeComputer comp = state.comp.clone();
                comp.setInput(new long[]{DIRS[d]});
                comp.runUntilOutput();
                long status = comp.getOutput();
                if (status == 0) {
                    map.put(np, TileType.WALL);
                    continue;
                }
                if (status == 1) {
                    map.put(np, TileType.EMPTY);
                    visited.add(np);
                    queue.add(new State(np, comp, state.steps + 1));
                }
                if (status == 2) {
                    map.put(np, TileType.OXYGEN);
                    oxygen = np;
                    visited.add(np);
                    queue.add(new State(np, comp, state.steps + 1));
                }
            }
        }
        // Now, BFS from oxygen to fill the area
        java.util.Set<Point> filled = new java.util.HashSet<>();
        java.util.Queue<Point> q = new java.util.ArrayDeque<>();
        q.add(oxygen);
        filled.add(oxygen);
        int minutes = 0;
        while (true) {
            java.util.List<Point> next = new java.util.ArrayList<>();
            while (!q.isEmpty()) {
                Point p = q.poll();
                for (int d = 0; d < 4; d++) {
                    int nx = p.x + DX[d];
                    int ny = p.y + DY[d];
                    Point np = new Point(nx, ny);
                    if (filled.contains(np)) continue;
                    TileType t = map.get(np);
                    if (t == TileType.EMPTY || t == TileType.OXYGEN) {
                        filled.add(np);
                        next.add(np);
                    }
                }
            }
            if (next.isEmpty()) break;
            q.addAll(next);
            minutes++;
        }
        return String.valueOf(minutes);
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = OxygenSystemAOC2019Day15.class.getResourceAsStream(INPUT_FILE)) {
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
