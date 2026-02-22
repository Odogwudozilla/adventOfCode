package odogwudozilla.year2024.day22;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Monkey Market - Advent of Code 2024 Day 22
 *
 * Simulates buyers' secret number evolution and price changes.
 * Part 1: Sum the 2000th secret number for all buyers.
 * Part 2: Find the best sequence of four price changes to maximise bananas.
 *
 * Official puzzle URL: https://adventofcode.com/2024/day/22
 *
 * @param args Command line arguments (not used)
 */
public class MonkeyMarketAOC2024Day22 {
    private static final int NUM_SECRETS = 2000;
    private static final int SEQ_LEN = 4;
    private static final int MODULO = 16777216;
    private static final int PRICE_MODULO = 10;

    public static void main(String[] args) {
        String inputPath = "src/main/resources/2024/day22/day22_puzzle_data.txt";
        List<Long> initialSecrets = parseInput(inputPath);
        if (initialSecrets.isEmpty()) {
            System.err.println("main - No input data found.");
            return;
        }
        System.out.println("main - Sum of 2000th secret numbers: " + solvePartOne(initialSecrets));
        System.out.println("main - Maximum bananas for Part 2: " + solvePartTwo(initialSecrets));
    }

    /**
     * Parses the input file for initial secret numbers.
     * @param path Input file path
     * @return List of initial secret numbers
     */
    private static List<Long> parseInput(String path) {
        List<Long> secrets = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    secrets.add(Long.parseLong(line.trim()));
                }
            }
        } catch (IOException e) {
            System.err.println("parseInput - Error reading input file: " + e.getMessage());
        }
        return secrets;
    }

    /**
     * Solves Part 1: Sum the 2000th secret number for all buyers.
     * @param initialSecrets List of initial secret numbers
     * @return Sum of 2000th secret numbers
     */
    private static long solvePartOne(List<Long> initialSecrets) {
        long sum = 0;
        for (long secret : initialSecrets) {
            sum += generateNthSecret(secret, NUM_SECRETS);
        }
        return sum;
    }

    /**
     * Solves Part 2: Finds the best sequence of four price changes to maximise bananas.
     * @param initialSecrets List of initial secret numbers
     * @return Maximum sum of prices for the best sequence
     */
    private static long solvePartTwo(List<Long> initialSecrets) {
        List<List<Integer>> allPrices = new ArrayList<>();
        List<List<Integer>> allChanges = new ArrayList<>();
        Set<String> uniqueSeqs = new HashSet<>();
        for (long secret : initialSecrets) {
            List<Integer> prices = new ArrayList<>();
            long s = secret;
            prices.add((int)(s % PRICE_MODULO));
            for (int i = 0; i < NUM_SECRETS; i++) {
                s = generateNextSecret(s);
                prices.add((int)(s % PRICE_MODULO));
            }
            allPrices.add(prices);
            List<Integer> changes = new ArrayList<>();
            for (int i = 1; i < prices.size(); i++) {
                changes.add(prices.get(i) - prices.get(i - 1));
            }
            allChanges.add(changes);
            for (int i = 0; i <= changes.size() - SEQ_LEN; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < SEQ_LEN; j++) {
                    sb.append(changes.get(i + j)).append(",");
                }
                uniqueSeqs.add(sb.toString());
            }
        }
        System.out.println("solvePartTwo - Unique sequences: " + uniqueSeqs.size());
        System.out.println("solvePartTwo - Buyers: " + allChanges.size());
        // Parallelise sequence search
        long maxSum = 0;
        String bestSeq = null;
        List<String> seqList = new ArrayList<>(uniqueSeqs);
        maxSum = seqList.parallelStream().mapToLong(seqStr -> {
            int[] seq = parseSeq(seqStr);
            long sum = 0;
            for (int buyer = 0; buyer < allChanges.size(); buyer++) {
                List<Integer> changes = allChanges.get(buyer);
                List<Integer> prices = allPrices.get(buyer);
                for (int i = 0; i <= changes.size() - SEQ_LEN; i++) {
                    boolean match = true;
                    for (int j = 0; j < SEQ_LEN; j++) {
                        if (changes.get(i + j) != seq[j]) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        sum += prices.get(i + SEQ_LEN);
                        break;
                    }
                }
            }
            return sum;
        }).max().orElse(0);
        // Find best sequence
        for (String seqStr : seqList) {
            int[] seq = parseSeq(seqStr);
            long sum = 0;
            for (int buyer = 0; buyer < allChanges.size(); buyer++) {
                List<Integer> changes = allChanges.get(buyer);
                List<Integer> prices = allPrices.get(buyer);
                for (int i = 0; i <= changes.size() - SEQ_LEN; i++) {
                    boolean match = true;
                    for (int j = 0; j < SEQ_LEN; j++) {
                        if (changes.get(i + j) != seq[j]) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        sum += prices.get(i + SEQ_LEN);
                        break;
                    }
                }
            }
            if (sum == maxSum) {
                System.out.print("solvePartTwo - Best sequence: ");
                for (int v : seq) System.out.print(v + " ");
                System.out.println();
                break;
            }
        }
        return maxSum;
    }

    /**
     * Parses a comma-separated sequence string to int array.
     * @param seqStr Sequence string
     * @return int array
     */
    private static int[] parseSeq(String seqStr) {
        String[] parts = seqStr.split(",");
        int[] arr = new int[SEQ_LEN];
        for (int i = 0; i < SEQ_LEN; i++) {
            arr[i] = Integer.parseInt(parts[i]);
        }
        return arr;
    }

    /**
     * Generates the nth secret number from the initial secret.
     * @param initialSecret Initial secret number
     * @param n Number of evolutions
     * @return nth secret number
     */
    private static long generateNthSecret(long initialSecret, int n) {
        long secret = initialSecret;
        for (int i = 0; i < n; i++) {
            secret = generateNextSecret(secret);
        }
        return secret;
    }

    /**
     * Generates the next secret number from the current secret number.
     * @param secret Current secret number
     * @return Next secret number
     */
    private static long generateNextSecret(long secret) {
        secret = mix(secret, secret * 64);
        secret = prune(secret);
        secret = mix(secret, secret / 32);
        secret = prune(secret);
        secret = mix(secret, secret * 2048);
        secret = prune(secret);
        return secret;
    }

    /**
     * Mixes a value into the secret number using bitwise XOR.
     * @param secret The current secret number
     * @param value The value to mix in
     * @return The mixed secret number
     */
    private static long mix(long secret, long value) {
        return secret ^ value;
    }

    /**
     * Prunes the secret number by taking modulo MODULO.
     * @param secret The current secret number
     * @return The pruned secret number
     */
    private static long prune(long secret) {
        return secret % MODULO;
    }
}
