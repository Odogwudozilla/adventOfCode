package odogwudozilla.year2021.day10;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2021 - Day 10: Syntax Scoring
 * <p>
 * Puzzle URL: https://adventofcode.com/2021/day/10
 */
public final class SyntaxScoringAOC2021Day10 {

    private static final String INPUT_FILE = "/2021/day10/day10_puzzle_data.txt";

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
        // Syntax error scores for each illegal closer
        final java.util.Map<Character, Integer> syntaxErrorScore = java.util.Map.of(
                ')', 3,
                ']', 57,
                '}', 1197,
                '>', 25137
        );
        final java.util.Map<Character, Character> openerToCloser = java.util.Map.of(
                '(', ')',
                '[', ']',
                '{', '}',
                '<', '>'
        );
        int totalScore = 0;
        for (String line : input) {
            java.util.Stack<Character> stack = new java.util.Stack<>();
            boolean corrupted = false;
            for (char c : line.toCharArray()) {
                if (openerToCloser.containsKey(c)) {
                    stack.push(c);
                } else if (openerToCloser.containsValue(c)) {
                    if (stack.isEmpty() || openerToCloser.get(stack.peek()) != c) {
                        // First illegal character
                        totalScore += syntaxErrorScore.getOrDefault(c, 0);
                        corrupted = true;
                        break;
                    } else {
                        stack.pop();
                    }
                }
            }
            // Incomplete lines are ignored for Part 1
        }
        return String.valueOf(totalScore);
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    private static String solvePartTwo(List<String> input) {
        final java.util.Map<Character, Character> openerToCloser = java.util.Map.of(
                '(', ')',
                '[', ']',
                '{', '}',
                '<', '>'
        );
        final java.util.Map<Character, Integer> completionScore = java.util.Map.of(
                ')', 1,
                ']', 2,
                '}', 3,
                '>', 4
        );
        java.util.List<Long> scores = new java.util.ArrayList<>();
        for (String line : input) {
            java.util.Stack<Character> stack = new java.util.Stack<>();
            boolean corrupted = false;
            for (char c : line.toCharArray()) {
                if (openerToCloser.containsKey(c)) {
                    stack.push(c);
                } else if (openerToCloser.containsValue(c)) {
                    if (stack.isEmpty() || openerToCloser.get(stack.peek()) != c) {
                        corrupted = true;
                        break;
                    } else {
                        stack.pop();
                    }
                }
            }
            if (!corrupted && !stack.isEmpty()) {
                long score = 0;
                while (!stack.isEmpty()) {
                    char opener = stack.pop();
                    char closer = openerToCloser.get(opener);
                    score = score * 5 + completionScore.get(closer);
                }
                scores.add(score);
            }
        }
        java.util.Collections.sort(scores);
        return String.valueOf(scores.get(scores.size() / 2));
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = SyntaxScoringAOC2021Day10.class.getResourceAsStream(INPUT_FILE)) {
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
