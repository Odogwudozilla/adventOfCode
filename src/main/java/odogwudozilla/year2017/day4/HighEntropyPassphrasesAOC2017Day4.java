package odogwudozilla.year2017.day4;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Advent of Code 2017 - Day 4: High-Entropy Passphrases
 * <p>
 * A valid passphrase must contain no duplicate words (Part 1).
 * A valid passphrase must also contain no two words that are anagrams
 * of each other (Part 2).
 * <p>
 * Puzzle URL: https://adventofcode.com/2017/day/4
 */
public final class HighEntropyPassphrasesAOC2017Day4 {

    private static final String INPUT_FILE = "/2017/day4/day4_puzzle_data.txt";

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
     * Counts passphrases that contain no duplicate words.
     * @param input list of passphrase lines
     * @return the count of valid passphrases as a string
     */
    private static String solvePartOne(List<String> input) {
        long validCount = input.stream()
                .filter(line -> !line.isBlank())
                .filter(HighEntropyPassphrasesAOC2017Day4::hasNoDuplicateWords)
                .count();
        return String.valueOf(validCount);
    }

    /**
     * Counts passphrases that contain no two words which are anagrams of each other.
     * @param input list of passphrase lines
     * @return the count of valid passphrases as a string
     */
    private static String solvePartTwo(List<String> input) {
        long validCount = input.stream()
                .filter(line -> !line.isBlank())
                .filter(HighEntropyPassphrasesAOC2017Day4::hasNoAnagramDuplicates)
                .count();
        return String.valueOf(validCount);
    }

    /**
     * Returns true if the passphrase line contains no duplicate words.
     * @param line a single passphrase line
     * @return true if all words are distinct
     */
    private static boolean hasNoDuplicateWords(String line) {
        String[] words = line.trim().split("\\s+");
        Set<String> seen = new HashSet<>();
        for (String word : words) {
            if (!seen.add(word)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if no two words in the passphrase are anagrams of each other.
     * Two words are anagrams if they consist of exactly the same letters in any order.
     * @param line a single passphrase line
     * @return true if no two words are anagrams
     */
    private static boolean hasNoAnagramDuplicates(String line) {
        String[] words = line.trim().split("\\s+");
        Set<String> sortedWords = new HashSet<>();
        for (String word : words) {
            char[] letters = word.toCharArray();
            Arrays.sort(letters);
            String sortedWord = new String(letters);
            if (!sortedWords.add(sortedWord)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = HighEntropyPassphrasesAOC2017Day4.class.getResourceAsStream(INPUT_FILE)) {
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
