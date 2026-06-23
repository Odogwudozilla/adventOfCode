package odogwudozilla.year2015.day8;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Advent of Code 2015 - Day 8: Matchsticks
 * <p>
 * Puzzle URL: https://adventofcode.com/2015/day/8
 */
public final class MatchsticksAOC2015Day8 {

    private static final String INPUT_FILE = "/2015/day8/day8_puzzle_data.txt";
    private static final char BACKSLASH = '\\';
    private static final char DOUBLE_QUOTE = '"';
    private static final char HEX_ESCAPE_PREFIX = 'x';
    private static final int SIMPLE_ESCAPE_SOURCE_LENGTH = 2;
    private static final int HEX_ESCAPE_SOURCE_LENGTH = 4;
    private static final int FIRST_CONTENT_INDEX = 1;
    private static final int CLOSING_QUOTE_OFFSET = 1;
    private static final int ENCODED_WRAPPING_QUOTES = 2;
    private static final int ENCODED_ESCAPED_CHARACTER_LENGTH = 2;
    private static final int ENCODED_PLAIN_CHARACTER_LENGTH = 1;

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
    public static String solvePartOne(List<String> input) {
        int totalCodeCharacters = 0;
        int totalInMemoryCharacters = 0;

        for (String rawLine : input) {
            String line = rawLine.trim();
            if (line.isEmpty()) {
                continue;
            }
            totalCodeCharacters += line.length();
            totalInMemoryCharacters += countInMemoryCharacters(line);
        }

        return String.valueOf(totalCodeCharacters - totalInMemoryCharacters);
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    public static String solvePartTwo(List<String> input) {
        int totalOriginalCodeCharacters = 0;
        int totalEncodedCharacters = 0;

        for (String rawLine : input) {
            String line = rawLine.trim();
            if (line.isEmpty()) {
                continue;
            }
            totalOriginalCodeCharacters += line.length();
            totalEncodedCharacters += countEncodedCharacters(line);
        }

        return String.valueOf(totalEncodedCharacters - totalOriginalCodeCharacters);
    }

    /**
     * Counts the number of characters represented in memory by one quoted literal.
     *
     * @param literal the raw string literal including surrounding quotes
     * @return decoded in-memory character count
     */
    private static int countInMemoryCharacters(String literal) {
        int inMemoryCharacters = 0;
        int index = FIRST_CONTENT_INDEX;
        int endExclusive = literal.length() - CLOSING_QUOTE_OFFSET;

        while (index < endExclusive) {
            char currentCharacter = literal.charAt(index);
            if (currentCharacter != BACKSLASH) {
                inMemoryCharacters++;
                index++;
                continue;
            }

            char escapedCharacter = literal.charAt(index + 1);
            if (escapedCharacter == BACKSLASH || escapedCharacter == DOUBLE_QUOTE) {
                inMemoryCharacters++;
                index += SIMPLE_ESCAPE_SOURCE_LENGTH;
                continue;
            }

            if (escapedCharacter == HEX_ESCAPE_PREFIX) {
                inMemoryCharacters++;
                index += HEX_ESCAPE_SOURCE_LENGTH;
                continue;
            }

            throw new IllegalArgumentException("Unsupported escape sequence in literal: " + literal);
        }

        return inMemoryCharacters;
    }

    /**
     * Counts the number of characters required to encode one literal again.
     *
     * @param literal the original raw string literal including surrounding quotes
     * @return re-encoded character count
     */
    private static int countEncodedCharacters(String literal) {
        int encodedCharacters = ENCODED_WRAPPING_QUOTES;

        for (int index = 0; index < literal.length(); index++) {
            char currentCharacter = literal.charAt(index);
            if (currentCharacter == BACKSLASH || currentCharacter == DOUBLE_QUOTE) {
                encodedCharacters += ENCODED_ESCAPED_CHARACTER_LENGTH;
                continue;
            }
            encodedCharacters += ENCODED_PLAIN_CHARACTER_LENGTH;
        }

        return encodedCharacters;
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = MatchsticksAOC2015Day8.class.getResourceAsStream(INPUT_FILE)) {
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
