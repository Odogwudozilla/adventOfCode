package odogwudozilla.year2022.day5;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2022 - Day 5: Supply Stacks
 * <p>
 * Puzzle URL: https://adventofcode.com/2022/day/5
 */
public final class SupplyStacksAOC2022Day5 {

    private static final String INPUT_FILE = "/2022/day5/day5_puzzle_data.txt";

    /** Character index of the crate letter within a fixed-width column block "[X]". */
    private static final int CRATE_LETTER_POSITION = 1;

    /** Number of characters between the start of consecutive stack columns in the drawing. */
    private static final int CHARS_PER_STACK_COLUMN = 4;

    /** Index into the split instruction tokens array for the move count. */
    private static final int TOKEN_COUNT = 1;

    /** Index into the split instruction tokens array for the source stack number. */
    private static final int TOKEN_FROM = 2;

    /** Index into the split instruction tokens array for the destination stack number. */
    private static final int TOKEN_TO = 3;

    /**
     * Offset applied when converting puzzle stack numbers (1-based) to Java array indices (0-based).
     * Puzzle instructions reference stacks starting at 1; the internal {@code stacks} array is 0-indexed.
     */
    private static final int STACK_INDEX_OFFSET = 1;

    /**
     * Entry point. Reads puzzle input and prints solutions for Part 1 and Part 2.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        List<String> input = readInput();
        SupplyStacksAOC2022Day5 solver = new SupplyStacksAOC2022Day5();
        System.out.println("Part 1: " + solver.solvePartOne(input));
        System.out.println("Part 2: " + solver.solvePartTwo(input));
    }

    /**
     * Solves Part 1: simulates a crane that moves crates one at a time (LIFO) and
     * returns the concatenated top-of-stack letters after all moves.
     * @param input list of input lines
     * @return concatenated top crate letters in ascending stack-number order
     */
    public String solvePartOne(@NotNull List<String> input) {
        int separatorIndex = findSeparatorIndex(input);

        List<String> drawingLines = input.subList(0, separatorIndex);
        @SuppressWarnings("unchecked")
        Deque<Character>[] stacks = parseStacks(drawingLines);

        for (int i = separatorIndex + 1; i < input.size(); i++) {
            String line = input.get(i);
            if (line.isBlank()) {
                continue;
            }
            int[] move = parseMove(line);
            int count = move[TOKEN_COUNT];
            int from  = move[TOKEN_FROM] - STACK_INDEX_OFFSET;
            int to    = move[TOKEN_TO]   - STACK_INDEX_OFFSET;

            for (int j = 0; j < count; j++) {
                stacks[to].push(stacks[from].pop());
            }
        }

        return buildResult(stacks);
    }

    /**
     * Solves Part 2: simulates a CrateMover 9001 crane that moves crates as a group,
     * preserving their original relative order, and returns the concatenated top-of-stack
     * letters after all moves.
     * <p>
     * Each move pops {@code count} crates from the source into a temporary {@link ArrayDeque}
     * (reversing order once), then pops all from the buffer onto the destination (reversing
     * again), restoring the original order — a double-reversal technique.
     * @param input list of input lines
     * @return concatenated top crate letters in ascending stack-number order
     */
    public String solvePartTwo(@NotNull List<String> input) {
        int separatorIndex = findSeparatorIndex(input);

        List<String> drawingLines = input.subList(0, separatorIndex);
        @SuppressWarnings("unchecked")
        Deque<Character>[] stacks = parseStacks(drawingLines);

        for (int i = separatorIndex + 1; i < input.size(); i++) {
            String line = input.get(i);
            if (line.isBlank()) {
                continue;
            }
            int[] move = parseMove(line);
            int count = move[TOKEN_COUNT];
            int from  = move[TOKEN_FROM] - STACK_INDEX_OFFSET;
            int to    = move[TOKEN_TO]   - STACK_INDEX_OFFSET;

            // Pop into a temporary buffer (reverses order once)
            Deque<Character> tempBuffer = new ArrayDeque<>();
            for (int j = 0; j < count; j++) {
                tempBuffer.push(stacks[from].pop());
            }

            // Pop from buffer onto destination (reverses again, restoring original order)
            while (!tempBuffer.isEmpty()) {
                stacks[to].push(tempBuffer.pop());
            }
        }

        return buildResult(stacks);
    }

    /**
     * Finds the index of the blank separator line that divides the drawing section
     * from the instruction section.
     * @param input full list of input lines
     * @return index of the blank line
     */
    private static int findSeparatorIndex(@NotNull List<String> input) {
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i).isBlank()) {
                return i;
            }
        }
        throw new IllegalArgumentException("No blank separator line found in input");
    }

    /**
     * Parses the fixed-width ASCII crate drawing into an array of stacks.
     * The drawing lines include the stack-label row as the final element.
     * Rows are read bottom-to-top (excluding the label row) so that each deque's
     * front represents the top crate.
     * @param drawingLines input lines from the start of the file up to (excluding) the blank line
     * @return array of {@link Deque} instances, one per stack, indexed 0-based
     */
    @SuppressWarnings("unchecked")
    private static Deque<Character>[] parseStacks(@NotNull List<String> drawingLines) {
        // The last line of the drawing section is the label row, e.g. " 1   2   3 "
        String labelRow = drawingLines.get(drawingLines.size() - 1);
        int stackCount = labelRow.trim().split("\\s+").length;

        Deque<Character>[] stacks = new ArrayDeque[stackCount];
        for (int i = 0; i < stackCount; i++) {
            stacks[i] = new ArrayDeque<>();
        }

        // Iterate from the row immediately above the label row upward to the top of the drawing
        for (int row = drawingLines.size() - 2; row >= 0; row--) {
            String line = drawingLines.get(row);
            for (int stackIdx = 0; stackIdx < stackCount; stackIdx++) {
                int charIndex = CRATE_LETTER_POSITION + stackIdx * CHARS_PER_STACK_COLUMN;
                if (charIndex < line.length()) {
                    char letter = line.charAt(charIndex);
                    if (Character.isLetter(letter)) {
                        stacks[stackIdx].push(letter);
                    }
                }
            }
        }

        return stacks;
    }

    /**
     * Parses a single move instruction line of the form {@code "move N from X to Y"}
     * and returns an int array whose non-zero indices match the TOKEN_* constants.
     * Index 0 is a dummy placeholder ({@code 0}) produced by the leading non-digit
     * characters; index 1 is count, index 2 is source stack number, index 3 is
     * destination stack number.
     * @param line instruction line to parse
     * @return int array of length 4: {@code [0, count, from, to]}
     */
    private static int[] parseMove(@NotNull String line) {
        // "move 3 from 1 to 2".split("\\D+") → ["", "3", "1", "2"]
        String[] tokens = line.split("\\D+");
        return new int[]{0, Integer.parseInt(tokens[TOKEN_COUNT]), Integer.parseInt(tokens[TOKEN_FROM]), Integer.parseInt(tokens[TOKEN_TO])};
    }

    /**
     * Builds the result string by peeking the top element of each stack in order.
     * @param stacks array of crate stacks
     * @return concatenated top-of-stack letters as a {@link String}
     */
    private static String buildResult(@NotNull Deque<Character>[] stacks) {
        StringBuilder sb = new StringBuilder();
        for (Deque<Character> stack : stacks) {
            if (!stack.isEmpty()) {
                sb.append(stack.peek());
            }
        }
        return sb.toString();
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = SupplyStacksAOC2022Day5.class.getResourceAsStream(INPUT_FILE)) {
            if (stream == null) {
                throw new IllegalStateException("Input file not found: " + INPUT_FILE);
            }
            Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8);
            List<String> lines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            return lines;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read input file", e);
        }
    }
}
