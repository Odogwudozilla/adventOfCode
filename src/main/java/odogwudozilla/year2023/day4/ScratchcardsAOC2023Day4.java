package odogwudozilla.year2023.day4;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Advent of Code 2023 Day 4: Scratchcards
 * <p>
 * The puzzle involves calculating the point value of scratchcards.
 * Each card has winning numbers and your numbers. Points are calculated by:
 * - First match: 1 point
 * - Each subsequent match: doubles the points (2, 4, 8, 16, etc.)
 * <p>
 * https://adventofcode.com/2023/day/4
 */
public class ScratchcardsAOC2023Day4 {
    private static final String RESOURCE_PATH = "src/main/resources/2023/day4/day4_puzzle_data.txt";
    private static final String PIPE_DELIMITER = "\\|";
    private static final String SPACE_DELIMITER = "\\s+";
    private static final int INITIAL_POWER = 1;

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(RESOURCE_PATH));

        long partOne = solvePartOne(lines);
        System.out.println("Part 1: " + partOne);

        long partTwo = solvePartTwo(lines);
        System.out.println("Part 2: " + partTwo);
    }

    /**
     * Solves Part 1: Calculate total points from all scratchcards.
     */
    private static long solvePartOne(List<String> lines) {
        long totalPoints = 0;

        for (String line : lines) {
            int matchCount = countMatches(line);
            if (matchCount > 0) {
                // First match is 1 point, then double for each additional match
                long points = (long) Math.pow(2, matchCount - 1);
                totalPoints += points;
            }
        }

        return totalPoints;
    }

    /**
     * Solves Part 2: Calculate total scratchcards including copies.
     * Each match causes you to win copies of subsequent cards.
     */
    private static long solvePartTwo(List<String> lines) {
        int cardCount = lines.size();
        long[] cardCopies = new long[cardCount];

        // Initialize: we have one copy of each card
        for (int i = 0; i < cardCount; i++) {
            cardCopies[i] = 1;
        }

        for (int i = 0; i < cardCount; i++) {
            String line = lines.get(i);
            int matchCount = countMatches(line);

            // For each match, add copies of the subsequent cards
            for (int j = 1; j <= matchCount && i + j < cardCount; j++) {
                cardCopies[i + j] += cardCopies[i];
            }
        }

        // Sum all card copies
        long totalCards = 0;
        for (long copies : cardCopies) {
            totalCards += copies;
        }

        return totalCards;
    }

    /**
     * Counts the number of matching numbers between winning numbers and your numbers.
     */
    private static int countMatches(String line) {
        // Parse the card line: "Card X: A B C | D E F"
        String[] parts = line.split(PIPE_DELIMITER);
        String winningPart = parts[0].split(":")[1].trim();
        String yourPart = parts[1].trim();

        // Extract winning numbers into a set
        Set<Integer> winningNumbers = new HashSet<>();
        String[] winningTokens = winningPart.split(SPACE_DELIMITER);
        for (String token : winningTokens) {
            winningNumbers.add(Integer.parseInt(token));
        }

        // Count matches in your numbers
        int matches = 0;
        String[] yourTokens = yourPart.split(SPACE_DELIMITER);
        for (String token : yourTokens) {
            if (winningNumbers.contains(Integer.parseInt(token))) {
                matches++;
            }
        }

        return matches;
    }
}

