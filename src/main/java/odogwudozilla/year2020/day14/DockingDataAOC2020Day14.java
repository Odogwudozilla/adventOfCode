package odogwudozilla.year2020.day14;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2020 - Day 14: Docking Data
 * <p>
 * Puzzle URL: https://adventofcode.com/2020/day/14
 */
public final class DockingDataAOC2020Day14 {

    private static final String INPUT_FILE = "/2020/day14/day14_puzzle_data.txt";

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
        // Implements Part 1: Apply bitmask to values before writing to memory
        java.util.Map<Long, Long> memory = new java.util.HashMap<>();
        String mask = "";
        for (String line : input) {
            if (line.startsWith("mask = ")) {
                mask = line.substring(7);
            } else if (line.startsWith("mem[")) {
                int addrStart = line.indexOf('[') + 1;
                int addrEnd = line.indexOf(']');
                long address = Long.parseLong(line.substring(addrStart, addrEnd));
                long value = Long.parseLong(line.substring(line.indexOf('=') + 2));
                long maskedValue = applyMaskToValue(mask, value);
                memory.put(address, maskedValue);
            }
        }
        long sum = 0L;
        for (long v : memory.values()) {
            sum += v;
        }
        return String.valueOf(sum);
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        // Implements Part 2: Apply bitmask to memory addresses (floating bits)
        java.util.Map<Long, Long> memory = new java.util.HashMap<>();
        String mask = "";
        for (String line : input) {
            if (line.startsWith("mask = ")) {
                mask = line.substring(7);
            } else if (line.startsWith("mem[")) {
                int addrStart = line.indexOf('[') + 1;
                int addrEnd = line.indexOf(']');
                long address = Long.parseLong(line.substring(addrStart, addrEnd));
                long value = Long.parseLong(line.substring(line.indexOf('=') + 2));
                for (long floatingAddress : applyMaskToAddress(mask, address)) {
                    memory.put(floatingAddress, value);
                }
            }
        }
        long sum = 0L;
        for (long v : memory.values()) {
            sum += v;
        }
        return String.valueOf(sum);
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = DockingDataAOC2020Day14.class.getResourceAsStream(INPUT_FILE)) {
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

    /**
     * Applies the bitmask to a value (Part 1 logic).
     * @param mask the bitmask string
     * @param value the value to mask
     * @return the masked value
     */
    private static long applyMaskToValue(@org.jetbrains.annotations.NotNull String mask, long value) {
        long orMask = 0L;
        long andMask = 0L;
        for (int i = 0; i < mask.length(); i++) {
            char c = mask.charAt(i);
            orMask <<= 1;
            andMask <<= 1;
            if (c == '1') {
                orMask |= 1;
                andMask |= 1;
            } else if (c == '0') {
                // orMask |= 0;
                // andMask |= 0;
            } else if (c == 'X') {
                andMask |= 1;
            }
        }
        return (value & andMask) | orMask;
    }

    /**
     * Applies the bitmask to a memory address, generating all possible addresses (Part 2 logic).
     * @param mask the bitmask string
     * @param address the original address
     * @return list of all possible addresses after floating bits expansion
     */
    private static java.util.List<Long> applyMaskToAddress(@org.jetbrains.annotations.NotNull String mask, long address) {
        char[] masked = new char[36];
        for (int i = 0; i < 36; i++) {
            char m = mask.charAt(i);
            if (m == '0') {
                masked[i] = ((address >> (35 - i)) & 1) == 1 ? '1' : '0';
            } else if (m == '1') {
                masked[i] = '1';
            } else {
                masked[i] = 'X';
            }
        }
        java.util.List<Long> addresses = new java.util.ArrayList<>();
        expandFloatingBits(masked, 0, 0L, addresses);
        return addresses;
    }

    /**
     * Recursively expands floating bits ('X') in the masked address.
     * @param masked the char array of the masked address
     * @param pos current bit position
     * @param acc accumulated address value
     * @param addresses list to collect all possible addresses
     */
    private static void expandFloatingBits(char[] masked, int pos, long acc, java.util.List<Long> addresses) {
        if (pos == masked.length) {
            addresses.add(acc);
            return;
        }
        acc <<= 1;
        if (masked[pos] == 'X') {
            expandFloatingBits(masked, pos + 1, acc, addresses);
            expandFloatingBits(masked, pos + 1, acc | 1, addresses);
        } else if (masked[pos] == '1') {
            expandFloatingBits(masked, pos + 1, acc | 1, addresses);
        } else {
            expandFloatingBits(masked, pos + 1, acc, addresses);
        }
    }
}
