package odogwudozilla.year2017.day12;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Advent of Code 2017 - Day 12: Digital Plumber
 * <p>
 * Puzzle URL: <a href="https://adventofcode.com/2017/day/12">https://adventofcode.com/2017/day/12</a>
 */
public final class DigitalPlumberAOC2017Day12 {

    private static final String INPUT_FILE = "/2017/day12/day12_puzzle_data.txt";
    private static final String PROGRAM_SEPARATOR = "<->";
    private static final String NEIGHBOUR_SEPARATOR = ",";
    private static final int START_PROGRAM_ID = 0;

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
        Map<Integer, List<Integer>> graph = parseGraph(input);
        return String.valueOf(countConnectedComponentSize(START_PROGRAM_ID, graph));
    }

    /**
     * Solves Part 2 of the puzzle.
     * @param input list of input lines
     * @return the Part 2 answer
     */
    public static String solvePartTwo(List<String> input) {
        Map<Integer, List<Integer>> graph = parseGraph(input);
        return String.valueOf(countGroups(graph));
    }

    private static Map<Integer, List<Integer>> parseGraph(List<String> input) {
        Map<Integer, List<Integer>> graph = new HashMap<>();

        for (String line : input) {
            if (line == null || line.trim().isEmpty()) {
                continue;
            }

            String[] sides = line.split(PROGRAM_SEPARATOR);
            if (sides.length != 2) {
                throw new IllegalArgumentException("Invalid program definition: " + line);
            }

            int source = Integer.parseInt(sides[0].trim());
            List<Integer> neighbours = parseNeighbours(sides[1]);

            graph.computeIfAbsent(source, key -> new ArrayList<>());
            for (int neighbour : neighbours) {
                graph.get(source).add(neighbour);
                graph.computeIfAbsent(neighbour, key -> new ArrayList<>()).add(source);
            }
        }

        return graph;
    }

    private static List<Integer> parseNeighbours(String rightHandSide) {
        String[] tokens = rightHandSide.split(NEIGHBOUR_SEPARATOR);
        List<Integer> neighbours = new ArrayList<>();
        for (String token : tokens) {
            neighbours.add(Integer.parseInt(token.trim()));
        }
        return neighbours;
    }

    /**
     * Counts the programs reachable from the provided starting program.
     *
     * @param startProgramId starting program id
     * @param graph undirected program graph
     * @return number of distinct programs in the connected component
     */
    private static int countConnectedComponentSize(int startProgramId, Map<Integer, List<Integer>> graph) {
        if (!graph.containsKey(startProgramId)) {
            return 0;
        }

        Set<Integer> visited = new HashSet<>();
        traverseConnectedComponent(startProgramId, graph, visited);
        return visited.size();
    }

    private static int countGroups(Map<Integer, List<Integer>> graph) {
        Set<Integer> allPrograms = new HashSet<>(graph.keySet());
        for (List<Integer> neighbours : graph.values()) {
            allPrograms.addAll(neighbours);
        }

        Set<Integer> visited = new HashSet<>();
        int groupCount = 0;

        for (int programId : allPrograms) {
            if (visited.contains(programId)) {
                continue;
            }
            traverseConnectedComponent(programId, graph, visited);
            groupCount++;
        }

        return groupCount;
    }

    /**
     * Marks every program in the same connected component as the starting program.
     *
     * @param startProgram starting program id
     * @param graph undirected program graph
     * @param visited mutable set of already-seen programs
     */
    private static void traverseConnectedComponent(int startProgram,
                                                   Map<Integer, List<Integer>> graph,
                                                   Set<Integer> visited) {
        ArrayDeque<Integer> frontier = new ArrayDeque<>();
        frontier.add(startProgram);
        visited.add(startProgram);

        while (!frontier.isEmpty()) {
            int currentProgram = frontier.poll();
            List<Integer> neighbours = graph.getOrDefault(currentProgram, List.of());
            for (int neighbour : neighbours) {
                if (visited.add(neighbour)) {
                    frontier.add(neighbour);
                }
            }
        }
    }

    /**
     * Reads the puzzle input file from the classpath.
     * @return list of input lines
     */
    private static List<String> readInput() {
        try {
            if (DigitalPlumberAOC2017Day12.class.getResource(INPUT_FILE) == null) {
                throw new IllegalStateException("Input file not found: " + INPUT_FILE);
            }
            Path inputPath = Path.of(DigitalPlumberAOC2017Day12.class.getResource(INPUT_FILE).toURI());
            return Files.readAllLines(inputPath);
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException("Failed to read input file", e);
        }
    }
}
