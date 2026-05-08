package odogwudozilla.year2018.day9;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2018 - Day 9: Marble Mania
 * <p>
 * Puzzle URL: https://adventofcode.com/2018/day/9
 */
public final class MarbleManiaAOC2018Day9 {

    private static final String INPUT_FILE = "/2018/day9/day9_puzzle_data.txt";

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
     * Solves Part 1 of the puzzle.
     * @param input list of input lines
     * @return the Part 1 answer
     */
    private static String solvePartOne(List<String> input) {
        // Parse input: e.g. "464 players; last marble is worth 70918 points"
        String line = input.get(0).trim();
        String[] tokens = line.split(" ");
        int players = Integer.parseInt(tokens[0]);
        int lastMarble = Integer.parseInt(tokens[6]);
        return String.valueOf(playGame(players, lastMarble));
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        // Part 2: last marble value is 100x larger
        String line = input.get(0).trim();
        String[] tokens = line.split(" ");
        int players = Integer.parseInt(tokens[0]);
        int lastMarble = Integer.parseInt(tokens[6]) * 100;
        return String.valueOf(playGame(players, lastMarble));
    }

    /**
     * Simulates the marble game and returns the highest score.
     * @param players number of players
     * @param lastMarble value of the last marble
     * @return highest score
     */
    private static long playGame(int players, int lastMarble) {
        // Use a circular doubly-linked list for efficiency
        class Node {
            long value;
            Node prev, next;
            Node(long value) { this.value = value; }
        }
        Node current = new Node(0);
        current.prev = current;
        current.next = current;
        long[] scores = new long[players];
        for (int marble = 1; marble <= lastMarble; marble++) {
            if (marble % 23 == 0) {
                int player = (marble - 1) % players;
                scores[player] += marble;
                // Move 7 counter-clockwise
                for (int i = 0; i < 7; i++) current = current.prev;
                scores[player] += current.value;
                // Remove current node
                current.prev.next = current.next;
                current.next.prev = current.prev;
                current = current.next;
            } else {
                // Insert new marble between current.next and current.next.next
                Node n1 = current.next;
                Node n2 = n1.next;
                Node newNode = new Node(marble);
                n1.next = newNode;
                newNode.prev = n1;
                newNode.next = n2;
                n2.prev = newNode;
                current = newNode;
            }
        }
        long max = 0;
        for (long score : scores) if (score > max) max = score;
        return max;
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = MarbleManiaAOC2018Day9.class.getResourceAsStream(INPUT_FILE)) {
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
