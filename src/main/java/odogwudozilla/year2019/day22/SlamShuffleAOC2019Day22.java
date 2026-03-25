package odogwudozilla.year2019.day22;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Slam Shuffle - Advent of Code 2019 Day 22
 * https://adventofcode.com/2019/day/22
 *
 * There isn't much to do while you wait for the droids to repair your ship. At least you're drifting in the right direction. You decide to practice a new card shuffle you've been working on.
 *
 * Digging through the ship's storage, you find a deck of space cards! Just like any deck of space cards, there are 10007 cards in the deck numbered 0 through 10006. The deck must be new - they're still in factory order, with 0 on the top, then 1, then 2, and so on, all the way through to 10006 on the bottom.
 *
 * You've been practicing three different techniques that you use while shuffling: deal into new stack, cut N cards, and deal with increment N. The complete shuffle process consists of applying many of these techniques in sequence.
 *
 * After shuffling your factory order deck of 10007 cards, what is the position of card 2019?
 */
public class SlamShuffleAOC2019Day22 {
    private static final int DECK_SIZE = 10007;
    private static final int TARGET_CARD = 2019;
    private static final String INPUT_PATH = "src/main/resources/2019/day22/day22_puzzle_data.txt";
    private static final long PART_TWO_DECK_SIZE = 119315717514047L;
    private static final long PART_TWO_SHUFFLES = 101741582076661L;
    private static final int PART_TWO_POSITION = 2020;

    public static void main(String[] args) throws IOException {
        List<String> instructions = Files.readAllLines(Paths.get(INPUT_PATH));
        int position = solvePartOne(instructions, DECK_SIZE, TARGET_CARD);
        System.out.println("Part 1: Position of card " + TARGET_CARD + " is " + position);
        long cardAt2020 = solvePartTwo(instructions, PART_TWO_DECK_SIZE, PART_TWO_SHUFFLES, PART_TWO_POSITION);
        System.out.println("Part 2: Card at position 2020 is " + cardAt2020);
    }

    /**
     * Solves Part 1: Finds the position of the target card after shuffling.
     * @param instructions the shuffle instructions
     * @param deckSize the size of the deck
     * @param targetCard the card to find
     * @return the position of the target card
     */
    public static int solvePartOne(@NotNull List<String> instructions, int deckSize, int targetCard) {
        List<Integer> deck = new ArrayList<>(deckSize);
        for (int i = 0; i < deckSize; i++) {
            deck.add(i);
        }
        for (String instruction : instructions) {
            if (instruction.equals("deal into new stack")) {
                reverse(deck);
            } else if (instruction.startsWith("cut ")) {
                int n = Integer.parseInt(instruction.substring(4));
                cut(deck, n);
            } else if (instruction.startsWith("deal with increment ")) {
                int n = Integer.parseInt(instruction.substring(20));
                dealWithIncrement(deck, n);
            }
        }
        return deck.indexOf(targetCard);
    }

    /**
     * Solves Part 2: Finds the card at the given position after shuffling a huge deck many times.
     * @param instructions the shuffle instructions
     * @param deckSize the size of the deck
     * @param shuffles the number of shuffle repetitions
     * @param position the position to query
     * @return the card at the given position
     */
    public static long solvePartTwo(@NotNull List<String> instructions, long deckSize, long shuffles, int position) {
        // For small decks, use brute-force simulation
        if (deckSize <= 1000) {
            List<Integer> deck = simulateDeck(instructions, (int) deckSize);
            for (long i = 1; i < shuffles; i++) {
                deck = simulateDeck(instructions, deck);
            }
            return deck.get(position);
        }
        // Model each shuffle as an affine transformation on card positions: f(x) = a*x + b (mod deckSize)
        // Compose all instructions into a single (a, b) pair
        long a = 1;
        long b = 0;
        for (String instruction : instructions) {
            if (instruction.equals("deal into new stack")) {
                a = mod(-a, deckSize);
                b = mod(-b - 1, deckSize);
            } else if (instruction.startsWith("cut ")) {
                long n = Long.parseLong(instruction.substring(4));
                b = mod(b - n, deckSize);
            } else if (instruction.startsWith("deal with increment ")) {
                long n = Long.parseLong(instruction.substring(20));
                a = mulMod(a, n, deckSize);
                b = mulMod(b, n, deckSize);
            }
        }
        // After 'shuffles' repetitions, f^n(x) = a^n * x + b*(a^n - 1)/(a-1)
        long aN = modPow(a, shuffles, deckSize);
        // bN = b * (a^n - 1) / (a - 1) mod deckSize
        long bN = mulMod(b, mulMod(mod(aN - 1, deckSize), modInverse(mod(a - 1, deckSize), deckSize), deckSize), deckSize);
        // We want x such that a^n * x + bN = position (mod deckSize)
        return mod(mulMod(mod(position - bN, deckSize), modInverse(aN, deckSize), deckSize), deckSize);
    }

    /**
     * Computes (a * b) mod m without overflow using BigInteger.
     * @param a the first factor
     * @param b the second factor
     * @param m the modulus
     * @return (a * b) mod m
     */
    private static long mulMod(long a, long b, long m) {
        return java.math.BigInteger.valueOf(a)
                .multiply(java.math.BigInteger.valueOf(b))
                .mod(java.math.BigInteger.valueOf(m))
                .longValue();
    }

    /**
     * Simulates the deck after applying all shuffle instructions (for testing).
     * @param instructions the shuffle instructions
     * @param deckSize the size of the deck
     * @return the deck as a list of integers
     */
    @NotNull
    public static List<Integer> simulateDeck(@NotNull List<String> instructions, int deckSize) {
        List<Integer> deck = new ArrayList<>(deckSize);
        for (int i = 0; i < deckSize; i++) {
            deck.add(i);
        }
        for (String instruction : instructions) {
            if ("deal into new stack".equals(instruction)) {
                reverse(deck);
            } else if (instruction.startsWith("cut ")) {
                int n = Integer.parseInt(instruction.substring(4));
                cut(deck, n);
            } else if (instruction.startsWith("deal with increment ")) {
                int n = Integer.parseInt(instruction.substring(20));
                dealWithIncrement(deck, n);
            }
        }
        return deck;
    }

    // Overload for simulateDeck to accept a starting deck (for brute-force repeated shuffles)
    private static List<Integer> simulateDeck(List<String> instructions, List<Integer> startDeck) {
        List<Integer> deck = new ArrayList<>(startDeck);
        for (String instruction : instructions) {
            if ("deal into new stack".equals(instruction)) {
                java.util.Collections.reverse(deck);
            } else if (instruction.startsWith("cut ")) {
                int n = Integer.parseInt(instruction.substring(4));
                int size = deck.size();
                n = ((n % size) + size) % size;
                List<Integer> cut = new ArrayList<>(deck.subList(0, n));
                deck.subList(0, n).clear();
                deck.addAll(cut);
            } else if (instruction.startsWith("deal with increment ")) {
                int n = Integer.parseInt(instruction.substring(20));
                int size = deck.size();
                List<Integer> newDeck = new ArrayList<>(deck);
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

    private static void reverse(List<Integer> deck) {
        int n = deck.size();
        for (int i = 0; i < n / 2; i++) {
            int tmp = deck.get(i);
            deck.set(i, deck.get(n - 1 - i));
            deck.set(n - 1 - i, tmp);
        }
    }

    private static void cut(List<Integer> deck, int n) {
        int size = deck.size();
        n = ((n % size) + size) % size;
        List<Integer> cut = new ArrayList<>(deck.subList(0, n));
        deck.subList(0, n).clear();
        deck.addAll(cut);
    }

    private static void dealWithIncrement(List<Integer> deck, int increment) {
        int size = deck.size();
        List<Integer> newDeck = new ArrayList<>(deck);
        for (int i = 0; i < size; i++) {
            newDeck.set((i * increment) % size, deck.get(i));
        }
        for (int i = 0; i < size; i++) {
            deck.set(i, newDeck.get(i));
        }
    }

    private static long mod(long x, long m) {
        x = x % m;
        if (x < 0) x += m;
        return x;
    }

    private static long modPow(long base, long exp, long mod) {
        long result = 1;
        base = base % mod;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                result = mulMod(result, base, mod);
            }
            base = mulMod(base, base, mod);
            exp >>= 1;
        }
        return result;
    }

    private static long modInverse(long n, long mod) {
        // Extended Euclidean Algorithm
        long t = 0, newT = 1;
        long r = mod, newR = n;
        while (newR != 0) {
            long quotient = r / newR;
            long tempT = t - quotient * newT;
            t = newT;
            newT = tempT;
            long tempR = r - quotient * newR;
            r = newR;
            newR = tempR;
        }
        if (r > 1) throw new ArithmeticException("No modular inverse");
        if (t < 0) t += mod;
        return t;
    }
}
