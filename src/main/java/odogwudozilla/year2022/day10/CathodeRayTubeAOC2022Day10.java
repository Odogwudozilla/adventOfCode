package odogwudozilla.year2022.day10;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Advent of Code 2022 - Day 10: Cathode-Ray Tube
 * <p>
 * Puzzle URL: https://adventofcode.com/2022/day/10
 */
public final class CathodeRayTubeAOC2022Day10 {

    private static final String INPUT_FILE = "/2022/day10/day10_puzzle_data.txt";
    private static final int INITIAL_X = 1;
    private static final int CRT_WIDTH = 40;
    private static final int CRT_HEIGHT = 6;
    private static final int CRT_PIXELS = CRT_WIDTH * CRT_HEIGHT;
    private static final int[] SAMPLE_CYCLES = {20, 60, 100, 140, 180, 220};

    /** Width of each letter glyph in the AoC 4×6 pixel font. */
    private static final int LETTER_WIDTH = 4;
    /** Column stride per letter slot (4 letter pixels + 1 gap pixel). */
    private static final int LETTER_STRIDE = 5;
    /** Number of letters rendered across the 40-column screen. */
    private static final int NUM_LETTERS = CRT_WIDTH / LETTER_STRIDE;

    /**
     * Lookup map from a 24-character pixel key (6 rows × 4 columns, row-major) to
     * the corresponding capital letter in the AoC 4×6 pixel font.
     */
    private static final Map<String, Character> LETTER_PATTERNS = buildLetterPatterns();

    /**
     * Entry point. Reads puzzle input, prints the CRT image for human inspection,
     * then prints the OCR-decoded letter string as the Part 2 answer.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        List<String> input = readInput();
        System.out.println("Part 1: " + solvePartOne(input));
        char[][] screen = buildScreen(input);
        // Print visual rendering for human inspection (not prefixed, so not captured by SolverRunner)
        System.out.println(renderScreen(screen));
        // Print decoded letter answer for automated capture
        System.out.println("Part 2: " + decodeLetters(screen));
    }

    /**
     * Solves Part 1: sums the signal strengths sampled at cycles 20, 60, 100, 140, 180 and 220.
     *
     * @param input list of input lines
     * @return the Part 1 answer
     */
    public static String solvePartOne(List<String> input) {
        int cycle = 0;
        int x = INITIAL_X;
        int totalSignalStrength = 0;

        for (String line : input) {
            if (line == null || line.isBlank()) {
                continue;
            }

            String[] parts = line.split("\\s+");
            String instruction = parts[0];

            if ("noop".equals(instruction)) {
                cycle++;
                if (isSampleCycle(cycle)) {
                    totalSignalStrength += cycle * x;
                }
                continue;
            }

            int value = Integer.parseInt(parts[1]);

            cycle++;
            if (isSampleCycle(cycle)) {
                totalSignalStrength += cycle * x;
            }

            cycle++;
            if (isSampleCycle(cycle)) {
                totalSignalStrength += cycle * x;
            }

            x += value;
        }

        return String.valueOf(totalSignalStrength);
    }

    /**
     * Solves Part 2: returns the 8 capital letters decoded from the CRT screen via pixel-font OCR.
     *
     * @param input list of input lines
     * @return the decoded letter string (e.g. {@code "EZFCHJAB"})
     */
    public static String solvePartTwo(List<String> input) {
        return decodeLetters(buildScreen(input));
    }

    /**
     * Simulates the CPU instruction stream and renders the 40×6 CRT screen.
     *
     * @param input list of input lines
     * @return the rendered screen as a {@code char[CRT_HEIGHT][CRT_WIDTH]} buffer
     */
    static char[][] buildScreen(List<String> input) {
        char[][] screen = new char[CRT_HEIGHT][CRT_WIDTH];
        for (int row = 0; row < CRT_HEIGHT; row++) {
            for (int column = 0; column < CRT_WIDTH; column++) {
                screen[row][column] = '.';
            }
        }

        int cycle = 0;
        int x = INITIAL_X;

        for (String line : input) {
            if (line == null || line.isBlank()) {
                continue;
            }

            String[] parts = line.split("\\s+");
            String instruction = parts[0];

            if ("noop".equals(instruction)) {
                cycle = drawPixel(screen, cycle, x);
                if (cycle >= CRT_PIXELS) {
                    break;
                }
                continue;
            }

            int value = Integer.parseInt(parts[1]);

            cycle = drawPixel(screen, cycle, x);
            if (cycle >= CRT_PIXELS) {
                break;
            }

            cycle = drawPixel(screen, cycle, x);
            x += value;
            if (cycle >= CRT_PIXELS) {
                break;
            }
        }

        return screen;
    }

    /**
     * Decodes the 8 capital letters from the rendered CRT screen using pixel-font OCR.
     * Each letter slot is {@value #LETTER_STRIDE} columns wide ({@value #LETTER_WIDTH} letter
     * pixels + 1 gap pixel). Unknown glyphs are represented as {@code '?'}.
     *
     * @param screen the 6×40 CRT screen buffer
     * @return the 8-character decoded letter string
     */
    static String decodeLetters(char[][] screen) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < NUM_LETTERS; i++) {
            int startCol = i * LETTER_STRIDE;
            StringBuilder key = new StringBuilder();
            for (int row = 0; row < CRT_HEIGHT; row++) {
                for (int col = startCol; col < startCol + LETTER_WIDTH; col++) {
                    key.append(screen[row][col]);
                }
            }
            Character letter = LETTER_PATTERNS.get(key.toString());
            result.append(letter != null ? letter : '?');
        }
        return result.toString();
    }

    private static int drawPixel(char[][] screen, int cycle, int x) {
        int nextCycle = cycle + 1;
        if (nextCycle > CRT_PIXELS) {
            return nextCycle;
        }

        int pixelIndex = nextCycle - 1;
        int row = pixelIndex / CRT_WIDTH;
        int column = pixelIndex % CRT_WIDTH;

        if (Math.abs(column - x) <= 1) {
            screen[row][column] = '#';
        }

        return nextCycle;
    }

    /**
     * Converts the CRT screen buffer to a newline-separated string for human reading.
     *
     * @param screen the 6×40 CRT screen buffer
     * @return multi-line string representation of the screen
     */
    static String renderScreen(char[][] screen) {
        StringBuilder output = new StringBuilder();
        for (int row = 0; row < CRT_HEIGHT; row++) {
            if (row > 0) {
                output.append(System.lineSeparator());
            }
            output.append(screen[row]);
        }
        return output.toString();
    }

    private static boolean isSampleCycle(int cycle) {
        for (int sampleCycle : SAMPLE_CYCLES) {
            if (sampleCycle == cycle) {
                return true;
            }
        }
        return false;
    }

    /**
     * Builds the pixel-pattern lookup map for the AoC 4×6 capital-letter font.
     * Each key is a 24-character string built by concatenating 6 rows of 4 characters
     * ({@code '#'} for lit, {@code '.'} for dark), in row-major order.
     *
     * @return immutable-style map from pixel key to letter
     */
    private static Map<String, Character> buildLetterPatterns() {
        Map<String, Character> map = new HashMap<>();
        addPattern(map, 'A', ".##.", "#..#", "#..#", "####", "#..#", "#..#");
        addPattern(map, 'B', "###.", "#..#", "###.", "#..#", "#..#", "###.");
        addPattern(map, 'C', ".##.", "#..#", "#...", "#...", "#..#", ".##.");
        addPattern(map, 'E', "####", "#...", "###.", "#...", "#...", "####");
        addPattern(map, 'F', "####", "#...", "###.", "#...", "#...", "#...");
        addPattern(map, 'G', ".##.", "#..#", "#...", "#.##", "#..#", ".###");
        addPattern(map, 'H', "#..#", "#..#", "####", "#..#", "#..#", "#..#");
        addPattern(map, 'I', ".###", "..#.", "..#.", "..#.", "..#.", ".###");
        addPattern(map, 'J', "..##", "...#", "...#", "...#", "#..#", ".##.");
        addPattern(map, 'K', "#..#", "#.#.", "##..", "#.#.", "#.#.", "#..#");
        addPattern(map, 'L', "#...", "#...", "#...", "#...", "#...", "####");
        addPattern(map, 'O', ".##.", "#..#", "#..#", "#..#", "#..#", ".##.");
        addPattern(map, 'P', "###.", "#..#", "#..#", "###.", "#...", "#...");
        addPattern(map, 'R', "###.", "#..#", "#..#", "###.", "#.#.", "#..#");
        addPattern(map, 'S', ".###", "#...", "#...", ".##.", "...#", "###.");
        addPattern(map, 'U', "#..#", "#..#", "#..#", "#..#", "#..#", ".##.");
        addPattern(map, 'Y', "#..#", "#..#", ".##.", "..#.", "..#.", "..#.");
        addPattern(map, 'Z', "####", "...#", "..#.", ".#..", "#...", "####");
        return map;
    }

    private static void addPattern(Map<String, Character> map, char letter,
                                   String r0, String r1, String r2,
                                   String r3, String r4, String r5) {
        map.put(r0 + r1 + r2 + r3 + r4 + r5, letter);
    }

    /**
     * Reads the puzzle input file from the classpath.
     *
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = CathodeRayTubeAOC2022Day10.class.getResourceAsStream(INPUT_FILE)) {
            if (stream == null) {
                throw new IllegalStateException("Input file not found: " + INPUT_FILE);
            }
            Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8);
            java.util.ArrayList<String> lines = new java.util.ArrayList<>();
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            return lines;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read input file", e);
        }
    }
}
