package odogwudozilla.year2021.day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Advent of Code 2021 - Day 3: Binary Diagnostic
 *
 * The submarine's diagnostic report consists of binary numbers that can be decoded
 * to determine the power consumption. The gamma rate is calculated by finding the
 * most common bit in each position, and the epsilon rate uses the least common bit.
 * Power consumption is the product of gamma rate and epsilon rate.
 *
 * Puzzle: https://adventofcode.com/2021/day/3
 */
public class BinaryDiagnosticAOC2021Day3 {

    private static final String RESOURCE_PATH = "src/main/resources/2021/day3/day3_puzzle_data.txt";

    public static void main(String[] args) {
        try {
            List<String> binaryNumbers = readPuzzleInput();

            // Part 1: Calculate power consumption
            long part1Result = solvePartOne(binaryNumbers);
            System.out.println("Part 1 - Power Consumption: " + part1Result);

            // Part 2: Calculate life support rating (if available)
            long part2Result = solvePartTwo(binaryNumbers);
            System.out.println("Part 2 - Life Support Rating: " + part2Result);

        } catch (IOException e) {
            System.err.println("Error reading puzzle input: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Solves Part 1: Calculate power consumption by multiplying gamma rate and epsilon rate.
     * @param binaryNumbers the list of binary numbers from the diagnostic report
     * @return the power consumption
     */
    private static long solvePartOne(List<String> binaryNumbers) {
        if (binaryNumbers.isEmpty()) {
            return 0;
        }

        int bitLength = binaryNumbers.get(0).length();
        int totalNumbers = binaryNumbers.size();

        // Count the number of 1s in each bit position
        int[] onesCount = new int[bitLength];
        for (String binary : binaryNumbers) {
            for (int i = 0; i < bitLength; i++) {
                if ('1' == binary.charAt(i)) {
                    onesCount[i]++;
                }
            }
        }

        // Build gamma rate (most common bit) and epsilon rate (least common bit)
        StringBuilder gammaBuilder = new StringBuilder();
        StringBuilder epsilonBuilder = new StringBuilder();

        for (int i = 0; i < bitLength; i++) {
            if (onesCount[i] > totalNumbers / 2) {
                // More 1s than 0s
                gammaBuilder.append('1');
                epsilonBuilder.append('0');
            } else {
                // More 0s than 1s
                gammaBuilder.append('0');
                epsilonBuilder.append('1');
            }
        }

        long gammaRate = Long.parseLong(gammaBuilder.toString(), 2);
        long epsilonRate = Long.parseLong(epsilonBuilder.toString(), 2);

        return gammaRate * epsilonRate;
    }

    /**
     * Solves Part 2: Calculate life support rating by multiplying oxygen generator rating and CO2 scrubber rating.
     * @param binaryNumbers the list of binary numbers from the diagnostic report
     * @return the life support rating
     */
    private static long solvePartTwo(List<String> binaryNumbers) {
        if (binaryNumbers.isEmpty()) {
            return 0;
        }

        long oxygenRating = findRatingByBitCriteria(binaryNumbers, true);
        long co2Rating = findRatingByBitCriteria(binaryNumbers, false);

        return oxygenRating * co2Rating;
    }

    /**
     * Finds a rating value by filtering numbers based on bit criteria.
     * @param numbers the list of binary numbers to filter
     * @param useMostCommon true to find oxygen generator rating (most common bit), false for CO2 scrubber (least common bit)
     * @return the rating value in decimal
     */
    private static long findRatingByBitCriteria(List<String> numbers, boolean useMostCommon) {
        List<String> remaining = new ArrayList<>(numbers);
        int bitPosition = 0;
        int bitLength = numbers.get(0).length();

        while (remaining.size() > 1 && bitPosition < bitLength) {
            char criterionBit = determineCriterionBit(remaining, bitPosition, useMostCommon);
            final int currentPosition = bitPosition;
            remaining.removeIf(binary -> binary.charAt(currentPosition) != criterionBit);
            bitPosition++;
        }

        return remaining.isEmpty() ? 0 : Long.parseLong(remaining.get(0), 2);
    }

    /**
     * Determines the criterion bit for filtering based on the current bit position.
     * @param numbers the list of binary numbers
     * @param bitPosition the current bit position to examine
     * @param useMostCommon true for most common bit (oxygen), false for least common bit (CO2)
     * @return the criterion bit ('0' or '1')
     */
    private static char determineCriterionBit(List<String> numbers, int bitPosition, boolean useMostCommon) {
        int onesCount = 0;
        int totalCount = numbers.size();

        for (String binary : numbers) {
            if ('1' == binary.charAt(bitPosition)) {
                onesCount++;
            }
        }

        int zerosCount = totalCount - onesCount;

        if (useMostCommon) {
            // Oxygen generator: most common bit (1 if equal)
            return onesCount >= zerosCount ? '1' : '0';
        } else {
            // CO2 scrubber: least common bit (0 if equal)
            return onesCount < zerosCount ? '1' : '0';
        }
    }

    /**
     * Reads the puzzle input from the resource file.
     * @return list of binary number strings
     * @throws IOException if file cannot be read
     */
    private static List<String> readPuzzleInput() throws IOException {
        Path path = Paths.get(RESOURCE_PATH);
        return new ArrayList<>(Files.readAllLines(path));
    }
}

