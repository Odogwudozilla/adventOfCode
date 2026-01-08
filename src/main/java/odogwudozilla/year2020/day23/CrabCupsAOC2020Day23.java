package odogwudozilla.year2020.day23;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Crab Cups (Advent of Code 2020 Day 23)
 * <p>
 * The crab mixes up cups in a circle, performing moves as described. Part 1: After 100 moves, what is the order of cups after cup 1? Part 2: After 10 million moves with 1 million cups, what is the product of the two cups immediately clockwise of cup 1?
 * <p>
 * @see <a href="https://adventofcode.com/2020/day/23">Official puzzle link</a>
 *
 * @author Advent of Code workflow
 */
public class CrabCupsAOC2020Day23 {
    /**
     * Main entry point for the solution.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        String inputPath = "src/main/resources/2020/day23/day23_puzzle_data.txt";
        try {
            List<String> lines = Files.readAllLines(Paths.get(inputPath));
            String input = String.join("", lines).trim();
            String partOneResult = solvePartOne(input);
            System.out.println("solvePartOne - Order after cup 1: " + partOneResult);
            long partTwoResult = solvePartTwo(input);
            System.out.println("solvePartTwo - Product of two cups after cup 1: " + partTwoResult);
        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Simulates 100 moves and returns the order of cups after cup 1.
     * @param input The initial cup labels
     * @return The order of cups after cup 1
     */
    public static String solvePartOne(String input) {
        final int MOVES = 100;
        LinkedList<Integer> cups = new LinkedList<>();
        for (char c : input.toCharArray()) {
            cups.add(Character.getNumericValue(c));
        }
        int min = cups.stream().min(Integer::compareTo).isPresent() ? cups.stream().min(Integer::compareTo).get() : 1;
        int max = cups.stream().max(Integer::compareTo).isPresent() ? cups.stream().max(Integer::compareTo).get() : 9;
        int currentIdx = 0;
        for (int move = 0; move < MOVES; move++) {
            int currentCup = cups.get(currentIdx);
            // Pick up next three cups
            List<Integer> pickUp = new LinkedList<>();
            for (int i = 0; i < 3; i++) {
                int idx = (currentIdx + 1) % cups.size();
                pickUp.add(cups.remove(idx));
                if (idx < currentIdx) currentIdx--;
            }
            // Select destination cup
            int destLabel = currentCup - 1;
            while (destLabel < min || pickUp.contains(destLabel)) {
                if (destLabel < min) destLabel = max;
                else destLabel--;
            }
            int destIdx = cups.indexOf(destLabel);
            cups.addAll(destIdx + 1, pickUp);
            currentIdx = (cups.indexOf(currentCup) + 1) % cups.size();
        }
        // Build result string after cup 1
        int idx1 = cups.indexOf(1);
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < cups.size(); i++) {
            sb.append(cups.get((idx1 + i) % cups.size()));
        }
        return sb.toString();
    }

    /**
     * Simulates 10 million moves with 1 million cups and returns the product of the two cups after cup 1.
     * @param input The initial cup labels
     * @return The product of the two cups after cup 1
     */
    public static long solvePartTwo(String input) {
        final int TOTAL_CUPS = 1000000;
        final int MOVES = 10000000;
        int[] next = new int[TOTAL_CUPS + 1];
        int prev = -1;
        int first = Character.getNumericValue(input.charAt(0));
        for (char c : input.toCharArray()) {
            int val = Character.getNumericValue(c);
            if (prev != -1) next[prev] = val;
            prev = val;
        }
        for (int i = input.length() + 1; i <= TOTAL_CUPS; i++) {
            next[prev] = i;
            prev = i;
        }
        next[prev] = first;
        int current = first;
        for (int move = 0; move < MOVES; move++) {
            int pick1 = next[current];
            int pick2 = next[pick1];
            int pick3 = next[pick2];
            int afterPick = next[pick3];
            int dest = current - 1;
            if (dest == 0) dest = TOTAL_CUPS;
            while (dest == pick1 || dest == pick2 || dest == pick3) {
                dest--;
                if (dest == 0) dest = TOTAL_CUPS;
            }
            next[current] = afterPick;
            next[pick3] = next[dest];
            next[dest] = pick1;
            current = next[current];
        }
        long cup1 = next[1];
        long cup2 = next[(int)cup1];
        return cup1 * cup2;
    }
}
