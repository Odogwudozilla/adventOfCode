package odogwudozilla.year2019.day16;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;

/**
 * Flawed Frequency Transmission (FFT) - Advent of Code 2019 Day 16
 * <p>
 * You're 3/4ths of the way through the gas giants. Not only do roundtrip signals to Earth take five hours, but the signal quality is quite bad as well. You can clean up the signal with the Flawed Frequency Transmission algorithm, or FFT.
 * <p>
 * As input, FFT takes a list of numbers. In the signal you received (your puzzle input), each number is a single digit.
 * <p>
 * FFT operates in repeated phases. In each phase, a new list is constructed with the same length as the input list. This new list is also used as the input for the next phase.
 * <p>
 * Each element in the new list is built by multiplying every value in the input list by a value in a repeating pattern and then adding up the results. Only the ones digit is kept.
 * <p>
 * The base pattern is 0, 1, 0, -1. Repeat each value in the pattern a number of times equal to the position in the output list being considered. Skip the very first value exactly once.
 * <p>
 * Official puzzle link: https://adventofcode.com/2019/day/16
 * @param args Command line arguments
 */
public class FlawedFrequencyTransmissionAOC2019Day16 {
    private static final String INPUT_PATH = "src/main/resources/2019/day16/day16_puzzle_data.txt";
    private static final int PHASES_PART_ONE = 100;
    private static final int[] BASE_PATTERN = {0, 1, 0, -1};

    public static void main(String[] args) {
        try {
            String input = Files.readAllLines(Paths.get(INPUT_PATH)).get(0).trim();
            System.out.println("solvePartOne - Calculating Part 1 solution...");
            String partOneResult = solvePartOne(input);
            System.out.println("solvePartOne - Part 1 result: " + partOneResult);
            System.out.println("solvePartTwo - Calculating Part 2 solution...");
            String partTwoResult = solvePartTwo(input);
            System.out.println("solvePartTwo - Part 2 result: " + partTwoResult);
        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1 of the Flawed Frequency Transmission puzzle.
     * @param input The input signal as a string of digits
     * @return The first eight digits of the final output list after 100 phases
     */
    public static String solvePartOne(String input) {
        List<Integer> signal = input.chars().map(c -> c - '0').boxed().collect(Collectors.toList());
        int length = signal.size();
        for (int phase = 0; phase < PHASES_PART_ONE; phase++) {
            List<Integer> nextSignal = new java.util.ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                int sum = 0;
                for (int j = 0; j < length; j++) {
                    int patternValue = getPatternValue(i, j);
                    sum += signal.get(j) * patternValue;
                }
                nextSignal.add(Math.abs(sum) % 10);
            }
            signal = nextSignal;
        }
        return signal.stream().limit(8).map(String::valueOf).collect(Collectors.joining());
    }

    /**
     * Returns the pattern value for a given output position and input index.
     * @param outputPos The output position (0-based)
     * @param inputIdx The input index (0-based)
     * @return The pattern value
     */
    private static int getPatternValue(int outputPos, int inputIdx) {
        int patternLength = BASE_PATTERN.length;
        int repeat = outputPos + 1;
        int patternIndex = ((inputIdx + 1) / repeat) % patternLength;
        return BASE_PATTERN[patternIndex];
    }

    /**
     * Solves Part 2 of the Flawed Frequency Transmission puzzle.
     * @param input The input signal as a string of digits
     * @return The eight-digit message embedded in the final output list
     */
    public static String solvePartTwo(String input) {
        int offset = Integer.parseInt(input.substring(0, 7));
        int inputLength = input.length();
        int totalLength = inputLength * 10000;
        if (offset < totalLength / 2) {
            // For offsets in the first half, a full simulation is required (not feasible for large input)
            return "Offset too small for efficient solution";
        }
        // Only need to simulate the region from offset to end
        int regionLength = totalLength - offset;
        int[] region = new int[regionLength];
        char[] inputChars = input.toCharArray();
        for (int i = 0; i < regionLength; i++) {
            region[i] = inputChars[(offset + i) % inputLength] - '0';
        }
        for (int phase = 0; phase < PHASES_PART_ONE; phase++) {
            int sum = 0;
            for (int i = regionLength - 1; i >= 0; i--) {
                sum = (sum + region[i]) % 10;
                region[i] = sum;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(region[i]);
        }
        return sb.toString();
    }
}
