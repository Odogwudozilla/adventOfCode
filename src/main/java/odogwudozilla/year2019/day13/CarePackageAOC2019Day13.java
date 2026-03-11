package odogwudozilla.year2019.day13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * --- Day 13: Care Package ---
 * As you ponder the solitude of space and the ever-increasing three-hour roundtrip for messages between you and Earth, you notice that the Space Mail Indicator Light is blinking. To help keep you sane, the Elves have sent you a care package.
 *
 * It's a new game for the ship's arcade cabinet! Unfortunately, the arcade is all the way on the other end of the ship. Surely, it won't be hard to build your own - the care package even comes with schematics.
 *
 * The arcade cabinet runs Intcode software like the game the Elves sent (your puzzle input). It has a primitive screen capable of drawing square tiles on a grid. The software draws tiles to the screen with output instructions: every three output instructions specify the x position (distance from the left), y position (distance from the top), and tile id. The tile id is interpreted as follows:
 *
 * 0 is an empty tile. No game object appears in this tile.
 * 1 is a wall tile. Walls are indestructible barriers.
 * 2 is a block tile. Blocks can be broken by the ball.
 * 3 is a horizontal paddle tile. The paddle is indestructible.
 * 4 is a ball tile. The ball moves diagonally and bounces off objects.
 *
 * Official puzzle URL: https://adventofcode.com/2019/day/13
 */
public class CarePackageAOC2019Day13 {
    private static final String INPUT_PATH = "src/main/resources/2019/day13/day13_puzzle_data.txt";

    public static void main(String[] args) throws IOException {
        List<Long> program = parseInput(INPUT_PATH);
        long partOneResult = solvePartOne(program);
        System.out.println("Part 1: Number of block tiles = " + partOneResult);
        long partTwoResult = solvePartTwo(program);
        System.out.println("Part 2: Final score after last block = " + partTwoResult);
    }

    /**
     * Reads the puzzle input file and parses it into a list of longs.
     * @param path the path to the input file
     * @return the parsed Intcode program
     * @throws IOException if file reading fails
     */
    private static List<Long> parseInput(String path) throws IOException {
        String line = Files.readAllLines(Paths.get(path)).get(0);
        String[] tokens = line.split(",");
        List<Long> program = new ArrayList<>();
        for (String token : tokens) {
            program.add(Long.parseLong(token.trim()));
        }
        return program;
    }

    /**
     * Runs the Intcode program and counts the number of block tiles (tile id 2) on the screen when the game exits.
     * @param program the Intcode program
     * @return the number of block tiles
     */
    public static long solvePartOne(List<Long> program) {
        IntcodeComputer computer = new IntcodeComputer(program);
        List<Long> output = computer.run();
        Map<String, Integer> screen = new HashMap<>();
        for (int i = 0; i < output.size(); i += 3) {
            long x = output.get(i);
            long y = output.get(i + 1);
            long tileId = output.get(i + 2);
            String key = x + "," + y;
            screen.put(key, (int) tileId);
        }
        return screen.values().stream().filter(id -> id == 2).count();
    }

    /**
     * Runs the Intcode program for Part 2, where the game is played in free play mode and the final score is reported.
     * @param program the Intcode program
     * @return the final score
     */
    public static long solvePartTwo(List<Long> program) {
        // Set memory address 0 to 2 for free play
        List<Long> programCopy = new ArrayList<>(program);
        programCopy.set(0, 2L);
        ArcadeGame game = new ArcadeGame(programCopy);
        return game.play();
    }

    /**
     * Simulates the arcade game for Part 2, handling paddle and ball movement and score tracking.
     */
    static class ArcadeGame {
        private final IntcodeComputer computer;
        private long score = 0;
        private long paddleX = 0;
        private long ballX = 0;

        public ArcadeGame(List<Long> program) {
            this.computer = new IntcodeComputer(program);
        }

        public long play() {
            while (!computer.isHalted()) {
                computer.runUntilInputOrHalt();
                List<Long> output = computer.drainOutput();
                for (int i = 0; i < output.size(); i += 3) {
                    long x = output.get(i);
                    long y = output.get(i + 1);
                    long value = output.get(i + 2);
                    if (x == -1 && y == 0) {
                        score = value;
                    } else if (value == 3) {
                        paddleX = x;
                    } else if (value == 4) {
                        ballX = x;
                    }
                }
                // Joystick: -1 left, 0 neutral, 1 right
                long joystick = Long.compare(ballX, paddleX);
                computer.addInput(joystick);
            }
            return score;
        }
    }

    /**
     * Minimal Intcode computer implementation for this puzzle.
     */
    static class IntcodeComputer {
        private final Map<Long, Long> memory = new HashMap<>();
        private long pointer = 0;
        private long relativeBase = 0;
        private final List<Long> output = new ArrayList<>();
        private int inputIndex = 0;
        private final List<Long> input = new ArrayList<>();
        private boolean halted = false;

        public IntcodeComputer(List<Long> program) {
            for (int i = 0; i < program.size(); i++) {
                memory.put((long) i, program.get(i));
            }
        }

        public List<Long> run() {
            while (true) {
                long opcode = memory.getOrDefault(pointer, 0L) % 100;
                long modes = memory.getOrDefault(pointer, 0L) / 100;
                if (opcode == 99) break;
                if (opcode == 1) { // add
                    set(3, modes / 100, get(1, modes % 10) + get(2, (modes / 10) % 10));
                    pointer += 4;
                } else if (opcode == 2) { // multiply
                    set(3, modes / 100, get(1, modes % 10) * get(2, (modes / 10) % 10));
                    pointer += 4;
                } else if (opcode == 3) { // input
                    set(1, modes % 10, inputIndex < input.size() ? input.get(inputIndex++) : 0L);
                    pointer += 2;
                } else if (opcode == 4) { // output
                    output.add(get(1, modes % 10));
                    pointer += 2;
                } else if (opcode == 5) { // jump-if-true
                    if (get(1, modes % 10) != 0) pointer = get(2, (modes / 10) % 10);
                    else pointer += 3;
                } else if (opcode == 6) { // jump-if-false
                    if (get(1, modes % 10) == 0) pointer = get(2, (modes / 10) % 10);
                    else pointer += 3;
                } else if (opcode == 7) { // less than
                    set(3, modes / 100, get(1, modes % 10) < get(2, (modes / 10) % 10) ? 1L : 0L);
                    pointer += 4;
                } else if (opcode == 8) { // equals
                    // Compare long values using == to avoid NullPointerException
                    set(3, modes / 100, get(1, modes % 10) == get(2, (modes / 10) % 10) ? 1L : 0L);
                    pointer += 4;
                } else if (opcode == 9) { // adjust relative base
                    relativeBase += get(1, modes % 10);
                    pointer += 2;
                } else {
                    throw new RuntimeException("Unknown opcode: " + opcode);
                }
            }
            return output;
        }

        private long get(int param, long mode) {
            long value = memory.getOrDefault(pointer + param, 0L);
            if (mode == 0) return memory.getOrDefault(value, 0L);
            if (mode == 1) return value;
            if (mode == 2) return memory.getOrDefault(relativeBase + value, 0L);
            throw new IllegalArgumentException("Unknown mode: " + mode);
        }

        private void set(int param, long mode, long value) {
            long addr = memory.getOrDefault(pointer + param, 0L);
            if (mode == 0) memory.put(addr, value);
            else if (mode == 2) memory.put(relativeBase + addr, value);
            else throw new IllegalArgumentException("Unknown mode for write: " + mode);
        }

        public boolean isHalted() {
            return halted;
        }

        public void addInput(long value) {
            input.add(value);
        }

        public void runUntilInputOrHalt() {
            while (true) {
                long opcode = memory.getOrDefault(pointer, 0L) % 100;
                long modes = memory.getOrDefault(pointer, 0L) / 100;
                if (opcode == 99) {
                    halted = true;
                    break;
                }
                if (opcode == 3 && inputIndex >= input.size()) {
                    // Wait for input
                    break;
                }
                if (opcode == 1) {
                    set(3, modes / 100, get(1, modes % 10) + get(2, (modes / 10) % 10));
                    pointer += 4;
                } else if (opcode == 2) {
                    set(3, modes / 100, get(1, modes % 10) * get(2, (modes / 10) % 10));
                    pointer += 4;
                } else if (opcode == 3) {
                    set(1, modes % 10, input.get(inputIndex++));
                    pointer += 2;
                } else if (opcode == 4) {
                    output.add(get(1, modes % 10));
                    pointer += 2;
                } else if (opcode == 5) {
                    if (get(1, modes % 10) != 0) pointer = get(2, (modes / 10) % 10);
                    else pointer += 3;
                } else if (opcode == 6) {
                    if (get(1, modes % 10) == 0) pointer = get(2, (modes / 10) % 10);
                    else pointer += 3;
                } else if (opcode == 7) {
                    set(3, modes / 100, get(1, modes % 10) < get(2, (modes / 10) % 10) ? 1L : 0L);
                    pointer += 4;
                } else if (opcode == 8) {
                    set(3, modes / 100, get(1, modes % 10) == get(2, (modes / 10) % 10) ? 1L : 0L);
                    pointer += 4;
                } else if (opcode == 9) {
                    relativeBase += get(1, modes % 10);
                    pointer += 2;
                } else {
                    throw new RuntimeException("Unknown opcode: " + opcode);
                }
            }
        }

        public List<Long> drainOutput() {
            List<Long> out = new ArrayList<>(output);
            output.clear();
            return out;
        }
    }
}
