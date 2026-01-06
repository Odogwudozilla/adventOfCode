package odogwudozilla.year2019.day11;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Emergency Hull Painting Robot (Space Police)
 *
 * The puzzle involves building a robot that moves on a grid, painting panels black or white according to an Intcode program.
 * The robot starts on a black panel, facing up, and follows instructions to paint and turn. The goal for Part 1 is to determine
 * how many panels are painted at least once. Part 2 requires rendering the registration identifier painted by the robot.
 *
 * Official puzzle URL: https://adventofcode.com/2019/day/11
 *
 * @param args Command line arguments (not used)
 * @return None
 */
public class SpacePoliceAOC2019Day11 {
    private static final String INPUT_PATH = "src/main/resources/2019/day11/day11_puzzle_data.txt";

    public static void main(String[] args) throws Exception {
        List<Long> program = parseInput(INPUT_PATH);
        System.out.println("solvePartOne - Starting Part 1");
        int partOneResult = solvePartOne(program);
        System.out.println("solvePartOne - Panels painted at least once: " + partOneResult);
        System.out.println("solvePartTwo - Starting Part 2");
        String partTwoResult = solvePartTwo(program);
        System.out.println("solvePartTwo - Registration identifier:\n" + partTwoResult);
    }

    /**
     * Reads the Intcode program from the input file.
     * @param path Path to the input file
     * @return List of program instructions
     */
    private static List<Long> parseInput(String path) throws Exception {
        String line = Files.readAllLines(Paths.get(path)).get(0);
        List<Long> program = new ArrayList<>();
        for (String s : line.split(",")) {
            program.add(Long.parseLong(s.trim()));
        }
        return program;
    }

    /**
     * Solves Part 1: Counts the number of panels painted at least once.
     * @param program The Intcode program
     * @return Number of panels painted at least once
     */
    public static int solvePartOne(List<Long> program) {
        HullPaintingRobot robot = new HullPaintingRobot(program, 0); // Start on black panel
        robot.run();
        return robot.getPaintedPanelCount();
    }

    /**
     * Solves Part 2: Renders the registration identifier painted by the robot.
     * @param program The Intcode program
     * @return String representation of the painted registration identifier
     */
    public static String solvePartTwo(List<Long> program) {
        HullPaintingRobot robot = new HullPaintingRobot(program, 1); // Start on white panel
        robot.run();
        return robot.renderPaintedPanels();
    }

    /**
     * HullPaintingRobot simulates the robot's movement and painting logic.
     */
    static class HullPaintingRobot {
        private final IntcodeComputer computer;
        private final Map<Point, Integer> panels = new HashMap<>();
        private final Set<Point> painted = new HashSet<>();
        private Point position = new Point(0, 0);
        private int direction = 0; // 0=up, 1=right, 2=down, 3=left

        public HullPaintingRobot(List<Long> program, int initialPanelColour) {
            computer = new IntcodeComputer(program);
            panels.put(position, initialPanelColour);
        }

        public void run() {
            while (!computer.isHalted()) {
                int currentColour = panels.getOrDefault(position, 0);
                computer.addInput(currentColour);
                Integer paintColour = computer.runUntilOutput();
                Integer turnDirection = computer.runUntilOutput();
                if (paintColour == null || turnDirection == null) break;
                panels.put(position, paintColour);
                painted.add(position);
                turn(turnDirection);
                moveForward();
            }
        }

        private void turn(int turnDirection) {
            direction = (direction + (turnDirection == 0 ? 3 : 1)) % 4;
        }

        private void moveForward() {
            switch (direction) {
                case 0: position = new Point(position.x, position.y - 1); break; // Up
                case 1: position = new Point(position.x + 1, position.y); break; // Right
                case 2: position = new Point(position.x, position.y + 1); break; // Down
                case 3: position = new Point(position.x - 1, position.y); break; // Left
            }
        }

        public int getPaintedPanelCount() {
            return painted.size();
        }

        public String renderPaintedPanels() {
            int minX = panels.keySet().stream().mapToInt(p -> p.x).min().orElse(0);
            int maxX = panels.keySet().stream().mapToInt(p -> p.x).max().orElse(0);
            int minY = panels.keySet().stream().mapToInt(p -> p.y).min().orElse(0);
            int maxY = panels.keySet().stream().mapToInt(p -> p.y).max().orElse(0);
            StringBuilder sb = new StringBuilder();
            // Render from top (lowest y) to bottom (highest y)
            for (int y = minY; y <= maxY; y++) {
                for (int x = minX; x <= maxX; x++) {
                    int colour = panels.getOrDefault(new Point(x, y), 0);
                    sb.append(colour == 1 ? '#' : ' ');
                }
                sb.append('\n');
            }
            return sb.toString();
        }
    }

    /**
     * Simple Point class for grid coordinates.
     */
    static class Point {
        public final int x, y;
        public Point(int x, int y) { this.x = x; this.y = y; }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }
        @Override public int hashCode() { return Objects.hash(x, y); }
    }

    /**
     * IntcodeComputer simulates the Intcode program execution with input/output.
     */
    static class IntcodeComputer {
        private final Map<Long, Long> memory = new HashMap<>();
        private long pointer = 0;
        private long relativeBase = 0;
        private final Queue<Long> input = new LinkedList<>();
        private final Queue<Long> output = new LinkedList<>();
        private boolean halted = false;

        public IntcodeComputer(List<Long> program) {
            for (int i = 0; i < program.size(); i++) {
                memory.put((long) i, program.get(i));
            }
        }

        public void addInput(long value) {
            input.add(value);
        }

        public Integer runUntilOutput() {
            while (!halted) {
                long opcode = memory.getOrDefault(pointer, 0L) % 100;
                long modes = memory.getOrDefault(pointer, 0L) / 100;
                switch ((int) opcode) {
                    case 1: { // add
                        long a = getParam(1, modes);
                        long b = getParam(2, modes / 10);
                        setParam(3, modes / 100, a + b);
                        pointer += 4;
                        break;
                    }
                    case 2: { // multiply
                        long a = getParam(1, modes);
                        long b = getParam(2, modes / 10);
                        setParam(3, modes / 100, a * b);
                        pointer += 4;
                        break;
                    }
                    case 3: { // input
                        if (input.isEmpty()) return null;
                        setParam(1, modes, input.poll());
                        pointer += 2;
                        break;
                    }
                    case 4: { // output
                        long val = getParam(1, modes);
                        output.add(val);
                        pointer += 2;
                        return output.poll().intValue();
                    }
                    case 5: { // jump-if-true
                        long a = getParam(1, modes);
                        long b = getParam(2, modes / 10);
                        pointer = (a != 0) ? b : pointer + 3;
                        break;
                    }
                    case 6: { // jump-if-false
                        long a = getParam(1, modes);
                        long b = getParam(2, modes / 10);
                        pointer = (a == 0) ? b : pointer + 3;
                        break;
                    }
                    case 7: { // less than
                        long a = getParam(1, modes);
                        long b = getParam(2, modes / 10);
                        setParam(3, modes / 100, a < b ? 1 : 0);
                        pointer += 4;
                        break;
                    }
                    case 8: { // equals
                        long a = getParam(1, modes);
                        long b = getParam(2, modes / 10);
                        setParam(3, modes / 100, a == b ? 1 : 0);
                        pointer += 4;
                        break;
                    }
                    case 9: { // adjust relative base
                        long a = getParam(1, modes);
                        relativeBase += a;
                        pointer += 2;
                        break;
                    }
                    case 99:
                        halted = true;
                        return null;
                    default:
                        throw new RuntimeException("Unknown opcode: " + opcode);
                }
            }
            return null;
        }

        public boolean isHalted() {
            return halted;
        }

        private long getParam(int param, long modes) {
            int mode = (int) (modes % 10);
            long value;
            switch (mode) {
                case 0: value = memory.getOrDefault(memory.getOrDefault(pointer + param, 0L), 0L); break;
                case 1: value = memory.getOrDefault(pointer + param, 0L); break;
                case 2: value = memory.getOrDefault(memory.getOrDefault(pointer + param, 0L) + relativeBase, 0L); break;
                default: throw new RuntimeException("Unknown parameter mode: " + mode);
            }
            return value;
        }

        private void setParam(int param, long modes, long value) {
            int mode = (int) (modes % 10);
            long address;
            switch (mode) {
                case 0: address = memory.getOrDefault(pointer + param, 0L); break;
                case 2: address = memory.getOrDefault(pointer + param, 0L) + relativeBase; break;
                default: throw new RuntimeException("Unknown parameter mode for write: " + mode);
            }
            memory.put(address, value);
        }
    }
}
