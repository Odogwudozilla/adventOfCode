package odogwudozilla.year2017.day13;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2017 - Day 13: Packet Scanners
 * <p>
 * A packet travels through a firewall made of layers, each with a security scanner
 * that oscillates back and forth. Part 1 calculates the total severity of a trip taken
 * immediately (sum of depth * range for each layer where the scanner is at position 0).
 * Part 2 finds the minimum delay so that the packet passes through without being caught
 * by any scanner.
 * <p>
 * Puzzle URL: <a href="https://adventofcode.com/2017/day/13">AoC 2017 Day 13</a>
 */
public final class PacketScannersAOC2017Day13 {

    private static final String INPUT_FILE = "/2017/day13/day13_puzzle_data.txt";

    /**
     * Entry point. Reads puzzle input and prints solutions for Part 1 and Part 2.
     *
     * @param args command line arguments (not used)
     */
    public static void main(final String[] args) {
        final List<String> input = readInput();
        System.out.println("Part 1: " + solvePartOne(input));
        System.out.println("Part 2: " + solvePartTwo(input));
    }

    /**
     * Solves Part 1: calculates the total severity of travelling through the firewall
     * with no delay. A packet is caught at layer (depth, range) if
     * {@code depth % (2 * (range - 1)) == 0}.  Severity for that layer is
     * {@code depth * range}.
     *
     * @param input list of input lines, each in the form "depth: range"
     * @return the total trip severity as a string
     */
    private static String solvePartOne(final List<String> input) {
        int totalSeverity = 0;
        for (final String line : input) {
            final int[] layer = parseLine(line);
            final int depth = layer[0];
            final int range = layer[1];
            final int period = 2 * (range - 1);
            if (depth % period == 0) {
                totalSeverity += depth * range;
            }
        }
        return String.valueOf(totalSeverity);
    }

    /**
     * Solves Part 2: finds the minimum delay (in picoseconds) before entering the
     * firewall so that the packet is never caught by any scanner.  A packet with
     * delay {@code d} is caught at layer (depth, range) if
     * {@code (depth + d) % (2 * (range - 1)) == 0}.
     *
     * @param input list of input lines, each in the form "depth: range"
     * @return the minimum safe delay as a string
     */
    private static String solvePartTwo(final List<String> input) {
        final int[][] layers = new int[input.size()][2];
        for (int i = 0; i < input.size(); i++) {
            layers[i] = parseLine(input.get(i));
        }

        for (int delay = 0; ; delay++) {
            if (!isCaught(layers, delay)) {
                return String.valueOf(delay);
            }
        }
    }

    /**
     * Determines whether a packet delayed by {@code delay} picoseconds is caught
     * by any scanner in the firewall.
     *
     * @param layers 2-D array of {depth, range} pairs
     * @param delay  the number of picoseconds to wait before entering
     * @return {@code true} if caught by at least one scanner, {@code false} otherwise
     */
    private static boolean isCaught(final int[][] layers, final int delay) {
        for (final int[] layer : layers) {
            final int depth = layer[0];
            final int range = layer[1];
            final int period = 2 * (range - 1);
            if ((depth + delay) % period == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Parses a single input line of the form "depth: range".
     *
     * @param line the input line to parse
     * @return an int array where index 0 is depth and index 1 is range
     */
    private static int[] parseLine(final String line) {
        final String[] parts = line.split(": ");
        return new int[]{
            Integer.parseInt(parts[0].trim()),
            Integer.parseInt(parts[1].trim())
        };
    }

    /**
     * Reads the puzzle input file from the classpath.
     *
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = PacketScannersAOC2017Day13.class.getResourceAsStream(INPUT_FILE)) {
            if (stream == null) {
                throw new IllegalStateException("Input file not found: " + INPUT_FILE);
            }
            final Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8);
            final List<String> lines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            return lines;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read input file", e);
        }
    }
}
