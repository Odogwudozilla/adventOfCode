package odogwudozilla.year2023.day15;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2023 - Day 15: Lens Library
 * <p>
 * Puzzle URL: https://adventofcode.com/2023/day/15
 */
public final class LensLibraryAOC2023Day15 {

    private static final String INPUT_FILE = "/2023/day15/day15_puzzle_data.txt";
    private static final int HASH_MULTIPLIER = 17;
    private static final int HASH_MODULUS = 256;
    private static final int BOX_COUNT = 256;
    private static final char REMOVE_OPERATION = '-';
    private static final char ASSIGN_OPERATION = '=';

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
        String joined = parseInitialisationSequence(input);
        if (joined.isEmpty()) {
            return "0";
        }

        String[] steps = joined.split(",");
        long total = 0L;
        for (String step : steps) {
            total += computeHash(step);
        }
        return String.valueOf(total);
    }

    /**
     * Joins input lines into one continuous sequence while ignoring newline boundaries.
     */
    private static String parseInitialisationSequence(List<String> input) {
        StringBuilder builder = new StringBuilder();
        for (String line : input) {
            builder.append(line);
        }
        return builder.toString();
    }

    /**
     * Computes the HASH value for one initialisation step.
     */
    private static int computeHash(String step) {
        int current = 0;
        for (int index = 0; index < step.length(); index++) {
            current += step.charAt(index);
            current *= HASH_MULTIPLIER;
            current %= HASH_MODULUS;
        }
        return current;
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        String joined = parseInitialisationSequence(input);
        if (joined.isEmpty()) {
            return "0";
        }

        List<LinkedHashMap<String, Integer>> boxes = initialiseBoxes();
        String[] steps = joined.split(",");
        for (String token : steps) {
            LensStep step = parseStep(token);
            applyStep(boxes, step);
        }

        return String.valueOf(computeFocusingPower(boxes));
    }

    private static List<LinkedHashMap<String, Integer>> initialiseBoxes() {
        List<LinkedHashMap<String, Integer>> boxes = new ArrayList<>(BOX_COUNT);
        for (int index = 0; index < BOX_COUNT; index++) {
            boxes.add(new LinkedHashMap<>());
        }
        return boxes;
    }

    private static LensStep parseStep(String token) {
        if (token.endsWith(String.valueOf(REMOVE_OPERATION))) {
            String label = token.substring(0, token.length() - 1);
            return new LensStep(label, REMOVE_OPERATION, -1);
        }

        int operationIndex = token.indexOf(ASSIGN_OPERATION);
        if (operationIndex < 0) {
            throw new IllegalArgumentException("Invalid step token: " + token);
        }

        String label = token.substring(0, operationIndex);
        int focalLength = Integer.parseInt(token.substring(operationIndex + 1));
        return new LensStep(label, ASSIGN_OPERATION, focalLength);
    }

    private static void applyStep(List<LinkedHashMap<String, Integer>> boxes, LensStep step) {
        int boxIndex = computeHash(step.label());
        LinkedHashMap<String, Integer> box = boxes.get(boxIndex);

        if (step.operation() == REMOVE_OPERATION) {
            box.remove(step.label());
            return;
        }

        box.put(step.label(), step.focalLength());
    }

    private static long computeFocusingPower(List<LinkedHashMap<String, Integer>> boxes) {
        long total = 0L;
        for (int boxIndex = 0; boxIndex < boxes.size(); boxIndex++) {
            int slotIndex = 1;
            for (int focalLength : boxes.get(boxIndex).values()) {
                total += (long) (boxIndex + 1) * slotIndex * focalLength;
                slotIndex++;
            }
        }
        return total;
    }

    private record LensStep(String label, char operation, int focalLength) {
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = LensLibraryAOC2023Day15.class.getResourceAsStream(INPUT_FILE)) {
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
