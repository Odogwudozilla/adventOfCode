package odogwudozilla.year2019.day12;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Advent of Code 2019 - Day 12: The N-Body Problem
 * <p>
 * Simulates the gravitational motion of four moons in 3-D space.
 * Part 1 computes total system energy after 1000 steps.
 * Part 2 exploits per-axis independence to find the global cycle length via LCM.
 * <p>
 * Puzzle URL: https://adventofcode.com/2019/day/12
 */
public final class TheNBodyProblemAOC2019Day12 {

    private static final String INPUT_FILE = "/2019/day12/day12_puzzle_data.txt";
    private static final int SIMULATION_STEPS = 1_000;
    private static final int MOON_COUNT = 4;
    private static final int AXIS_COUNT = 3;
    private static final Pattern MOON_PATTERN =
            Pattern.compile("<x=(-?\\d+),\\s*y=(-?\\d+),\\s*z=(-?\\d+)>");

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
     * Simulates {@value SIMULATION_STEPS} steps of N-body gravity and returns the total
     * system energy (sum of potential energy * kinetic energy for each moon).
     * @param input list of input lines describing initial moon positions
     * @return the total system energy as a string
     */
    private static String solvePartOne(List<String> input) {
        int[][] pos = parsePositions(input);
        int[][] vel = new int[MOON_COUNT][AXIS_COUNT];

        for (int step = 0; step < SIMULATION_STEPS; step++) {
            applyGravity(pos, vel);
            applyVelocity(pos, vel);
        }

        int totalEnergy = 0;
        for (int i = 0; i < MOON_COUNT; i++) {
            int pot = Math.abs(pos[i][0]) + Math.abs(pos[i][1]) + Math.abs(pos[i][2]);
            int kin = Math.abs(vel[i][0]) + Math.abs(vel[i][1]) + Math.abs(vel[i][2]);
            totalEnergy += pot * kin;
        }
        return String.valueOf(totalEnergy);
    }

    /**
     * Finds the first step at which the simulation exactly repeats its initial state.
     * Because the x, y, and z axes are fully independent, the per-axis cycle lengths
     * are found separately and combined via LCM.
     * @param input list of input lines describing initial moon positions
     * @return the global cycle length as a string
     */
    private static String solvePartTwo(List<String> input) {
        int[][] initialPos = parsePositions(input);

        long[] cycleLengths = new long[AXIS_COUNT];
        for (int axis = 0; axis < AXIS_COUNT; axis++) {
            cycleLengths[axis] = findAxisCycleLength(initialPos, axis);
        }

        long result = lcm(lcm(cycleLengths[0], cycleLengths[1]), cycleLengths[2]);
        return String.valueOf(result);
    }

    /**
     * Simulates a single axis until its combined position+velocity state returns to the
     * initial configuration, then returns the number of steps taken.
     * @param initialPos the initial 3-D positions of all moons
     * @param axis the axis index (0=x, 1=y, 2=z)
     * @return the cycle length for this axis
     */
    private static long findAxisCycleLength(int[][] initialPos, int axis) {
        int[] pos = new int[MOON_COUNT];
        int[] vel = new int[MOON_COUNT];
        int[] initPos = new int[MOON_COUNT];

        for (int i = 0; i < MOON_COUNT; i++) {
            pos[i] = initialPos[i][axis];
            initPos[i] = initialPos[i][axis];
        }

        long steps = 0;
        do {
            // Apply gravity along this axis only
            for (int i = 0; i < MOON_COUNT; i++) {
                for (int j = i + 1; j < MOON_COUNT; j++) {
                    if (pos[i] < pos[j]) {
                        vel[i]++;
                        vel[j]--;
                    } else if (pos[i] > pos[j]) {
                        vel[i]--;
                        vel[j]++;
                    }
                }
            }
            // Apply velocity along this axis only
            for (int i = 0; i < MOON_COUNT; i++) {
                pos[i] += vel[i];
            }
            steps++;
        } while (!matchesInitialAxisState(pos, vel, initPos));

        return steps;
    }

    /**
     * Applies pairwise gravity between all moons, adjusting velocities.
     * @param pos current positions of all moons
     * @param vel current velocities of all moons (mutated in place)
     */
    private static void applyGravity(int[][] pos, int[][] vel) {
        for (int i = 0; i < MOON_COUNT; i++) {
            for (int j = i + 1; j < MOON_COUNT; j++) {
                for (int axis = 0; axis < AXIS_COUNT; axis++) {
                    if (pos[i][axis] < pos[j][axis]) {
                        vel[i][axis]++;
                        vel[j][axis]--;
                    } else if (pos[i][axis] > pos[j][axis]) {
                        vel[i][axis]--;
                        vel[j][axis]++;
                    }
                }
            }
        }
    }

    /**
     * Applies each moon's velocity to its position.
     * @param pos current positions of all moons (mutated in place)
     * @param vel current velocities of all moons
     */
    private static void applyVelocity(int[][] pos, int[][] vel) {
        for (int i = 0; i < MOON_COUNT; i++) {
            for (int axis = 0; axis < AXIS_COUNT; axis++) {
                pos[i][axis] += vel[i][axis];
            }
        }
    }

    /**
     * Returns true when a single-axis simulation state matches its initial configuration.
     * Velocities must all be zero (as they are at the start) and positions must match.
     * @param pos current axis positions
     * @param vel current axis velocities
     * @param initPos initial axis positions
     * @return true if the current state equals the initial state
     */
    private static boolean matchesInitialAxisState(int[] pos, int[] vel, int[] initPos) {
        for (int i = 0; i < MOON_COUNT; i++) {
            if (pos[i] != initPos[i] || vel[i] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Parses the four moon positions from the input lines.
     * @param input list of input lines in the format {@code <x=N, y=N, z=N>}
     * @return a 4x3 array of initial positions
     */
    private static int[][] parsePositions(List<String> input) {
        int[][] positions = new int[MOON_COUNT][AXIS_COUNT];
        for (int i = 0; i < MOON_COUNT; i++) {
            Matcher matcher = MOON_PATTERN.matcher(input.get(i));
            if (matcher.find()) {
                positions[i][0] = Integer.parseInt(matcher.group(1));
                positions[i][1] = Integer.parseInt(matcher.group(2));
                positions[i][2] = Integer.parseInt(matcher.group(3));
            }
        }
        return positions;
    }

    /**
     * Computes the least common multiple of two long values.
     * @param a first value
     * @param b second value
     * @return LCM of a and b
     */
    private static long lcm(long a, long b) {
        return a / gcd(a, b) * b;
    }

    /**
     * Computes the greatest common divisor of two long values using Euclid's algorithm.
     * @param a first value
     * @param b second value
     * @return GCD of a and b
     */
    private static long gcd(long a, long b) {
        while (b != 0) {
            long t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of non-blank input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = TheNBodyProblemAOC2019Day12.class.getResourceAsStream(INPUT_FILE)) {
            if (stream == null) {
                throw new IllegalStateException("Input file not found: " + INPUT_FILE);
            }
            Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8);
            List<String> lines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isBlank()) {
                    lines.add(line);
                }
            }
            return lines;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read input file", e);
        }
    }
}
