package odogwudozilla.year2018.day8;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import org.jetbrains.annotations.NotNull;

/**
 * Advent of Code 2018 - Day 8: Memory Maneuver
 * <p>
 * Puzzle URL: https://adventofcode.com/2018/day/8
 */
public final class MemoryManeuverAOC2018Day8 {

    private static final String INPUT_FILE = "/2018/day8/day8_puzzle_data.txt";

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
        // Parse the input into an integer array
        int[] data = parseInput(input);
        // Use a wrapper index to allow recursion
        Index idx = new Index();
        int sum = sumMetadata(data, idx);
        return Integer.toString(sum);
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        // Parse the input into an integer array
        int[] data = parseInput(input);
        Index idx = new Index();
        int value = nodeValue(data, idx);
        return Integer.toString(value);
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = MemoryManeuverAOC2018Day8.class.getResourceAsStream(INPUT_FILE)) {
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
     * Parses the input lines into an integer array.
     * @param input the input lines
     * @return the parsed integer array
     */
    private static int[] parseInput(@NotNull List<String> input) {
        String[] tokens = input.get(0).trim().split("\\s+");
        int[] data = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            data[i] = Integer.parseInt(tokens[i]);
        }
        return data;
    }

    /**
     * Helper class to wrap the index for recursion.
     */
    private static class Index {
        int value = 0;
    }

    /**
     * Recursively sums all metadata entries in the tree (Part 1).
     * @param data the input data array
     * @param idx the current index (wrapped)
     * @return the sum of all metadata entries
     */
    private static int sumMetadata(@NotNull int[] data, @NotNull Index idx) {
        final int childCount = data[idx.value++];
        final int metaCount = data[idx.value++];
        int sum = 0;
        for (int i = 0; i < childCount; i++) {
            sum += sumMetadata(data, idx);
        }
        for (int i = 0; i < metaCount; i++) {
            sum += data[idx.value++];
        }
        return sum;
    }

    /**
     * Recursively computes the value of the root node (Part 2).
     * @param data the input data array
     * @param idx the current index (wrapped)
     * @return the value of the node
     */
    private static int nodeValue(@NotNull int[] data, @NotNull Index idx) {
        final int childCount = data[idx.value++];
        final int metaCount = data[idx.value++];
        int[] childValues = new int[childCount];
        for (int i = 0; i < childCount; i++) {
            childValues[i] = nodeValue(data, idx);
        }
        int value = 0;
        if (childCount == 0) {
            for (int i = 0; i < metaCount; i++) {
                value += data[idx.value++];
            }
        } else {
            for (int i = 0; i < metaCount; i++) {
                int ref = data[idx.value++];
                if (ref >= 1 && ref <= childCount) {
                    value += childValues[ref - 1];
                }
            }
        }
        return value;
    }
}
