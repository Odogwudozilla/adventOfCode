package odogwudozilla.year2016.day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Explosives in Cyberspace (Advent of Code 2016 Day 9)
 * Wandering around a secure area, you come across a datalink port to a new part of the network. After briefly scanning it for interesting files, you find one file in particular that catches your attention. It's compressed with an experimental format, but fortunately, the documentation for the format is nearby.
 * The format compresses a sequence of characters. Whitespace is ignored. To indicate that some sequence should be repeated, a marker is added to the file, like (10x2). To decompress this marker, take the subsequent 10 characters and repeat them 2 times. Then, continue reading the file after the repeated data. The marker itself is not included in the decompressed output.
 * For full details, see: <a href="https://adventofcode.com/2016/day/9">https://adventofcode.com/2016/day/9</a>
 */
public class ExplosivesInCyberspaceAOC2016Day9 {
    private static final String INPUT_PATH = "src/main/resources/2016/day9/day9_puzzle_data.txt";

    /**
     * Main entry point for the solution.
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get(INPUT_PATH)).replaceAll("\\s", "");
        System.out.println("Part 1 decompressed length: " + solvePartOne(input));
        System.out.println("Part 2 decompressed length: " + solvePartTwo(input));
    }

    /**
     * Computes the decompressed length of the input (non-recursive markers).
     * @param input the compressed input string
     * @return the decompressed length
     */
    public static long solvePartOne(String input) {
        long length = 0;
        int i = 0;
        while (i < input.length()) {
            if (input.charAt(i) == '(') {
                int close = input.indexOf(')', i);
                String marker = input.substring(i + 1, close);
                String[] parts = marker.split("x");
                int chars = Integer.parseInt(parts[0]);
                int repeat = Integer.parseInt(parts[1]);
                i = close + 1 + chars;
                length += (long) chars * repeat;
            } else {
                length++;
                i++;
            }
        }
        return length;
    }

    /**
     * Computes the decompressed length of the input (recursive markers, version 2).
     * @param input the compressed input string
     * @return the decompressed length (version 2)
     */
    public static long solvePartTwo(String input) {
        return decompressedLength(input, 0, input.length());
    }

    /**
     * Recursively computes the decompressed length for a substring.
     * @param input the compressed input string
     * @param start start index (inclusive)
     * @param end end index (exclusive)
     * @return the decompressed length
     */
    private static long decompressedLength(String input, int start, int end) {
        long length = 0;
        int i = start;
        while (i < end) {
            if (input.charAt(i) == '(') {
                int close = input.indexOf(')', i);
                String marker = input.substring(i + 1, close);
                String[] parts = marker.split("x");
                int chars = Integer.parseInt(parts[0]);
                int repeat = Integer.parseInt(parts[1]);
                int sectionStart = close + 1;
                int sectionEnd = sectionStart + chars;
                length += (long) repeat * decompressedLength(input, sectionStart, sectionEnd);
                i = sectionEnd;
            } else {
                length++;
                i++;
            }
        }
        return length;
    }
}
