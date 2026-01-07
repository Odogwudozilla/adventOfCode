package odogwudozilla.year2017.day9;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;

/**
 * Stream Processing (Advent of Code 2017 Day 9)
 * <p>
 * A large stream blocks your path. The characters represent groups - sequences that begin with { and end with }.
 * Groups can contain other groups or garbage. Garbage begins with < and ends with >. Within garbage, ! cancels the next character.
 * Your goal is to find the total score for all groups in your input. Each group is assigned a score which is one more than the score of the group that immediately contains it. (The outermost group gets a score of 1.)
 * <p>
 * Official puzzle link: https://adventofcode.com/2017/day/9
 *
 * @author Advent of Code workflow
 */
public class StreamProcessingAOC2017Day9 {
    /**
     * Main entry point for the solution.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        String inputPath = "src/main/resources/2017/day9/day9_puzzle_data.txt";
        try {
            List<String> lines = Files.readAllLines(Paths.get(inputPath));
            String stream = String.join("", lines);
            int partOneResult = solvePartOne(stream);
            System.out.println("solvePartOne - Total score for all groups: " + partOneResult);
            int partTwoResult = solvePartTwo(stream);
            System.out.println("solvePartTwo - Non-canceled garbage characters: " + partTwoResult);
        } catch (IOException e) {
            System.err.println("main - Error reading input file: " + e.getMessage());
        }
    }

    /**
     * Calculates the total score for all groups in the input stream.
     * @param stream The input stream as a String
     * @return The total score for all groups
     */
    public static int solvePartOne(String stream) {
        int score = 0;
        int depth = 0;
        boolean inGarbage = false;
        boolean skipNext = false;
        for (int i = 0; i < stream.length(); i++) {
            char c = stream.charAt(i);
            if (skipNext) {
                skipNext = false;
                continue;
            }
            if (inGarbage) {
                if (c == '!') {
                    skipNext = true;
                } else if (c == '>') {
                    inGarbage = false;
                }
                // Ignore all other characters inside garbage
            } else {
                if (c == '<') {
                    inGarbage = true;
                } else if (c == '{') {
                    depth++;
                } else if (c == '}') {
                    score += depth;
                    depth--;
                }
                // Ignore commas and other characters outside garbage
            }
        }
        return score;
    }

    /**
     * Counts the number of non-canceled characters within garbage in the input stream.
     * @param stream The input stream as a String
     * @return The count of non-canceled garbage characters
     */
    public static int solvePartTwo(String stream) {
        int garbageCount = 0;
        boolean inGarbage = false;
        boolean skipNext = false;
        for (int i = 0; i < stream.length(); i++) {
            char c = stream.charAt(i);
            if (skipNext) {
                skipNext = false;
                continue;
            }
            if (inGarbage) {
                if (c == '!') {
                    skipNext = true;
                } else if (c == '>') {
                    inGarbage = false;
                } else {
                    garbageCount++;
                }
            } else {
                if (c == '<') {
                    inGarbage = true;
                }
            }
        }
        return garbageCount;
    }
}

