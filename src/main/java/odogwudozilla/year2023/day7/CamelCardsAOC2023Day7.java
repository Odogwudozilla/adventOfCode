package odogwudozilla.year2023.day7;

import org.jetbrains.annotations.NotNull;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.IOException;

/**
 * Camel Cards - Advent of Code 2023 Day 7
 * <p>
 * Your all-expenses-paid trip turns out to be a one-way, five-minute ride in an airship. (At least it's a cool airship!) It drops you off at the edge of a vast desert and descends back to Island Island.
 * ...
 * In Camel Cards, you get a list of hands, and your goal is to order them based on the strength of each hand. A hand consists of five cards labeled one of A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, or 2. The relative strength of each card follows this order, where A is the highest and 2 is the lowest.
 * ...
 * Find the rank of every hand in your set. What are the total winnings?
 *
 * Official puzzle URL: https://adventofcode.com/2023/day/7
 */
public class CamelCardsAOC2023Day7 {
    private static final Map<Character, Integer> CARD_STRENGTH;
    static {
        CARD_STRENGTH = Map.ofEntries(Map.entry('A', 14), Map.entry('K', 13), Map.entry('Q', 12), Map.entry('J', 11), Map.entry('T', 10), Map.entry('9', 9), Map.entry('8', 8), Map.entry('7', 7), Map.entry('6', 6), Map.entry('5', 5), Map.entry('4', 4), Map.entry('3', 3), Map.entry('2', 2));
    }

    public static void main(String[] args) {
        try {
            List<String> inputLines = Files.readAllLines(Paths.get("src/main/resources/2023/day7/day7_puzzle_data.txt"));
            System.out.println("Loaded " + inputLines.size() + " lines of puzzle input.");
            int partOneResult = solvePartOne(inputLines);
            System.out.println("solvePartOne - Total winnings: " + partOneResult);
            int partTwoResult = solvePartTwo(inputLines);
            System.out.println("solvePartTwo - Total winnings: " + partTwoResult);
        } catch (IOException e) {
            System.err.println("main - Failed to read input file: " + e.getMessage());
        }
    }

    /**
     * Solves Part 1 of Camel Cards: ranks hands and computes total winnings.
     * @param inputLines the puzzle input lines
     * @return the total winnings
     */
    public static int solvePartOne(@NotNull List<String> inputLines) {
        List<HandBid> hands = new ArrayList<>();
        for (String line : inputLines) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length != 2) continue;
            hands.add(new HandBid(parts[0], Integer.parseInt(parts[1])));
        }
        hands.sort(new HandComparator());
        int total = 0;
        for (int i = 0; i < hands.size(); i++) {
            int rank = i + 1;
            total += hands.get(i).bid * rank;
        }
        return total;
    }

    /**
     * Solves Part 2 of Camel Cards: joker wildcards, J is weakest card.
     * @param inputLines the puzzle input lines
     * @return the total winnings for part two
     */
    public static int solvePartTwo(@NotNull List<String> inputLines) {
        List<HandBid> hands = new ArrayList<>();
        for (String line : inputLines) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length != 2) continue;
            hands.add(new HandBid(parts[0], Integer.parseInt(parts[1])));
        }
        hands.sort(new JokerHandComparator());
        int total = 0;
        for (int i = 0; i < hands.size(); i++) {
            int rank = i + 1;
            total += hands.get(i).bid * rank;
        }
        return total;
    }

    private static class HandBid {
        final String hand;
        final int bid;
        HandBid(String hand, int bid) {
            this.hand = hand;
            this.bid = bid;
        }
    }

    private static class HandComparator implements Comparator<HandBid> {
        @Override
        public int compare(HandBid h1, HandBid h2) {
            int type1 = handType(h1.hand);
            int type2 = handType(h2.hand);
            if (type1 != type2) {
                return Integer.compare(type1, type2);
            }
            for (int i = 0; i < 5; i++) {
                int c1 = CARD_STRENGTH.get(h1.hand.charAt(i));
                int c2 = CARD_STRENGTH.get(h2.hand.charAt(i));
                if (c1 != c2) {
                    return Integer.compare(c1, c2);
                }
            }
            return 0;
        }

        /**
         * Returns a numeric value for the hand type (lower is weaker).
         */
        private int handType(String hand) {
            Map<Character, Integer> freq = new HashMap<>();
            for (char c : hand.toCharArray()) {
                freq.put(c, freq.getOrDefault(c, 0) + 1);
            }
            Collection<Integer> counts = freq.values();
            if (counts.contains(5)) return 7; // Five of a kind
            if (counts.contains(4)) return 6; // Four of a kind
            if (counts.contains(3) && counts.contains(2)) return 5; // Full house
            if (counts.contains(3)) return 4; // Three of a kind
            int pairCount = 0;
            for (int v : counts) if (v == 2) pairCount++;
            if (pairCount == 2) return 3; // Two pair
            if (pairCount == 1) return 2; // One pair
            return 1; // High card
        }
    }

    private static class JokerHandComparator implements Comparator<HandBid> {
        private static final Map<Character, Integer> JOKER_CARD_STRENGTH;
        static {
            // Joker is weakest
            JOKER_CARD_STRENGTH = Map.ofEntries(Map.entry('A', 14), Map.entry('K', 13), Map.entry('Q', 12), Map.entry('T', 10), Map.entry('9', 9), Map.entry('8', 8), Map.entry('7', 7), Map.entry('6', 6), Map.entry('5', 5), Map.entry('4', 4), Map.entry('3', 3), Map.entry('2', 2), Map.entry('J', 1));
        }

        @Override
        public int compare(HandBid h1, HandBid h2) {
            int type1 = jokerHandType(h1.hand);
            int type2 = jokerHandType(h2.hand);
            if (type1 != type2) {
                return Integer.compare(type1, type2);
            }
            for (int i = 0; i < 5; i++) {
                int c1 = JOKER_CARD_STRENGTH.get(h1.hand.charAt(i));
                int c2 = JOKER_CARD_STRENGTH.get(h2.hand.charAt(i));
                if (c1 != c2) {
                    return Integer.compare(c1, c2);
                }
            }
            return 0;
        }

        /**
         * Returns a numeric value for the hand type (lower is weaker), using jokers as wildcards.
         */
        private int jokerHandType(String hand) {
            Map<Character, Integer> freq = new HashMap<>();
            for (char c : hand.toCharArray()) {
                freq.put(c, freq.getOrDefault(c, 0) + 1);
            }
            int jokers = freq.getOrDefault('J', 0);
            if (jokers == 5) return 7; // Five jokers = five of a kind
            // Remove jokers for max grouping
            freq.remove('J');
            int max = 0;
            for (int v : freq.values()) max = Math.max(max, v);
            // Add jokers to the most frequent non-joker card
            max += jokers;
            Collection<Integer> counts = freq.values();
            // Build a list of counts with jokers added to the max
            List<Integer> allCounts = new ArrayList<>(counts);
            if (!allCounts.isEmpty()) {
                int idx = allCounts.indexOf(max - jokers);
                if (idx != -1) allCounts.set(idx, max);
            } else {
                allCounts.add(max); // all jokers
            }
            // Now, determine hand type
            if (max == 5) return 7; // Five of a kind
            if (max == 4) return 6; // Four of a kind
            if (allCounts.contains(3) && allCounts.contains(2)) return 5; // Full house
            if (max == 3) return 4; // Three of a kind
            int pairCount = 0;
            for (int v : allCounts) if (v == 2) pairCount++;
            if (pairCount == 2) return 3; // Two pair
            if (pairCount == 1) return 2; // One pair
            return 1; // High card
        }
    }

}
