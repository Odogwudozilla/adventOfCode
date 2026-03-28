package odogwudozilla.year2019.day21;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Springdroid Adventure (Advent of Code 2019 Day 21)
 * https://adventofcode.com/2019/day/21
 *
 * You lift off from Pluto and start flying in the direction of Santa.
 * While experimenting further with the tractor beam, you accidentally pull an asteroid directly into your ship! It deals significant damage to your hull and causes your ship to begin tumbling violently.
 * You can send a droid out to investigate, but the tumbling is causing enough artificial gravity that one wrong step could send the droid through a hole in the hull and flying out into space.
 * The clear choice for this mission is a droid that can jump over the holes in the hull - a springdroid.
 * You can use an Intcode program (your puzzle input) running on an ASCII-capable computer to program the springdroid. However, springdroids don't run Intcode; instead, they run a simplified assembly language called springscript.
 * While a springdroid is certainly capable of navigating the artificial gravity and giant holes, it has one downside: it can only remember at most 15 springscript instructions.
 * The springdroid will move forward automatically, constantly thinking about whether to jump. The springscript program defines the logic for this decision.
 * Springscript programs only use Boolean values, not numbers or strings. Two registers are available: T, the temporary value register, and J, the jump register. If the jump register is true at the end of the springscript program, the springdroid will try to jump. Both of these registers start with the value false.
 * Springdroids have a sensor that can detect whether there is ground at various distances in the direction it is facing; these values are provided in read-only registers. Your springdroid can detect ground at four distances: one tile away (A), two tiles away (B), three tiles away (C), and four tiles away (D). If there is ground at the given distance, the register will be true; if there is a hole, the register will be false.
 * There are only three instructions available in springscript: AND, OR, NOT. See the puzzle description for details.
 *
 * @author Advent of Code
 */
public class SpringdroidAdventureAOC2019Day21 {
    private static final String INPUT_PATH = "src/main/resources/2019/day21/day21_puzzle_data.txt";

    public static void main(String[] args) {
        List<Long> program = readInput(INPUT_PATH);
        long part1 = solvePartOne(program);
        System.out.println("Part 1: " + part1);
        long part2 = solvePartTwo(program);
        System.out.println("Part 2: " + part2);
    }

    /**
     * Reads the Intcode program from the input file.
     * @param path the path to the input file
     * @return the Intcode program as a list of longs
     */
    @NotNull
    private static List<Long> readInput(@NotNull String path) {
        try {
            String line = Files.readAllLines(Paths.get(path)).getFirst();
            String[] tokens = line.split(",");
            return java.util.Arrays.stream(tokens).map(Long::parseLong).toList();
        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Solves Part 1 of the puzzle.
     * @param program the Intcode program
     * @return the amount of hull damage reported
     */
    private static long solvePartOne(@NotNull List<Long> program) {
        String[] springscript = {
                "NOT A J",
                "NOT B T",
                "OR T J",
                "NOT C T",
                "OR T J",
                "AND D J",
                "WALK"
        };
        IntcodeComputer computer = new IntcodeComputer(program);
        for (String line : springscript) {
            for (char c : line.toCharArray()) {
                computer.addInput((long) c);
            }
            computer.addInput(10L); // newline
        }
        computer.run();
        List<Long> output = computer.getOutput();
        long last = output.get(output.size() - 1);
        // If last output is ASCII, print all as chars; else, return last as damage
        if (last < 128) {
            for (Long val : output) {
                System.out.print((char) val.intValue());
            }
            return -1;
        } else {
            return last;
        }
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param program the Intcode program
     * @return the amount of hull damage reported
     */
    private static long solvePartTwo(@NotNull List<Long> program) {
        String[] springscript = {
                // If any of A, B, or C is a hole, and D is ground, and (H is ground or E is ground)
                "NOT A J",
                "NOT B T",
                "OR T J",
                "NOT C T",
                "OR T J",
                "AND D J",
                "NOT H T",
                "NOT T T",
                "OR E T",
                "AND T J",
                "RUN"
        };
        IntcodeComputer computer = new IntcodeComputer(program);
        for (String line : springscript) {
            for (char c : line.toCharArray()) {
                computer.addInput((long) c);
            }
            computer.addInput(10L); // newline
        }
        computer.run();
        List<Long> output = computer.getOutput();
        long last = output.get(output.size() - 1);
        if (last < 128) {
            for (Long val : output) {
                System.out.print((char) val.intValue());
            }
            return -1;
        } else {
            return last;
        }
    }

    /**
     * Minimal Intcode computer implementation for this puzzle.
     */
    private static class IntcodeComputer {
        private final java.util.Map<Long, Long> mem = new java.util.HashMap<>();
        private long ip = 0, relBase = 0;
        private final java.util.Queue<Long> input = new java.util.ArrayDeque<>();
        private final java.util.List<Long> output = new java.util.ArrayList<>();
        private boolean halted = false;

        IntcodeComputer(List<Long> program) {
            for (int i = 0; i < program.size(); i++) mem.put((long) i, program.get(i));
        }
        void addInput(Long val) { input.add(val); }
        List<Long> getOutput() { return output; }
        boolean isHalted() { return halted; }
        private long get(long addr) { return mem.getOrDefault(addr, 0L); }
        private void set(long addr, long val) { mem.put(addr, val); }
        private void setParam(int i, long v, int[] mode) {
            long addr = get(ip + i + 1);
            if (mode[i] == 0) set(addr, v);
            else if (mode[i] == 2) set(relBase + addr, v);
            else throw new IllegalStateException();
        }
        void run() {
            while (!halted) {
                long instr = get(ip);
                int op = (int) (instr % 100);
                int[] mode = { (int) (instr / 100) % 10, (int) (instr / 1000) % 10, (int) (instr / 10000) % 10 };
                java.util.function.IntFunction<Long> param = i -> {
                    long val = get(ip + i + 1);
                    if (mode[i] == 0) return get(val);
                    if (mode[i] == 1) return val;
                    if (mode[i] == 2) return get(relBase + val);
                    throw new IllegalStateException();
                };
                switch (op) {
                    case 1: setParam(2, param.apply(0) + param.apply(1), mode); ip += 4; break;
                    case 2: setParam(2, param.apply(0) * param.apply(1), mode); ip += 4; break;
                    case 3: setParam(0, input.isEmpty() ? -1 : input.remove(), mode); ip += 2; break;
                    case 4: output.add(param.apply(0)); ip += 2; break;
                    case 5: ip = param.apply(0) != 0 ? param.apply(1) : ip + 3; break;
                    case 6: ip = param.apply(0) == 0 ? param.apply(1) : ip + 3; break;
                    case 7: setParam(2, param.apply(0) < param.apply(1) ? 1L : 0L, mode); ip += 4; break;
                    case 8: setParam(2, param.apply(0).equals(param.apply(1)) ? 1L : 0L, mode); ip += 4; break;
                    case 9: relBase += param.apply(0); ip += 2; break;
                    case 99: halted = true; break;
                    default: throw new IllegalStateException("Unknown opcode: " + op);
                }
            }
        }
    }
}
