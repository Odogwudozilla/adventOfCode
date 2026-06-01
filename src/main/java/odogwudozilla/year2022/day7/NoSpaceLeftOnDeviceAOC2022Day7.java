package odogwudozilla.year2022.day7;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Advent of Code 2022 - Day 7: No Space Left On Device
 * <p>
 * Puzzle URL: https://adventofcode.com/2022/day/7
 */
public final class NoSpaceLeftOnDeviceAOC2022Day7 {

    private static final String INPUT_FILE = "/2022/day7/day7_puzzle_data.txt";
    private static final long MAX_QUALIFYING_DIRECTORY_SIZE = 100_000L;
    private static final long TOTAL_DISK_SPACE = 70_000_000L;
    private static final long REQUIRED_UNUSED_SPACE = 30_000_000L;
    private static final String ROOT_PATH = "/";
    private static final String COMMAND_PREFIX = "$";
    private static final String CHANGE_DIRECTORY_COMMAND = "$ cd ";
    private static final String LIST_DIRECTORY_COMMAND = "$ ls";
    private static final String DIRECTORY_LISTING_PREFIX = "dir ";
    private static final String PARENT_DIRECTORY = "..";
    private static final String SPACE_DELIMITER = " ";

    /**
     * Entry point. Reads puzzle input and prints solutions for Part 1 and Part 2.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        List<String> input = readInput();
        NoSpaceLeftOnDeviceAOC2022Day7 solver = new NoSpaceLeftOnDeviceAOC2022Day7();
        System.out.println("Part 1: " + solver.solvePartOne(input));
        System.out.println("Part 2: " + solver.solvePartTwo(input));
    }

    /**
     * Solves Part 1 of the puzzle.
     * @param input list of input lines
     * @return the Part 1 answer
     */
    public String solvePartOne(List<String> input) {
        Map<String, Long> directoryTotals = buildDirectoryTotals(input);

        long qualifyingTotal = 0L;
        for (long directorySize : directoryTotals.values()) {
            if (directorySize <= MAX_QUALIFYING_DIRECTORY_SIZE) {
                qualifyingTotal += directorySize;
            }
        }

        return String.valueOf(qualifyingTotal);
    }

    /**
     * Parses the transcript and aggregates each file size into the current directory and all ancestors.
     * @param input list of transcript lines
     * @return cumulative directory totals keyed by canonical absolute path
     */
    private static Map<String, Long> buildDirectoryTotals(List<String> input) {
        Deque<String> currentPath = new ArrayDeque<>();
        Map<String, Long> directoryTotals = new HashMap<>();

        for (String rawLine : input) {
            if (rawLine == null) {
                continue;
            }

            String line = rawLine.trim();
            if (line.isEmpty()) {
                continue;
            }

            if (isCommand(line)) {
                if (line.startsWith(CHANGE_DIRECTORY_COMMAND)) {
                    applyCd(line.substring(CHANGE_DIRECTORY_COMMAND.length()), currentPath);
                }
                if (LIST_DIRECTORY_COMMAND.equals(line)) {
                    continue;
                }
                continue;
            }

            if (line.startsWith(DIRECTORY_LISTING_PREFIX)) {
                continue;
            }

            long fileSize = parseFileSize(line);
            addSizeToAncestors(fileSize, currentPath, directoryTotals);
        }

        return directoryTotals;
    }

    /**
     * Returns whether a transcript line is a shell command.
     * @param line transcript line
     * @return {@code true} if the line starts with the shell command prefix
     */
    private static boolean isCommand(String line) {
        return line.startsWith(COMMAND_PREFIX);
    }

    /**
     * Applies a {@code cd} argument to the current directory stack.
     * The stack stores path segments beneath the root directory.
     * @param argument target directory argument from the transcript
     * @param path current directory stack
     */
    private static void applyCd(String argument, Deque<String> path) {
        if (ROOT_PATH.equals(argument)) {
            path.clear();
            return;
        }
        if (PARENT_DIRECTORY.equals(argument)) {
            if (!path.isEmpty()) {
                path.removeLast();
            }
            return;
        }
        path.addLast(argument);
    }

    /**
     * Parses the leading numeric size from a file listing entry.
     * @param line file listing such as {@code "12345 filename.txt"}
     * @return parsed file size
     */
    private static long parseFileSize(String line) {
        int firstSpaceIndex = line.indexOf(SPACE_DELIMITER);
        return Long.parseLong(line.substring(0, firstSpaceIndex));
    }

    /**
     * Adds a file size to the root directory and every ancestor directory on the current path.
     * @param fileSize size of the current file entry
     * @param path current directory stack beneath root
     * @param totals cumulative directory totals keyed by canonical absolute path
     */
    private static void addSizeToAncestors(long fileSize, Deque<String> path, Map<String, Long> totals) {
        totals.merge(ROOT_PATH, fileSize, Long::sum);

        List<String> segments = new ArrayList<>(path);
        StringBuilder pathBuilder = new StringBuilder();
        for (String segment : segments) {
            if (pathBuilder.isEmpty()) {
                pathBuilder.append(ROOT_PATH).append(segment);
            } else {
                pathBuilder.append(ROOT_PATH).append(segment);
            }
            totals.merge(pathBuilder.toString(), fileSize, Long::sum);
        }
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    public String solvePartTwo(List<String> input) {
        Map<String, Long> directoryTotals = buildDirectoryTotals(input);
        long rootSize = directoryTotals.getOrDefault(ROOT_PATH, 0L);
        long currentUnusedSpace = TOTAL_DISK_SPACE - rootSize;
        long additionalSpaceRequired = REQUIRED_UNUSED_SPACE - currentUnusedSpace;

        long smallestDirectoryToDelete = Long.MAX_VALUE;
        for (long directorySize : directoryTotals.values()) {
            if (directorySize >= additionalSpaceRequired && directorySize < smallestDirectoryToDelete) {
                smallestDirectoryToDelete = directorySize;
            }
        }

        if (smallestDirectoryToDelete == Long.MAX_VALUE) {
            throw new IllegalStateException("No directory is large enough to free the required space");
        }

        return String.valueOf(smallestDirectoryToDelete);
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try (InputStream stream = NoSpaceLeftOnDeviceAOC2022Day7.class.getResourceAsStream(INPUT_FILE)) {
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
