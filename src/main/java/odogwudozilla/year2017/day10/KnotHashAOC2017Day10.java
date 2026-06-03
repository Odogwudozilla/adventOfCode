package odogwudozilla.year2017.day10;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2017 - Day 10: Knot Hash
 * <p>
 * Puzzle URL: https://adventofcode.com/2017/day/10
 */
public final class KnotHashAOC2017Day10 {

    private static final String INPUT_FILE = "/2017/day10/day10_puzzle_data.txt";
    private static final int DEFAULT_LIST_SIZE = 256;

    /**
     * Immutable holder for knot hash state: the sparse hash and metadata.
     */
    private static final class KnotHashState {
        final int[] sparseHash;
        final int currentPosition;
        final int skipSize;

        KnotHashState(int[] sparseHash, int currentPosition, int skipSize) {
            this.sparseHash = sparseHash;
            this.currentPosition = currentPosition;
            this.skipSize = skipSize;
        }
    }

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
    static String solvePartOne(List<String> input) {
        return solvePartOne(input, DEFAULT_LIST_SIZE);
    }

    static String solvePartOne(List<String> input, int listSize) {
        int[] lengths = parseLengths(input);
        int[] numbers = initialiseNumbers(listSize);

        int currentPosition = 0;
        int skipSize = 0;

        for (int length : lengths) {
            if (length < 0 || length > numbers.length) {
                throw new IllegalArgumentException("Invalid length for list size " + numbers.length + ": " + length);
            }
            reverseCircular(numbers, currentPosition, length);
            currentPosition = (currentPosition + length + skipSize) % numbers.length;
            skipSize++;
        }

        return String.valueOf(numbers[0] * numbers[1]);
    }

    private static int[] parseLengths(List<String> input) {
        if (input == null || input.isEmpty()) {
            return new int[0];
        }

        String firstLine = input.get(0);
        if (firstLine == null || firstLine.trim().isEmpty()) {
            return new int[0];
        }

        String[] tokens = firstLine.split(",");
        List<Integer> values = new ArrayList<>();
        for (String token : tokens) {
            String trimmed = token.trim();
            if (!trimmed.isEmpty()) {
                values.add(Integer.parseInt(trimmed));
            }
        }

        int[] lengths = new int[values.size()];
        for (int i = 0; i < values.size(); i++) {
            lengths[i] = values.get(i);
        }
        return lengths;
    }

    private static int[] initialiseNumbers(int listSize) {
        int[] numbers = new int[listSize];
        for (int i = 0; i < listSize; i++) {
            numbers[i] = i;
        }
        return numbers;
    }

    private static void reverseCircular(int[] numbers, int start, int length) {
        for (int offset = 0; offset < length / 2; offset++) {
            int leftIndex = (start + offset) % numbers.length;
            int rightIndex = (start + length - 1 - offset) % numbers.length;

            int temporary = numbers[leftIndex];
            numbers[leftIndex] = numbers[rightIndex];
            numbers[rightIndex] = temporary;
        }
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer (knot hash in hexadecimal)
     */
    private static String solvePartTwo(List<String> input) {
        String rawInput = input == null || input.isEmpty() ? "" : input.get(0).trim();
        int[] lengths = toAsciiLengthsWithSuffix(rawInput);
        KnotHashState state = runKnotHashRounds(lengths, DEFAULT_LIST_SIZE, 64);
        int[] denseHash = buildDenseHash(state.sparseHash);
        return toHexadecimalString(denseHash);
    }

    /**
     * Converts an input string to ASCII codes and appends the standard suffix.
     * @param input the input string
     * @return array of lengths including ASCII codes and suffix
     */
    private static int[] toAsciiLengthsWithSuffix(String input) {
        final int[] suffix = {17, 31, 73, 47, 23};
        byte[] asciiBytes = input.getBytes(StandardCharsets.UTF_8);
        int[] lengths = new int[asciiBytes.length + suffix.length];

        for (int i = 0; i < asciiBytes.length; i++) {
            lengths[i] = asciiBytes[i] & 0xFF;
        }
        for (int i = 0; i < suffix.length; i++) {
            lengths[asciiBytes.length + i] = suffix[i];
        }

        return lengths;
    }

    /**
     * Runs 64 rounds of the knot hash algorithm, preserving state between rounds.
     * @param lengths the length sequence to apply in each round
     * @param listSize the size of the list
     * @param rounds the number of rounds to execute
     * @return the final state after all rounds
     */
    private static KnotHashState runKnotHashRounds(int[] lengths, int listSize, int rounds) {
        int[] numbers = initialiseNumbers(listSize);
        int currentPosition = 0;
        int skipSize = 0;

        for (int round = 0; round < rounds; round++) {
            for (int length : lengths) {
                if (length < 0 || length > numbers.length) {
                    throw new IllegalArgumentException("Invalid length for list size " + numbers.length + ": " + length);
                }
                reverseCircular(numbers, currentPosition, length);
                currentPosition = (currentPosition + length + skipSize) % numbers.length;
                skipSize++;
            }
        }

        return new KnotHashState(numbers, currentPosition, skipSize);
    }

    /**
     * Builder the dense hash from the sparse hash using XOR.
     * @param sparseHash the sparse hash (256 elements)
     * @return the dense hash (16 elements)
     */
    private static int[] buildDenseHash(int[] sparseHash) {
        final int blockSize = 16;
        if (sparseHash.length % blockSize != 0) {
            throw new IllegalArgumentException("Sparse hash length must be divisible by 16");
        }

        int[] denseHash = new int[sparseHash.length / blockSize];
        for (int block = 0; block < denseHash.length; block++) {
            int xorValue = sparseHash[block * blockSize];
            for (int i = 1; i < blockSize; i++) {
                xorValue ^= sparseHash[block * blockSize + i];
            }
            denseHash[block] = xorValue;
        }

        return denseHash;
    }

    /**
     * Converts a dense hash to a 32-character hexadecimal string.
     * @param denseHash the dense hash (16 elements)
     * @return the hexadecimal string representation
     */
    private static String toHexadecimalString(int[] denseHash) {
        StringBuilder builder = new StringBuilder(denseHash.length * 2);
        for (int value : denseHash) {
            builder.append(String.format("%02x", value & 0xFF));
        }
        return builder.toString();
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = KnotHashAOC2017Day10.class.getResourceAsStream(INPUT_FILE)) {
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
