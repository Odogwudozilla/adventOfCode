package odogwudozilla.year2025.day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Advent of Code 2025 - Day 3: Lobby
 *
 * You need to power an escalator using battery banks. Each bank is a line of digits (1-9).
 * Within each bank, you must turn on exactly N batteries. The joltage produced is the
 * N-digit number formed by those batteries (in order, cannot rearrange).
 *
 * Part 1: Select exactly 2 batteries to maximize joltage.
 * Part 2: Select exactly 12 batteries to maximize joltage.
 *
 * Goal: Find the maximum possible joltage from each bank and calculate the total output joltage.
 *
 * @see <a href="https://adventofcode.com/2025/day/3">AoC 2025 Day 3</a>
 */
public class LobbyAOC2025Day3 {

    private static final String INPUT_FILE = "src/main/resources/2025/day3/day3_puzzle_data.txt";
    private static final int PART_1_BATTERIES = 2;
    private static final int PART_2_BATTERIES = 12;

    public static void main(String[] args) {
        try {
            List<String> banks = Files.readAllLines(Paths.get(INPUT_FILE));

            // Part 1: Select exactly 2 batteries
            long totalJoltagePart1 = solvePartOne(banks);
            System.out.println("Part 1 - Total output joltage (2 batteries): " + totalJoltagePart1);

            long totalJoltagePart2 = solvePartTwo(banks);
            System.out.println("Part 2 - Total output joltage (12 batteries): " + totalJoltagePart2);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Calculate the total output joltage from all battery banks.
     * @param banks list of battery bank strings
     * @param batteryCount number of batteries to select from each bank
     * @return the sum of maximum joltages from all banks
     */
    private static long calculateTotalJoltage(List<String> banks, int batteryCount) {
        long totalJoltage = 0;

        for (String bank : banks) {
            long maxJoltage = findMaxJoltageForBank(bank, batteryCount);
            totalJoltage += maxJoltage;
        }

        return totalJoltage;
    }

    /**
     * Find the maximum joltage possible for a single battery bank.
     * Uses a greedy algorithm to select the N largest digits while maintaining their order.
     * @param bank string of digits representing battery joltages
     * @param batteryCount number of batteries to select
     * @return the maximum joltage possible with the given number of batteries
     */
    private static long findMaxJoltageForBank(String bank, int batteryCount) {
        int bankLength = bank.length();

        // We need to select batteryCount digits
        // At each position, we want the largest digit that still allows us to select enough digits after it
        StringBuilder result = new StringBuilder();
        int startIndex = 0;

        for (int i = 0; i < batteryCount; i++) {
            // We need (batteryCount - i) more digits total (including current one)
            // So we can look up to index: bankLength - (batteryCount - i)
            int maxSearchIndex = bankLength - (batteryCount - i);

            // Find the maximum digit in the valid range
            char maxDigit = '0';
            int maxDigitIndex = startIndex;

            for (int j = startIndex; j <= maxSearchIndex; j++) {
                if (bank.charAt(j) > maxDigit) {
                    maxDigit = bank.charAt(j);
                    maxDigitIndex = j;
                }
            }

            result.append(maxDigit);
            startIndex = maxDigitIndex + 1;
        }

        return Long.parseLong(result.toString());
    }

    /**
     * Standardised method for Part 1.
     */
    private static long solvePartOne(List<String> banks) {
        return calculateTotalJoltage(banks, PART_1_BATTERIES);
    }

    /**
     * Standardised method for Part 2.
     */
    private static long solvePartTwo(List<String> banks) {
        return calculateTotalJoltage(banks, PART_2_BATTERIES);
    }
}
