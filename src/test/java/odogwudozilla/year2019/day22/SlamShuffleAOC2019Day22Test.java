package odogwudozilla.year2019.day22;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for SlamShuffleAOC2019Day22.
 */
class SlamShuffleAOC2019Day22Test {

    /**
     * Verifies the shuffle logic for a small deck and known shuffle sequence.
     */
    @Test
    void testSmallDeckKnownSequence() {
        // Example from AoC 2019 Day 22: deck size 10
        List<String> instructions = Arrays.asList(
                "deal with increment 7",
                "deal into new stack",
                "deal into new stack"
        );
        int deckSize = 10;
        // After shuffling, deck should be: 0 3 6 9 2 5 8 1 4 7
        List<Integer> expectedDeck = Arrays.asList(0, 3, 6, 9, 2, 5, 8, 1, 4, 7);
        List<Integer> actualDeck = SlamShuffleAOC2019Day22.simulateDeck(instructions, deckSize);
        assertEquals(expectedDeck, actualDeck, "Deck order after shuffles should match expected");
        // Check position of card 7
        int expectedPosition = 9;
        int actualPosition = SlamShuffleAOC2019Day22.solvePartOne(instructions, deckSize, 7);
        assertEquals(expectedPosition, actualPosition, "Position of card 7 should be 9");
    }

    /**
     * Verifies the modular arithmetic logic for Part 2 using a small deck and known shuffle sequence.
     */
    @Test
    void testPartTwoSmallDeckMultipleShuffles() {
        // Example: deck size 10, repeat 3 times
        List<String> instructions = Arrays.asList(
                "deal with increment 7",
                "deal into new stack",
                "deal into new stack"
        );
        int deckSize = 10;
        int repetitions = 3;
        int position = 0; // Query: which card ends up at position 0?

        // Brute-force simulation
        List<Integer> deck = SlamShuffleAOC2019Day22.simulateDeck(instructions, deckSize);
        for (int i = 1; i < repetitions; i++) {
            deck = simulateDeck(instructions, deck);
        }
        int expectedCard = deck.get(position);

        // Modular arithmetic solution
        long actualCard = SlamShuffleAOC2019Day22.solvePartTwo(instructions, deckSize, repetitions, position);
        assertEquals(expectedCard, actualCard, "Card at position 0 after 3 shuffles should match brute-force simulation");
    }

    /**
     * Verifies the affine modular arithmetic path for Part 2 using a prime-sized deck (> 1000)
     * and a small number of repetitions, comparing against brute-force simulation.
     */
    @Test
    void testPartTwoAffinePath_primeDeckSmallRepetitions() {
        // Use a prime deck size > 1000 to force the affine arithmetic path
        List<String> instructions = Arrays.asList(
                "deal with increment 7",
                "deal into new stack",
                "deal into new stack"
        );
        int deckSize = 1009; // 1009 is prime
        int repetitions = 5;
        // Brute-force expected results for each position
        List<Integer> deck = SlamShuffleAOC2019Day22.simulateDeck(instructions, deckSize);
        for (int i = 1; i < repetitions; i++) {
            deck = simulateDeck(instructions, deck);
        }
        // Check a few positions using the affine arithmetic method
        for (int pos = 0; pos < 10; pos++) {
            int expectedCard = deck.get(pos);
            long actualCard = SlamShuffleAOC2019Day22.solvePartTwo(instructions, deckSize, repetitions, pos);
            assertEquals(expectedCard, actualCard,
                    "Card at position " + pos + " after " + repetitions + " shuffles should match brute-force");
        }
    }

    // Overload for simulateDeck to accept a starting deck
    // (This is a helper for the test, not part of the main class API)
    private static List<Integer> simulateDeck(List<String> instructions, List<Integer> startDeck) {
        List<Integer> deck = new java.util.ArrayList<>(startDeck);
        for (String instruction : instructions) {
            if ("deal into new stack".equals(instruction)) {
                java.util.Collections.reverse(deck);
            } else if (instruction.startsWith("cut ")) {
                int n = Integer.parseInt(instruction.substring(4));
                int size = deck.size();
                n = ((n % size) + size) % size;
                List<Integer> cut = new java.util.ArrayList<>(deck.subList(0, n));
                deck.subList(0, n).clear();
                deck.addAll(cut);
            } else if (instruction.startsWith("deal with increment ")) {
                int n = Integer.parseInt(instruction.substring(20));
                int size = deck.size();
                List<Integer> newDeck = new java.util.ArrayList<>(deck);
                for (int i = 0; i < size; i++) {
                    newDeck.set((i * n) % size, deck.get(i));
                }
                for (int i = 0; i < size; i++) {
                    deck.set(i, newDeck.get(i));
                }
            }
        }
        return deck;
    }
}
